package com.example.secondhandmarket.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;

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

public class LoginActivity extends AppCompatActivity {
    private Button getCodeButton;
    private EditText inputCode;
    private EditText inputPhone;

    private Button loginButton;
    private TextView signIn;
    private boolean tag = true;
    private int i = 60;
    private String check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
         getCodeButton = findViewById(R.id.buttongetcode);
         inputCode = findViewById(R.id.input_code);
         inputPhone= findViewById(R.id.input_phone);

         loginButton = findViewById(R.id.login_button);
         signIn = findViewById(R.id.sign_in);
         
         getCodeButton.setOnClickListener(view -> {
             //获取验证码
             changeBtnGetCode();
         });
        
    }

    @SuppressLint("NonConstantResourceId")
    public void loginActivityListener(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_button://请求。登录
                String Phone = inputPhone.getText().toString();
                String Code = inputCode.getText().toString();
                if(isMobileNO(Phone) && !Code.equals("")){
                    post(Code, Phone);
                }else{
                    Toast.makeText(this, "账号格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_in:
                startActivity(new Intent(LoginActivity.this, SigninActivity.class));
                break;

        }
    }

    private void post(String phone, String code) {
        new Thread(()->{
            String url ="http://47.107.52.7:88/member/tran/user/login";

            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
                    .add("Content-Type", "application/json")
                    .build();


            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", code);
            bodyMap.put("phone", phone);
            // 将Map转换为字符串类型加入请求体中
            String body = bodyMap.toString();

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();

            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }


        }).start();
    }
    //回调
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            assert response.body() != null;
            System.out.println(response.body().string());
        }
    };

    private void changeBtnGetCode() {
        new Thread(()->{
            if (tag) {
                while (i > 0) {
                    i--;
                    //如果活动为空
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getCodeButton.setText( i + "s");
                            getCodeButton.setClickable(false);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tag = false;
            }
            i = 60;
            tag = true;
            LoginActivity.this.runOnUiThread(() -> {
                getCodeButton.setText("获取验证码");
                getCodeButton.setClickable(true);
            });
        }).start();
    }
    private boolean isMobileNO(String sphone) {
        String telRegex = "[1][358]\\d{9}";
        if (TextUtils.isEmpty(sphone))
            return false;
        else
            return sphone.matches(telRegex);

    }
}