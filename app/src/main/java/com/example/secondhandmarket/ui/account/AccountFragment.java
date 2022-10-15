package com.example.secondhandmarket.ui.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.secondhandmarket.BaseResponseBody;
import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.LoginActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appSecret.AppSecret;
import com.example.secondhandmarket.ui.account.traderecord.MyTradeRecord;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private TextView tvuserName, tvuserId, tvMoney;
    private TextView changeInfor;
    private TextView tvmyRelease, tvmyRecord;
    private ImageView ivavatar;
    private ImageView ivClearLogin;

    private Context mcontext;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public AccountFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mcontext = getContext();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        tvuserName = (TextView) view.findViewById(R.id.account_name);
        tvuserId = view.findViewById(R.id.user_id);
        tvMoney = view.findViewById(R.id.user_money);
        changeInfor = view.findViewById(R.id.my_information);
        tvmyRelease = view.findViewById(R.id.myrelease);
        tvmyRelease.setOnClickListener(this::MainActivityClickListener);
        tvmyRecord = view.findViewById(R.id.myrecord);
        ivavatar = view.findViewById(R.id.profile);
        ivClearLogin = view.findViewById(R.id.clear_login);

//        GetUserInfor getUserInfor = new GetUserInfor(mcontext);
//        String userName = getUserInfor.getUSerName();
//        String avatar = getUserInfor.getAvatar();
//        long userId = getUserInfor.getUSerID();
//        int money = getUserInfor.getUserMoney();
//
//        tvuserName.setText(userName);
//        tvuserId.setText(Long.toString(userId));
//        tvMoney.setText(Integer.toString(money));
//        Glide.with(mcontext)
//                .load(avatar)
//                .into(ivavatar);


        tvuserName.setOnClickListener(view1 -> {//如果没登陆，没登陆获取到的就是 !false
            if(!new GetUserInfor(mcontext).getIsLogin()){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        if(new GetUserInfor(mcontext).getIsLogin()){
            tvmyRecord.setOnClickListener(v -> {
                startActivity(new Intent(mcontext, MyTradeRecord.class));
            });

            changeInfor.setOnClickListener(v -> {
                startActivity(new Intent(mcontext, ChangeMyInformationActivity.class));
            });
        }

        ivClearLogin.setOnClickListener(v->{
            if(new GetUserInfor(mcontext).getIsLogin()){
               new AlertDialog.Builder(mcontext)
                       .setTitle("注销登录？")
                       .setMessage("确定注销登录？？")
                       //确认按钮
                       .setPositiveButton("确定", (dialogInterface, i) -> {
                            SharedPreferences sp = mcontext
                                    .getSharedPreferences(new GetUserInfor().MYSP_USER,Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp.edit();
                            editor.clear();
                            editor.apply();
                            Toast.makeText(mcontext, "注销成功", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(mcontext,LoginActivity.class));
//                            getActivity().finish();
                       })
                       //取消按钮
                       .setNegativeButton("取消", (dialogInterface, i) -> {

                       }).create().show();
            }else {
                new AlertDialog.Builder(mcontext)
                        .setTitle("你还没登陆呢！")
                        .setMessage("你还没登陆呢！请登录")
                        .create()
                        .show();
            }
        });
        return view;
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onStart() {
        super.onStart();
        GetUserInfor getUserInfor = new GetUserInfor(mcontext);
        String userName = getUserInfor.getUSerName();
        String avatar = getUserInfor.getAvatar();
        long userId = getUserInfor.getUSerID();
        int money = getUserInfor.getUserMoney();

        tvuserName.setText(userName);
        tvuserId.setText(Long.toString(userId));
        tvMoney.setText(Integer.toString(money));

        Glide.with(mcontext)
                .load(avatar)
                .into(ivavatar);
    }


    /*
    {
msg:"string"
code:0
data:{
TotalRevenue:0
TotalSpending:0
totalRevenue:0
totalSpending:0
}
}
     */
    static class costAndRevenue extends BaseResponseBody {
        private data data;

        public data getDtat() {
            return data;
        }

        private static class data {
            private int TotalRevenue;
            private int TotalSpending;
            private int totalRevenue;
            private int totalSpending;

            public int getTotalRevenue() {
                return TotalRevenue;
            }

            public int getTotalSpending() {
                return TotalSpending;
            }
        }
    }
    public void MainActivityClickListener(View view) {//完善点击事件
      //account Fragment 的点击监听
            int id = view.getId();
            //account Fragment
            if(id == R.id.myrelease && new GetUserInfor(mcontext).getIsLogin()){
                startActivity(new Intent(mcontext, MyReleaseActivity.class));
            }

    }

}