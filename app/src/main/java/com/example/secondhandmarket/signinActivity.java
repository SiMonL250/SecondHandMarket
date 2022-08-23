package com.example.secondhandmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.widget.Button;
import android.widget.EditText;

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

public class signinActivity extends AppCompatActivity {
    private EditText signInAccount, signInPhone, sigInCode;
    private Button signinButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signinButton = findViewById(R.id.sign_in_button);
        signInAccount = findViewById(R.id.sign_in_account);
        signInPhone = findViewById(R.id.sign_in_phone);
        sigInCode = findViewById(R.id.sign_in_code);
        signinButton.setOnClickListener(view -> {
            //获取账号并上传
            String signInAccountString = signInAccount.getText().toString().trim();
            String singInPasswordString = signInPhone.getText().toString().trim();
            String signInCode = sigInCode.getText().toString().trim();
            post();
        });
    }

    private void post() {
        new Thread(()->{
            String url = "http://47.107.52.7:88/member/tran/user/register";

            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "6e7ad529141b4ec18c355eff7abfd160")
                    .add("appSecret", "63421994d54e2abe54902b678072a31a94e66")
                    .add("Content-Type", "application/json")
                    .build();

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", sigInCode);
            bodyMap.put("phone", signInPhone);
            bodyMap.put("account", signInAccount);
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
            System.out.println(response.toString());
        }
    };
}