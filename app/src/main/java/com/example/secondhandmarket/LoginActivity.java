package com.example.secondhandmarket;

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
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        getCodeButton = findViewById(R.id.buttongetcode);
        inputCode = findViewById(R.id.input_code);
        inputPhone= findViewById(R.id.input_phone);
        Button loginButton = findViewById(R.id.login_button);



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

    private void loginPost(String phone, String code) {
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/user/login";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("code", code);
            bodyMap.put("phone", phone);
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);

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
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        assert response.body() != null;
                        ResponseBody body = response.body();

                        String json = new String(body.bytes());
                        LoginResponseBean responseBodylogin = new LoginResponseBean();
                        responseBodylogin = new Gson().fromJson(json, responseBodylogin.getClass());

                        GetUserInfor ginfor = new GetUserInfor();
                        sp = getSharedPreferences(ginfor.MYSP_USER, MODE_PRIVATE);
                        editor = sp.edit();
                        //ShareePreference 保存数据
                        editor.putBoolean(ginfor.MYSP_ISLOGIN, true);
                        editor.putString(ginfor.MYSP_USERNAME, responseBodylogin.getData().getUsername());
                        editor.putString(ginfor.MYSP_AVATAR, responseBodylogin.getData().getAvatar());
                        editor.putLong(ginfor.MYSP_USERID, responseBodylogin.getData().getId());
                        editor.putInt(ginfor.MYSP_MONEY, responseBodylogin.getData().getMoney());
                        editor.commit();
                        finish();
                    }
                });

        }).start();
    }


    @SuppressLint("NonConstantResourceId")
    public void loginActivityListener(View view) {
        int id = view.getId();
        if (id == R.id.sign_in) {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
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
                codeResponcebean = new Gson().fromJson(new String(body.bytes()), codeResponcebean.getClass());
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