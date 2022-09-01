package com.example.secondhandmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.secondhandmarket.databinding.ActivityMainBinding;
import com.example.secondhandmarket.ui.account.LoginActivity.LoginResponseBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_release,R.id.navigation_notifications,R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        LoginResponseBean usernow= (LoginResponseBean) intent.getSerializableExtra("user");
        if (usernow != null) {

            System.out.println(usernow.getMsg());
        }
    }

    //完善点击事件
    public void MainActivityClickListener(View view) {//account Fragment 的点击监听
        int id = view.getId();
        //home 和 message 有List View或RecycleView 可能需要设定自己的itemClick
        //account Fragment
        if(id == R.id.myrelease){
            startActivity(new Intent(MainActivity.this, MyReleaseActivity.class));
        }
    }
    }