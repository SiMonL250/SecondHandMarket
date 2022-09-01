package com.example.secondhandmarket.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.secondhandmarket.R;

public class ChangeMyInformationActivity extends AppCompatActivity {
    ImageView ivAvatar;
    EditText etName, etaddr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
    }
}