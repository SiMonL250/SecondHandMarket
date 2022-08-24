package com.example.secondhandmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private Button signinButton,getCodeButton;
    private boolean tag = true;
    private int i = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signinButton = findViewById(R.id.sign_in_button);
        signInAccount = findViewById(R.id.sign_in_account);
        signInPhone = findViewById(R.id.sign_in_phone);
        sigInCode = findViewById(R.id.sign_in_code);
        getCodeButton = findViewById(R.id.btn_getcode);



        signinButton.setOnClickListener(view -> {
            String codeString = sigInCode.getText().toString();
            String accountString = signInAccount.getText().toString();
            String phoneString = signInPhone.getText().toString();
            //获取账号并上传
            if(!codeString.equals("") && !accountString.equals("") && !phoneString.equals("")){
                post(accountString, phoneString, codeString);
                Toast.makeText(signinActivity.this, "success", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(signinActivity.this, "格式不正确", Toast.LENGTH_SHORT).show();
            }

        });
        getCodeButton.setOnClickListener(view -> {
            String phoneString = signInPhone.getText().toString();
            if(isMobileNO(phoneString)){
                changeBtnGetCode();
                get(phoneString);
            }else {
                Toast.makeText(signinActivity.this, "号码格式错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void post(String account,String phone, String code) {
        new Thread(()->{
            String url = "http://47.107.52.7:88/member/tran/user/register";

            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "6e7ad529141b4ec18c355eff7abfd160")
                    .add("appSecret", "63421994d54e2abe54902b678072a31a94e66")
                    .add("Content-Type", "application/json")
                    .build();

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", code);
            bodyMap.put("phone", phone);
            bodyMap.put("account", account);
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

            System.out.println(response.toString());
        }
    };

    private void get(String phone){
        new Thread(()->{
            String url = "http://47.107.52.7:88/member/tran/user/send?phone="+ phone;
            System.out.println(url);
            Headers headers = new Headers.Builder()
                    .add("appId", "6e7ad529141b4ec18c355eff7abfd160")
                    .add("appSecret", "63421994d54e2abe54902b678072a31a94e66")
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
               client.newCall(request).enqueue(callbackGetCode);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback callbackGetCode = new Callback() {
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
    //当发送验证码成功时，按钮样式变成倒计时
    private void changeBtnGetCode() {
        new Thread(()->{
            if (tag) {
                while (i > 0) {
                    i--;
                    //如果活动为空
                    signinActivity.this.runOnUiThread(new Runnable() {
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
            signinActivity.this.runOnUiThread(() -> {
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