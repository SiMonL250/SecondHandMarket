package com.example.secondhandmarket.ui.account;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.secondhandmarket.MainActivity;
import com.example.secondhandmarket.MyReleaseActivity;
import com.example.secondhandmarket.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    private TextView userName;
    private TextView myInfor;
    private TextView tvmyRelease, tvmyRecord;
    private  SharedPreferences sharedPreferences;
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        userName = (TextView) view.findViewById(R.id.account_name);
        myInfor = view.findViewById(R.id.my_information);
        tvmyRelease = view.findViewById(R.id.myrelease);
        tvmyRelease.setOnClickListener(this::MainActivityClickListener);
        tvmyRecord = view.findViewById(R.id.myrecord);

        sharedPreferences =  getContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("username","null").equals("null")) {
            userName.setText(sharedPreferences.getString("username", "null"));
        }

        userName.setOnClickListener(view1 -> {//如果没登陆，没登陆获取到的就是“null”
            if(sharedPreferences.getString("islogin","null").equals("null")){
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });


        myInfor.setOnClickListener(v->{
            startActivity(new Intent(getContext(), ChangeMyInformationActivity.class));
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        String st = sharedPreferences.getString("username","null");
        Log.d("accFSP", st);
    }

    public void MainActivityClickListener(View view) {//完善点击事件
      //account Fragment 的点击监听
            int id = view.getId();
            //account Fragment
            if(id == R.id.myrelease){
                startActivity(new Intent(getContext(), MyReleaseActivity.class));
            }

    }
}