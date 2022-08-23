package com.example.secondhandmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;

public class LoginActivity extends AppCompatActivity {
    private ImageView visibleButton;
    private EditText inputCode;
    private EditText inputPhone;
    private boolean isVisible = false;

    private Button loginButton;
    private TextView forgetPassword;
    private TextView signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
         visibleButton = findViewById(R.id.password_isvisible);
         inputCode = findViewById(R.id.input_code);
         inputPhone= findViewById(R.id.input_phone);

         loginButton = findViewById(R.id.login_button);
         forgetPassword = findViewById(R.id.forgot_pwd);
         signIn = findViewById(R.id.sign_in);
         
         visibleButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 isVisible = !isVisible;
                 if(isVisible){
                     visibleButton.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                     inputCode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                     inputCode.setSelection(inputCode.getText().length());//光标重回文字末尾
                 }else{
                     visibleButton.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                     inputCode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                     inputCode.setTypeface(Typeface.DEFAULT);
                     inputCode.setSelection(inputCode.getText().length());
                 }
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
                if(!Phone.equals("") && !Code.equals("")){
                    post();
                }else{
                    Toast.makeText(this, "账号格式不正确", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.sign_in:
                startActivity(new Intent(LoginActivity.this, signinActivity.class));
                break;

        }
    }

    private void post() {
        new Thread(()->{
            String url ="http://47.107.52.7:88/member/tran/user/login";

            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", "6e7ad529141b4ec18c355eff7abfd160")
                    .add("appSecret", "63421994d54e2abe54902b678072a31a94e66")
                    .add("Content-Type", "application/json")
                    .build();

            Map<String,Object> bodyMAp = new HashMap<>();

        }).start();
    }

}