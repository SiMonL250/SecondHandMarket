package com.example.secondhandmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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

public class SignupActivity extends AppCompatActivity {
    private EditText signInPhone, sigInCode;
    private Button signinButton, getCodeButton;

    private registerResponce registerResponce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        signinButton = findViewById(R.id.sign_in_button);
        signInPhone = findViewById(R.id.sign_in_phone);
        sigInCode = findViewById(R.id.sign_in_code);
        getCodeButton = findViewById(R.id.btn_getcode);

        getCodeButton.setOnClickListener(view -> {

            String phoneString = signInPhone.getText().toString();
            others function = new others();
            if(function.isMobileNO(phoneString)){
                function.changeBtnGetCode(getCodeButton,SignupActivity.this);
                new UseAPI().getCode(phoneString, SignupActivity.this,
                        new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                Looper.prepare();
                                Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                CodeResponse codeResponse = new CodeResponse();
                                codeResponse = new Gson().fromJson(
                                        new String(response.body().bytes()),codeResponse.getClass());
                            }
                        });
            }else {
                Toast.makeText(SignupActivity.this, "号码格式错误", Toast.LENGTH_SHORT).show();
            }
        });

        signinButton.setOnClickListener(view -> {
            String codeString = sigInCode.getText().toString();
            String phoneString = signInPhone.getText().toString();

            if(new others().isMobileNO(phoneString)){
                new UseAPI().loginRegister(phoneString, codeString, "register", callbackRegister);
                //注册完跳到登录
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();

            }else {
                Toast.makeText(SignupActivity.this, "格式不正确", Toast.LENGTH_SHORT).show();
            }
        });

    }
//local object user to save userName userId phone avatar addr,etc
    //还要对phone进行检查，看是否已经注册。
    //回调
    private final Callback callbackRegister = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            Looper.prepare();
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {

            ResponseBody body = response.body();
            registerResponce = new registerResponce();
            assert body != null;
            try{
                String json = new String(body.bytes());
                Gson gson = new Gson();
                registerResponce = gson.fromJson(json, registerResponce.getClass());
            }catch (IOException e){
                e.printStackTrace();
            }
            Looper.prepare();
            Toast.makeText(SignupActivity.this, registerResponce.getMsg(), Toast.LENGTH_SHORT).show();
            Looper.loop();
            System.out.println(registerResponce.getData());
        }
    };
    //responceBody 的类
/*{
code:200
msg:"成功"
data:"注册成功"
}
 */
    static class CodeResponse extends BaseResponseBody{
        private String data;

        public String getData() {
            return data;
        }
    }
    private static class registerResponce extends BaseResponseBody{
        private String data;
        public String getData() {
            return data;
        }

    }
}