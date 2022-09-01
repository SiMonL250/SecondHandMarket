package com.example.secondhandmarket.myrelease;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.example.secondhandmarket.appkey.appMobSDK;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Requestget {
    private String url = "http://47.107.52.7:88/member/tran/";

    public String getUrlsouldout() {
        return urlsouldout;
    }

    public void setUrlsouldout(String urlsouldout) {
        this.urlsouldout = urlsouldout;
    }

    public String getUrlmyRelease() {
        return urlmyRelease;
    }

    public void setUrlmyRelease(String urlmyRelease) {
        this.urlmyRelease = urlmyRelease;
    }

    private String urlsouldout = "trading/sell?";//url2
    private String urlmyRelease = "goods/myself?";//url2
    private String current = "current=";
    private String size = "&size=";
    private String userId = "&userId=";

    public void get(String url2, int currentint, int sizeint, int userid, Callback callback) {
        new Thread(() -> {

            // url路径
            String urlget = this.url + url2
                    + this.current + currentint
                    + this.size + sizeint
                    + this.userId + userid;
            Log.d("info", urlget);
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
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}