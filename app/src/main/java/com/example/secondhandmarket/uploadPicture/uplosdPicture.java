package com.example.secondhandmarket.uploadPicture;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class uplosdPicture {
    private Context context;
    private Handler handler ;

    public uplosdPicture(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void uploadPost(){
        String url = "http://47.107.52.7:88/member/tran/image/upload";

        // 请求头
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", new appMobSDK().appID)
                .add("appSecret", new appMobSDK().appSecret)
                .add("Content-Type", "multipart/form-data")
                .build();


        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        //请求组合创建
        Request request = new Request.Builder()
                .url(url)
                // 将请求头加至请求中
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
                .build();
        try {
            OkHttpClient client = new OkHttpClient();
            //发起请求，传入callback进行回调
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    ResponseBody body = response.body();
                    assert body !=null;
                    UpLoadResultBean upLoadResultBean = new UpLoadResultBean();
                    upLoadResultBean = new Gson().fromJson(new String(body.bytes()),upLoadResultBean.getClass());

                    Message msg = Message.obtain();
                    msg.what = 0x01;
                    msg.obj = upLoadResultBean.getData().getImageCode();//直接把imageCode传出去

                    handler.sendMessage(msg);
                }
            });
        }catch (NetworkOnMainThreadException ex){
            ex.printStackTrace();
        }
    }
}
