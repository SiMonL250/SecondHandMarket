package com.example.secondhandmarket.myrelease;

import android.os.NetworkOnMainThreadException;

import com.example.secondhandmarket.appSecret.AppSecret;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestDelete {
    private final String url = "http://47.107.52.7:88/member/tran/goods/delete?";
    public void delete(long goodsId, long userId, Callback callback){ //userId从文件获取，所以用int就行

            // url路径
            String url1 = this.url+"goodsId=" + goodsId+"&userId="+ userId;
//            System.out.println(url1);
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", new AppSecret().appID)
                    .add("appSecret", new AppSecret().appSecret)
                    .add("Content-Type", "application/json")
                    .build();


            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url1)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }

}
