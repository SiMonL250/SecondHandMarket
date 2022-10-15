package com.example.secondhandmarket;

import android.content.Context;
import android.os.NetworkOnMainThreadException;

import androidx.annotation.NonNull;

import com.example.secondhandmarket.appSecret.AppSecret;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UseAPI {
    private final Headers headers = new Headers.Builder()
            .add("appId", new AppSecret().appID)
            .add("appSecret", new AppSecret().appSecret)
            .add("Accept", "application/json, text/plain, */*")
            .build();

    public void getCode(String phone, Context context,Callback callback){

        String url = "http://47.107.52.7:88/member/tran/user/send?phone="+ phone;
//                System.out.println(url);

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

        }catch (NetworkOnMainThreadException ex){
            ex.printStackTrace();
        }

    }
    public void loginRegister(String phone, String code, String tail, Callback callback) {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/user/"+tail;

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", code);
            bodyMap.put("phone", phone);
            // 将Map转换为字符串类型加入请求体中
            String body = new Gson().toJson(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();

            OkHttpClient client = new OkHttpClient();
            //发起请求，传入callback进行回调
            client.newCall(request).enqueue(callback);

        }).start();
    }

    public void getCommodity(String tail, Callback callback){
        new Thread(()->{
            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/"+tail;

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
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }

        ).start();

    }

    public void getTypeNameList(Callback callback) {
        String url = "http://47.107.52.7:88/member/tran/goods/type";
        // 请求头
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", new AppSecret().appID)
                .add("appSecret", new AppSecret().appSecret)
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
        }catch (NetworkOnMainThreadException ex){
            ex.printStackTrace();
        }

    }

    public void getCostRevenue(long userId){
        new Thread(()->{
            String url ="http://47.107.52.7:88/member/tran/trading/allMoney?userId=" +userId;
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
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    }
                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
}
