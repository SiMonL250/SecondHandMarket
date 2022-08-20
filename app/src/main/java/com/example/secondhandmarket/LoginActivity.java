package com.example.secondhandmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private ImageView visibleButton;
    private EditText inputPassword;
    private EditText inputAccount;
    private boolean isVisible = false;

    private Button loginButton;
    private TextView forgetPassword;
    private TextView signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
         visibleButton = findViewById(R.id.password_isvisible);
         inputPassword = findViewById(R.id.input_password);
         inputAccount = findViewById(R.id.input_account);

         loginButton = findViewById(R.id.login_button);
         forgetPassword = findViewById(R.id.forgot_pwd);
         signIn = findViewById(R.id.sign_in);
         
         visibleButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 isVisible = !isVisible;
                 if(isVisible){
                     visibleButton.setImageResource(R.drawable.ic_baseline_remove_red_eye_24);
                     inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                     inputPassword.setSelection(inputPassword.getText().length());//光标重回文字末尾
                 }else{
                     visibleButton.setImageResource(R.drawable.ic_baseline_visibility_off_24);
                     inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                     inputPassword.setTypeface(Typeface.DEFAULT);
                     inputPassword.setSelection(inputPassword.getText().length());
                 }
             }
         });
        
    }

    public void loginActivityListener(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_button:
                String account = inputAccount.getText().toString();
                String password = inputPassword.getText().toString();
                //将账号密码获取验证，登录
                System.out.println(account+",,"+password);
                break;
            case R.id.sign_in:
                startActivity(new Intent(LoginActivity.this, signinActivity.class));
                break;

        }
    }
}