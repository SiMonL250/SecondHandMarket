package com.example.secondhandmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.commoditybean.GoodSingleInfor;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CommodityInformationActivity extends AppCompatActivity {
    private TextView goodName, goodPrice, goodCotent, releaserName, releaserId, releaserAddr;
    private Button buyitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);
        Intent intent = getIntent();
        Long goodsId = intent.getLongExtra("commodityId",-1);
        //TODO get buyer id;and money
        SharedPreferences sp = getPreferences(MODE_PRIVATE);

//        System.out.println(goodsId);
        buyitBtn = findViewById(R.id.buy_btn);
        buyitBtn.setOnClickListener(view ->{
            //TODO complete purchase
        });

    }

    private void getSIngleInfo(long goodsId){
        new Thread(() -> {
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
                    }
                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
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