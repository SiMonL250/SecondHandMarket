package com.example.secondhandmarket.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.secondhandmarket.CommodityInformationActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private  ListView goodsList;
    private TextView tvEmpty;
    private FragmentHomeBinding binding;
    private commodityAdapter mAdapter;
    private Context mcontext;
    private ResponseBodyBean responseBodyBeanGoods = new ResponseBodyBean();
    String FILENAME = "goods_file.json";
    private long userId;

    private String jsonStr;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        goodsList = view.findViewById(R.id.commodityList);
        tvEmpty = view.findViewById(R.id.ng);
//        try{
//            SharedPreferences sp = getContext().getSharedPreferences("mysp",Context.MODE_PRIVATE);
//            userId = sp.getLong("userId",0);
//        }catch (NullPointerException e){
//            Log.d("NullPointer", e.getMessage());
//        }

        //getAllCommodity(14);

       //adapter
        //货物id 在这个list里面

        List<GotCommodityBean> l = new ArrayList<>();
        if((l= initData()) != null){
            mAdapter = new commodityAdapter(l,mcontext);
            goodsList.setAdapter(mAdapter);
            tvEmpty.setVisibility(View.GONE);
        }


        goodsList.setOnItemClickListener(this);
        return view;
    }




    //将List改一改，将Commodity类放进去，直接用SimpleAdapter；
    private  List<GotCommodityBean> initData() {

        jsonStr = getFileFromSD(mcontext.getFilesDir()+"/"+FILENAME);

        ResponseBodyBean bean = new ResponseBodyBean();
        bean = new Gson().fromJson(jsonStr, bean.getClass());

        return bean.getData().getRecords();

    }

    //从手机存储卡路径下解析json,并返回String
    public static String getFileFromSD(String path) {
        String result = "";

        try {
            FileInputStream f = new FileInputStream(path);
            BufferedReader bis = new BufferedReader(new InputStreamReader(f));
            String line = "";
            while ((line = bis.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
    private void getAllCommodity(long userId) {

            // url路径
        //String url = "http://47.107.52.7:88/member/tran/goods/all?current=0&size=0&typeId=0&keyword=string&userId=0";
        String url = "http://47.107.52.7:88/member/tran/goods/all?current=1&size=10000"+"&userId="+userId;

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .get()
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
    }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            responseBodyBeanGoods = new ResponseBodyBean();
            ResponseBody body = response.body();
            assert body != null;
            responseBodyBeanGoods = new Gson().fromJson(new String(body.bytes())
                    ,responseBodyBeanGoods.getClass());

            FileOutputStream fos = null;
            try{
                fos = getContext().openFileOutput(FILENAME,Context.MODE_PRIVATE);
                fos.write(responseBodyBeanGoods.toString().getBytes(StandardCharsets.UTF_8));
                fos.close();
            }catch (IOException e){
                Log.d("IOExption", e.getMessage());
            }


        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView tvcommodityID = view.findViewById(R.id.textViewidd);
        Long commodityId = Long.parseLong(tvcommodityID.getText().toString());
        //可以用view.findViewById()方法来获取所点击item中的控件。

//        System.out.println(commodityId);
        Intent intent = new Intent(HomeFragment.this.mcontext, CommodityInformationActivity.class);
        //send id to get infor
        intent.putExtra("commodityId",commodityId);
        startActivity(intent);

    }

}

