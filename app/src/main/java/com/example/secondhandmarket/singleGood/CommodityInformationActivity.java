package com.example.secondhandmarket.singleGood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.commoditybean.GoodSingleInfor;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CommodityInformationActivity extends AppCompatActivity {
    private TextView tvgoodName, tvgoodPrice, tvgoodTypeName, tvreleaserName, tvreleaserAddr, tvrelesearId;
    private Button buyitBtn;
    private ImageView  releaserAvatar;
    private Banner banner;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);

        tvgoodName = findViewById(R.id.information_name);
        tvgoodPrice = findViewById(R.id.information_price);
        tvgoodTypeName = findViewById(R.id.tv_content);
        tvreleaserName = findViewById(R.id.commo_info_user_name);
        tvreleaserAddr = findViewById(R.id.commo_info_user_addr);
        tvrelesearId = findViewById(R.id.commo_info_user_id);
        banner = findViewById(R.id.information_picture);
        releaserAvatar = findViewById(R.id.commo_info_user_avatar);

        Intent intent = getIntent();
        long goodsId = intent.getLongExtra("commodityId",-1);//商品id
        int goodsPrice = intent.getIntExtra("commodityPrice",-1);//商品价格

        //TODO get buyer id;and money
        SharedPreferences sharedPreferences_getuser = getSharedPreferences("mysp",MODE_PRIVATE);//购买人 id
        long buyerId = sharedPreferences_getuser.getLong("userId", -1);


        if(goodsId != -1){
            getSingleInfo(goodsId);
        }

//        System.out.println(goodsId);
        buyitBtn = findViewById(R.id.buy_btn);
        buyitBtn.setOnClickListener(view ->{
            //TODO complete purchase
            //goodsId,goodsPrice,buyerId
            long sellerId = Long.parseLong(tvrelesearId.getText().toString());
            System.out.println("sellerId"+sellerId);
        });

    }

    private void getSingleInfo(long goodsId){
        new Thread(()->{
            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/details?goodsId="+goodsId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
                    .add("Accept", "application/json, text/plain, */*")
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
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(CommodityInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        ResponseBody body = response.body();
                        assert body != null;
                        GotInfoBean gotInfoBean = new GotInfoBean();
                        try {
                            gotInfoBean = new Gson().fromJson(new String(body.bytes()),GotInfoBean.class);
                            //TODO process data
                        }catch (Exception e){
                            e.printStackTrace();
                        }


                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = gotInfoBean;
                        new Handler(Looper.getMainLooper()){
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 0){
                                    GotInfoBean mgotBean = (GotInfoBean) msg.obj;
                                    tvgoodName.setText(mgotBean.getData().getContent());
                                    tvgoodPrice.setText(mgotBean.getData().getPrice() + "元");
                                    tvgoodTypeName.setText(mgotBean.getData().getTypeName());
                                    tvreleaserName.setText(mgotBean.getData().getUsername());
                                    tvreleaserAddr.setText(mgotBean.getData().getAddr());
                                    tvrelesearId.setText(Long.toString(mgotBean.getData().getTuserId()));

                                    String avatar = mgotBean.getData().getAvatar();
                                    if(avatar != null && avatar.length()!=0){
                                        Glide.with(CommodityInformationActivity.this)
                                                .load(avatar)
                                                .into(releaserAvatar);
                                    }


                                    List<String> list = mgotBean.getData().getImageUrlList();
                                    banner.setAdapter(new BannerImageAdapter<String>(list) {
                                        @Override
                                        public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                                            Glide.with(holder.itemView)
                                                    .load(data)
                                                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                                    .into(holder.imageView);
                                        }
                                    }).addBannerLifecycleObserver(CommodityInformationActivity.this)
                                            .setIndicator(new CircleIndicator(CommodityInformationActivity.this));

                                }
                            }
                        }.sendMessage(msg);
                    }


                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }


        ).start();

    }

    static class GotInfoBean{
        private String msg;
        private int code;
        private GoodSingleInfor data;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public GoodSingleInfor getData() {
            return data;
        }

        public void setData(GoodSingleInfor data) {
            this.data = data;
        }
    }
}
/*
{
  "code": 200,
  "msg": "成功",
  "data": {
    "id": "18",
    "appKey": "bf8da9cfafbb4bd29c8b0b1570e1a117",
    "tUserId": "13",
    "imageCode": "1566069915104972800",
    "content": "sgg",
    "price": 5454,
    "addr": "各睡各的",
    "typeId": 1,
    "typeName": "手机",
    "status": 1,
    "createTime": "1662269399774",
    "username": "15123902554",
    "avatar": "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/04/a03eb2f0-f01b-4f74-8314-2aa7e4e8dbff.jpg",
    "imageUrlList": [
      "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/03/2c285ead-50db-42a3-96a4-41e28e7d2726.jpg",
      "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/09/03/7f7c357e-c196-4aa7-b7a8-b39a2af669e6.jpg"
    ],
    "appIsShare": 1,
    "tuserId": "13"
  }
}
 */