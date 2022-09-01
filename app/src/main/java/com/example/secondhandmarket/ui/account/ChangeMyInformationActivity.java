package com.example.secondhandmarket.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.secondhandmarket.R;

public class ChangeMyInformationActivity extends AppCompatActivity {
    private ImageView ivAvatar;
    private EditText etName, etAddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_information);
        ivAvatar = findViewById(R.id.iv_avatar);
        etName = findViewById(R.id.et_name);
        etAddr = findViewById(R.id.et_addr);
    }

    public void ChangeInfoClick(View view) {
        int id = view.getId();
        if(id == R.id.iv_avatar){
            Log.d("test", "ChangeInfoClick: ");
        }
        if(id == R.id.btn_sumbit){
            Log.d("test", "ChangeInfoClick: ");
        }
    }
}