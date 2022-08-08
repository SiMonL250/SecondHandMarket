package com.example.secondhandmarket;

import androidx.appcompat.app.AppCompatActivity;

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
}