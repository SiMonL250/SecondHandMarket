package com.example.secondhandmarket.ui.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.LoginActivity;
import com.example.secondhandmarket.MyReleaseActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.getURLimage.getURLimage;
import com.example.secondhandmarket.traderecord.MyTradeRecord;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private TextView tvuserName, tvuserId, tvMoney;
    private TextView myInfor;
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
        myInfor = view.findViewById(R.id.my_information);
        tvmyRelease = view.findViewById(R.id.myrelease);
        tvmyRelease.setOnClickListener(this::MainActivityClickListener);
        tvmyRecord = view.findViewById(R.id.myrecord);
        ivavatar = view.findViewById(R.id.profile);
        ivClearLogin = view.findViewById(R.id.clear_login);

        GetUserInfor getUserInfor = new GetUserInfor(mcontext);
        String userName = getUserInfor.getUSerName();
        String avatar = getUserInfor.getAvatar();
        long userId = getUserInfor.getUSerID();
        int money = getUserInfor.getUserMoney();

        tvuserName.setText(userName);
        tvuserId.setText(Long.toString(userId));
        tvMoney.setText(Integer.toString(money));

        if(avatar != null){
            new Thread(() -> {
                    Bitmap avatarbm = new getURLimage().getimage(avatar);
                    if(avatarbm !=null){
                        Message msg = Message.obtain();
                        msg.obj = avatarbm;
                        msg.what = 0x34;
                        new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 0x34){
                                    ivavatar.setImageBitmap((Bitmap) msg.obj);
                                }
                            }
                        }.sendMessage(msg);
                    }
            }).start();
        }

        tvuserName.setOnClickListener(view1 -> {//如果没登陆，没登陆获取到的就是 !false
            if(!new GetUserInfor(mcontext).getIsLogin()){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        tvmyRecord.setOnClickListener(v->{
            startActivity(new Intent(mcontext, MyTradeRecord.class));
        });

        myInfor.setOnClickListener(v->{
            startActivity(new Intent(mcontext, ChangeMyInformationActivity.class));
        });

        ivClearLogin.setOnClickListener(v->{
            if(new GetUserInfor(mcontext).getIsLogin()){
               new AlertDialog.Builder(mcontext)
                       .setTitle("注销登录？")
                       .setMessage("确定注销登录？？")
                       //确认按钮
                       .setPositiveButton("确定", (dialogInterface, i) -> {

                       })
                       //取消按钮
                       .setNegativeButton("取消", (dialogInterface, i) -> {

                       }).create().show();
                //TODO 清除登录
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

    @Override
    public void onStart() {
        super.onStart();
    }

    public void MainActivityClickListener(View view) {//完善点击事件
      //account Fragment 的点击监听
            int id = view.getId();
            //account Fragment
            if(id == R.id.myrelease){
                startActivity(new Intent(mcontext, MyReleaseActivity.class));
            }

    }
}