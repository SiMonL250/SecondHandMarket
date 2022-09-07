package com.example.secondhandmarket.myrelease.soldout;

import static com.example.secondhandmarket.databinding.FragmentNewReleaseSoldoutBinding.inflate;

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
    private List<Map<String,String>> mDataList = new ArrayList<>();
    private MyReleaseAdapter myReleaseAdapter;
    private ImageView delete;


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
        delete = view.findViewById(R.id.iv_my_del);
        initData();
        //对mDataList初始化
        myReleaseAdapter = new MyReleaseAdapter(mDataList);
        mRecyclerViewList.setAdapter(myReleaseAdapter);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        delete.setOnClickListener(view1 -> {
            //TODO 获取两个Id
            new RequestDelete().delete("0", "12");
        });
        return view;
    }
    void initData(){
        callbackForMyGoods callback = new callbackForMyGoods();
        new Requestget().get(new Requestget().getUrlmyRelease(),1,100,12
                ,callback.callback(1,getContext()));
        mDataList = callback.getL();
    }
}