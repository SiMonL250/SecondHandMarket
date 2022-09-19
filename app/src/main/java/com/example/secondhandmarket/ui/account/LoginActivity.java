package com.example.secondhandmarket.ui.account;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
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
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {
    private Button getCodeButton;
    private EditText inputCode;
    private EditText inputPhone;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private boolean tag = true;
    private int i = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        getCodeButton = findViewById(R.id.buttongetcode);
        inputCode = findViewById(R.id.input_code);
        inputPhone= findViewById(R.id.input_phone);
        Button loginButton = findViewById(R.id.login_button);

        sp = getSharedPreferences("user", MODE_PRIVATE);
        editor = sp.edit();

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

        loginButton.setOnClickListener(v->{
            String Phone = inputPhone.getText().toString().trim();
            String Code = inputCode.getText().toString().trim();
             if(!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Code)){
                 loginPost(Phone, Code);

             }else{
                 Toast.makeText(this, "账号格式不正确", Toast.LENGTH_SHORT).show();
             }
         });
        
    }


    @SuppressLint("NonConstantResourceId")
    public void loginActivityListener(View view) {
        int id = view.getId();
        if (id == R.id.sign_in) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        }
    }

    private void loginPost(String phone, String code) {
            String url ="http://47.107.52.7:88/member/tran/user/login";

        Headers headers = new Headers.Builder()
                .add("appId", new appMobSDK().appID)
                .add("appSecret", new appMobSDK().appSecret)
                .add("Accept", "application/json, text/plain, */*")
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
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, Response response) throws IOException {//处理responce

                        assert response.body() != null;
                        ResponseBody body = response.body();

                        String json = new String(body.bytes());
                        LoginResponseBean responseBodylogin = new LoginResponseBean();
                        Gson gson = new Gson();
                        responseBodylogin = gson.fromJson(json, responseBodylogin.getClass());

                        //ShareePreference 保存数据
                        if(responseBodylogin.getCode() == 200){
                            editor.putBoolean("islogin",true);
                            user d = new user();
                            d = responseBodylogin.getData();

                            editor.putString("userName",d.getUsername());
                            editor.putString("avatar",d.getAvatar());
                            editor.putLong("userId",d.getId());
                            editor.putInt("money",d.getMoney());
                            editor.apply();
                            finish();
                        }


                    }
                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
    }

    private void get(String phone){

        String url = "http://47.107.52.7:88/member/tran/user/send?phone="+ phone;
//                System.out.println(url);
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
        public void onResponse(@NonNull Call call, Response response) throws NullPointerException {
            //关闭此页面，应用为以登录状态
            okhttp3.ResponseBody body = response.body();
            codeResponce codeResponcebean = new codeResponce();
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
//            Log.d("data", codeResponcebean.getData());
            Looper.loop();

        }
    };


    private void changeBtnGetCode() {
        new Thread(()->{
            if (tag) {
                while (i > 0) {
                    i--;
                    //如果活动为空
                    LoginActivity.this.runOnUiThread(() -> {
                        getCodeButton.setText( i + "s");
                        getCodeButton.setClickable(false);
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
               // getCodeButton.setClickable(false);
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

    static class codeResponce{
        //{"code":500,"msg":"当前验证码未失效，请勿频繁获取验证码","data":null}
        private int code;
        private String msg;
        private String data;
        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public String getData() {
            return data;
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
    public static class LoginResponseBean{

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
        private user data;

        public LoginResponseBean(){}

        public int getCode() {
            return code;
        }
        public String getMsg() {
            return msg;
        }

        public user getData() {
            return data;
        }
        @NonNull
        @Override
        public String toString() {
            return "ResponseBody{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", data=" +data.toString()+
                    '}';
        }
    }

}