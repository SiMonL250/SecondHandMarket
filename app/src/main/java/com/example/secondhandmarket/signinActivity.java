package com.example.secondhandmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class signinActivity extends AppCompatActivity {
    private EditText signInAccount, signInPassword;
    private Button signinButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signinButton = findViewById(R.id.sign_in_button);
        signInAccount = findViewById(R.id.sign_in_account);
        signInPassword = findViewById(R.id.sign_in_password);

        signinButton.setOnClickListener(view -> {
            //获取账号密码并上传
        });
    }
}