package com.example.secondhandmarket.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.LoginActivity;
import com.example.secondhandmarket.commoditybean.AllGoodsListBean;
import com.example.secondhandmarket.singleGood.CommodityInformationActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.AllGoodsBean;
import com.example.secondhandmarket.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private  ListView goodsList;
    private TextView tvEmpty;
    private FragmentHomeBinding binding;
    private commodityAdapter mAdapter;
    private Context mcontext;
    private long userId;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        goodsList = view.findViewById(R.id.commodityList);
        tvEmpty = view.findViewById(R.id.ng);
        long userId = new GetUserInfor(mcontext).getUSerID();
        if(userId != -1){
            getAllCommodity(userId);
        }else {
            Toast.makeText(mcontext, "No User Present! Please Log In", Toast.LENGTH_SHORT).show();

            TextView plsLogin = new TextView(mcontext);
            plsLogin.setText("登录");
            plsLogin.setTextSize(48);
            plsLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            plsLogin.getPaint().setAntiAlias(true);//抗锯齿

            plsLogin.setTextColor(getResources().getColor(android.R.color.darker_gray));
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
            layoutParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;

            plsLogin.setLayoutParams(layoutParams);
            ConstraintLayout constraintLayout = view.findViewById(R.id.hf);
            constraintLayout.addView(plsLogin);
            plsLogin.setId(R.id.pls_login);
            plsLogin.setOnClickListener(v->{
                startActivity(new Intent(mcontext, LoginActivity.class));
            });
        }


        tvEmpty.setVisibility(View.GONE);

        goodsList.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        long userIdonstart = new GetUserInfor(mcontext).getUSerID();
        if(userIdonstart != -1){
            getAllCommodity(userIdonstart);
            try{
                TextView plslogin = getView().findViewById(R.id.pls_login);
                plslogin.setVisibility(View.GONE);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    private void getAllCommodity(long userId) {
        new Thread(() -> {
            String url = "http://47.107.52.7:88/member/tran/goods/all?size=1000&userId=" + userId;

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
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            Looper.prepare();
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {

            AllGoodsBean allGoodsBean = new AllGoodsBean();
            ResponseBody body = response.body();
            assert body != null;
            allGoodsBean = new Gson().fromJson(new String(body.bytes())
                    , allGoodsBean.getClass());

            AllGoodsListBean data = allGoodsBean.getData();
            if(data !=null && data.getRecords().size()!=0){
                Message message = Message.obtain();
                message.what = 0x11;
                message.obj = data;
                new Handler(Looper.getMainLooper()){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if(msg.what == 0x11){
                            AllGoodsListBean allGoodsListBean = (AllGoodsListBean) msg.obj;
                            List<GotCommodityBean> list = allGoodsListBean.getRecords();
                            mAdapter = new commodityAdapter(list,mcontext);
                            goodsList.setAdapter(mAdapter);
                        }
                    }
                }.sendMessage(message);
            }
        }
    };
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tvcommodityID = view.findViewById(R.id.textViewidd);
        TextView tvcmmodityPrice = view.findViewById(R.id.commodityPrice);
        Long commodityId = Long.parseLong(tvcommodityID.getText().toString());
        //可以用view.findViewById()方法来获取所点击item中的控件。
        int commodityPrice =  Integer.parseInt(tvcmmodityPrice.getText().toString());
//        System.out.println(commodityId);
        Intent intent = new Intent(HomeFragment.this.mcontext, CommodityInformationActivity.class);
        //send id to get infor
        intent.putExtra("commodityId",commodityId);
        intent.putExtra("commodityPrice",commodityPrice);
        startActivity(intent);

    }

}

