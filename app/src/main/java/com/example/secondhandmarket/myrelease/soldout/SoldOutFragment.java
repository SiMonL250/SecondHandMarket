package com.example.secondhandmarket.myrelease.soldout;

import static com.example.secondhandmarket.databinding.FragmentNewReleaseSoldoutBinding.inflate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.myrelease.MyReleaseAdapter;
import com.example.secondhandmarket.myrelease.RequestDelete;
import com.example.secondhandmarket.myrelease.Requestget;
import com.example.secondhandmarket.myrelease.callbackForMyGoods;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoldOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoldOutFragment extends Fragment {
    private RecyclerView mRecyclerViewList;
    private MyReleaseAdapter myReleaseAdapter;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;



    public SoldOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoldOutFragment.
     */
    public static SoldOutFragment newInstance(String param1, String param2) {
        SoldOutFragment fragment = new SoldOutFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewBinding binding = inflate(inflater,container,false);
        View view= binding.getRoot();
        mRecyclerViewList = view.findViewById(R.id.recyeView);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("mysp", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        //对mDataList初始化
        //List<Map<String,String>>
        myReleaseAdapter = new MyReleaseAdapter(initData(userId));
        mRecyclerViewList.setAdapter(myReleaseAdapter);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    private List<Map<String,String>> initData(int id){
        //TODO 用SharedPreference 获取ID
        callbackForMyGoods callback = new callbackForMyGoods();
        new Requestget().get(new Requestget().getUrlmyRelease(),1,100, id
                ,callback.callback(1,getContext()));
        return callback.getL();
    }
}