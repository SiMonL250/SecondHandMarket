package com.example.secondhandmarket.ui.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhandmarket.MainActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
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
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;

public class LoginActivity extends AppCompatActivity {
    private Button getCodeButton;
    private EditText inputCode;
    private EditText inputPhone;

    private Button loginButton;
    private TextView signIn;
    private boolean tag = true;
    private int i = 60;
    private LoginActivity.codeResponce codeResponcebean;
    private final Gson gson = new Gson();
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
             String phone = inputPhone.getText().toString();
             if(isMobileNO(phone)){
                 changeBtnGetCode();
                 get(phone);
             }else {
                 Toast.makeText(this, "号码格式错误", Toast.LENGTH_SHORT).show();
             }

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
                    post(Phone, Code);
                }else{
                    Toast.makeText(this, "账号格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_in:
                startActivity(new Intent(LoginActivity.this, SigninActivity.class));
                finish();
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
                client.newCall(request).enqueue(callbackLogin);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }


        }).start();
    }
    //回调
    private final Callback callbackLogin = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            Looper.prepare();
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {//处理responce

            Type jsonType = new TypeToken<ResponseBody<Object>>(){}.getType();
            // 获取响应体的json串
            assert response.body() != null;
            String body = response.body().string();
            Log.d("info", body);
            // 解析json串到自己封装的状态
            ResponseBody<Object> dataResponseBody = gson.fromJson(body,jsonType);
            Log.d("info", dataResponseBody.toString());
            //成功登录 把数据传给MainActivity，

            if(dataResponseBody.getCode()==200){
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user",dataResponseBody);
                startActivity(intent);
            }

        }
    };


    private void get(String phone){

        String url = "http://47.107.52.7:88/member/tran/user/send?phone="+ phone;
//                System.out.println(url);
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
            client.newCall(request).enqueue(callGetCode);

        }catch (NetworkOnMainThreadException ex){
            ex.printStackTrace();
        }
    }
    Callback callGetCode =new Callback() {

        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            Looper.prepare();
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //关闭此页面，应用为以登录状态
            okhttp3.ResponseBody body = response.body();
            codeResponcebean= new codeResponce();
            assert body != null;
            try{
                String json = new String(body.bytes());
                Gson gson = new Gson();
                codeResponcebean = gson.fromJson(json, codeResponcebean.getClass());
            }catch (IOException e){
                e.printStackTrace();
            }
            Looper.prepare();
            Toast.makeText(LoginActivity.this, codeResponcebean.getMsg(), Toast.LENGTH_SHORT).show();
            Looper.loop();


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
                getCodeButton.setText("输入验证码");
                getCodeButton.setClickable(false);
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

    private class codeResponce{
        //{"code":500,"msg":"当前验证码未失效，请勿频繁获取验证码","data":null}
        private int code;
        private String msg;
        private String data;
        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
    /*
     {
    msg:"string"
    code:0
    data:{
        appKey:"string"
        avatar:"string"
        id:0//user 类的userId
        money:0
        username:"string"
        }
    }
     */
    public static class ResponseBody <T> implements Serializable {

        /**
         * 业务响应码
         */
        private int code;
        /**
         * 响应提示信息
         */
        private String msg;
        /**
         * 响应数据
         */
        private T data;

        public ResponseBody(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }
        public T getData() {
            return data;
        }

        @NonNull
        @Override
        public String toString() {
            return "ResponseBody{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}