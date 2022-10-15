package com.example.secondhandmarket;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class LoginActivity extends AppCompatActivity {
    private Button getCodeButton;
    private EditText inputCode;
    private EditText inputPhone;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

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
             if(new others().isMobileNO(phone)){
                 new others().changeBtnGetCode(getCodeButton,LoginActivity.this);

                 new UseAPI().getCode(phone, LoginActivity.this, new Callback() {
                     @Override
                     public void onFailure(@NonNull Call call, @NonNull IOException e) {
                         Looper.prepare();
                         Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                         Looper.loop();
                     }

                     @Override
                     public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        CodeResponse codeResponse = new CodeResponse();

                        codeResponse = new Gson().fromJson(
                                new String(response.body().bytes()),codeResponse.getClass());

                        Looper.prepare();
                        Toast.makeText(LoginActivity.this, codeResponse.getMsg(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                     }
                 });
             }else {
                 Toast.makeText(this, "号码格式错误", Toast.LENGTH_SHORT).show();
             }

         });

        loginButton.setOnClickListener(v->{
            String Phone = inputPhone.getText().toString().trim();
            String Code = inputCode.getText().toString().trim();
             if(!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Code)){

                 new UseAPI().loginRegister(Phone,Code,"login",new Callback() {
                     @Override
                     public void onFailure(@NonNull Call call, @NonNull IOException e) {
                         e.printStackTrace();
                     }

                     @Override
                     public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                         ResponseBody body = response.body();
                         assert body != null;
                         String json = new String(body.bytes());
                         LoginActivity.LoginResponseBean responseBodylogin = new LoginActivity.LoginResponseBean();
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
    static class CodeResponse extends BaseResponseBody{
        private String data;

        public String getData() {
            return data;
        }
    }
    static class LoginResponseBean extends BaseResponseBody{

        private user data;

        public LoginResponseBean(){}

        public user getData() {
            return data;
        }
    }

}