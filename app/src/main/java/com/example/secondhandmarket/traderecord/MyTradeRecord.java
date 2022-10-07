package com.example.secondhandmarket.traderecord;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.getURLimage.getURLimage;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyTradeRecord extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    List<TradeRecordDataBean> list;

    Handler handler = new Handler(Looper.getMainLooper()){//handler 用于线程间的通信
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0xec){
                list = (List<TradeRecordDataBean>) msg.obj;
                RecordAdapter recordAdapter = new RecordAdapter(list);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(MyTradeRecord.this));
                mRecyclerView.setAdapter(recordAdapter);

                //无记录
                if(recordAdapter.getItemCount() == 0){
                    LinearLayout.LayoutParams layoutParams =
                            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    TextView empty = new TextView(MyTradeRecord.this);
                    empty.setLayoutParams(layoutParams);
                    empty.setText(R.string.norecord);
                    empty.setTextSize(36);
                    empty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);//居中

                    LinearLayout father = findViewById(R.id.father);
                    father.addView(empty);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trade_record);

        mRecyclerView = findViewById(R.id.record_list);

        long userId;
        GetUserInfor getUserInfor = new GetUserInfor(MyTradeRecord.this);
        userId = getUserInfor.getUSerID();
        if(userId != -1){
            getTradeRecord(userId);

        }
    }
    //method get()
    private void getTradeRecord(long userId){


            // url路径
            String url = "http://47.107.52.7:88/member/tran/trading/records?size=1000&userId="+userId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
                    .build();

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(MyTradeRecord.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        assert body != null;
                        TradeRecordBean tradeRecordBean=
                                new Gson().fromJson(new String(body.bytes()),TradeRecordBean.class);

                        Message msg = Message.obtain();
                        msg.what = 0xec;
                        msg.obj = tradeRecordBean.getData().getRecords();//数据列表

                        handler.sendMessage(msg);//handler

                    }
                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
    }

//adapter
    static class RecordAdapter extends RecyclerView.Adapter<viewholder>{
        List<TradeRecordDataBean> list;

    public RecordAdapter(List<TradeRecordDataBean> list) {
        this.list = list;
    }

    @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View item = View.inflate(parent.getContext(), R.layout.my_trade_recorde_layout_items,null);
            viewholder vh = new viewholder(item);
            return vh;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            TradeRecordDataBean trdb = list.get(position);

            holder.myRecordGoodName.setText(trdb.getGoodsDescription());
            holder.myRecordPrice.setText(Integer.toString(trdb.getPrice()));
            holder.myRecordSellerName.setText(trdb.getSellerName());
            holder.myRecordBuyerName.setText(trdb.getBuyerName());
            //卖家头像
            setAvatar(trdb.getSellerAvatar(), holder.myRecordSellerAvatar,0xee);
            //买家头像
            setAvatar(trdb.getBuyerAvatar(), holder.myRecoredBuyerAvatar,0xef);
            //商品图片
            if(trdb.getImageUrlList().size()!=0){
                new Thread(()->{
                    Bitmap bmgoodpic = new getURLimage().getimage(trdb.getImageUrlList().get(0));
                    Message msg = Message.obtain();
                    msg.what = 0xed;
                    msg.obj = bmgoodpic;

                    new Handler(Looper.getMainLooper()){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == 0xed){
                                holder.myRecordImage.setImageBitmap((Bitmap)msg.obj);
                            }
                        }
                    }.sendMessage(msg);
                }).start();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setAvatar(String avatarUrl,ImageView imageView, int wh){
            if(avatarUrl != null){
                new Thread(()->{
                    Bitmap bmavatar = new getURLimage().getimage(avatarUrl);
                    Message msg = Message.obtain();
                    msg.what = wh;
                    msg.obj = bmavatar;

                    new Handler(Looper.getMainLooper()){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if(msg.what == wh){
                                imageView.setImageBitmap((Bitmap)msg.obj);
                            }
                        }
                    }.sendMessage(msg);
                }).start();
            }
        }
    }

    private static class viewholder extends RecyclerView.ViewHolder {
        TextView myRecordGoodName, myRecordPrice, myRecordSellerName,myRecordBuyerName;
        ImageView myRecordImage, myRecordSellerAvatar, myRecoredBuyerAvatar;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            myRecordGoodName = itemView.findViewById(R.id.record_goods_name);
            myRecordPrice = itemView.findViewById(R.id.record_goods_price);
            myRecordSellerName= itemView.findViewById(R.id.record_seller_name);
            myRecordBuyerName= itemView.findViewById(R.id.record_buyer_name);

            myRecordImage= itemView.findViewById(R.id.record_goods_pic);
            myRecordSellerAvatar= itemView.findViewById(R.id.record_seller_avatar);
            myRecoredBuyerAvatar = itemView.findViewById(R.id.record_buyer_avatar);
        }
    }


}