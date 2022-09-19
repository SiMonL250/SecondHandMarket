package com.example.secondhandmarket.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.secondhandmarket.singleGood.CommodityInformationActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    private ResponseBodyBean responseBodyBeanGoods = new ResponseBodyBean();
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

        getAllCommodity(14);

        tvEmpty.setVisibility(View.GONE);

        goodsList.setOnItemClickListener(this);
        return view;
    }

    //从手机存储卡路径下解析json,并返回String
    public static String getFileFromSD(String path) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

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
        }
        ).start();
        // url路径
        //String url = "http://47.107.52.7:88/member/tran/goods/all?current=0&size=0&typeId=0&keyword=string&userId=0";

    }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            Looper.prepare();
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            responseBodyBeanGoods = new ResponseBodyBean();
            ResponseBody body = response.body();
            assert body != null;
            responseBodyBeanGoods = new Gson().fromJson(new String(body.bytes())
                    ,responseBodyBeanGoods.getClass());


            Message message = Message.obtain();
            message.what = 0x11;
            message.obj = responseBodyBeanGoods.getData().getRecords();
            new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if(msg.what == 0x11){
                        List<GotCommodityBean> list = (List<GotCommodityBean>) msg.obj;
                        mAdapter = new commodityAdapter(list,mcontext);
                        goodsList.setAdapter(mAdapter);
                    }
                }
            }.sendMessage(message);
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

