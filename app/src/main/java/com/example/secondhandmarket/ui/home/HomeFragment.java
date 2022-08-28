package com.example.secondhandmarket.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.databinding.FragmentHomeBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private  ListView goodsList;
    private FragmentHomeBinding binding;
    private SimpleAdapter mAdapter;
    private Context mcontext;

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

       //adapter
        mAdapter = new SimpleAdapter(getActivity(), initData(),R.layout.commodity_list_item,new String[]{"title","p"},new int[]{R.id.commodityName,R.id.commodityPrice});

        goodsList.setAdapter(mAdapter);
        goodsList.setOnItemClickListener(this);
        return view;
    }
//将List改一改，将Commodity类放进去，直接用SimpleAdapter；
    private List<Map<String,String>> initData() {
        get();
        String [] titles={"水果1","水果2","水果3","水果4","水果5","水果6","水果7"};
        List<Map<String,String>> list= new ArrayList<>();
        for(int i=0;i<7;i++){
            Map map = new HashMap();
            map.put("title",titles[i]);
            map.put("p",titles[i]);
            list.add(map);
        }
        return list;
    }

    private void get() {

            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/all?userId=0";

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
//            try {
//                OkHttpClient client = new OkHttpClient();
//                //发起请求，传入callback进行回调
//                client.newCall(request).enqueue(callback);
//            }catch (NetworkOnMainThreadException ex){
//                ex.printStackTrace();
//            }
    }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            Looper.prepare();
            Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
           processData(response);
        }
    };

    private commodityBean processData(Response responce) {//Responce类处理成一个commodityBean类再获取数据，okHTTP3 Responce转换成Json
        commodityBean obj = null;
        ResponseBody body= responce.body();
        java.lang.reflect.Type type = new TypeToken<commodityBean>() {}.getType();

        try{
            assert body != null;
            String json = new String(body.bytes());
            Gson gson = new Gson();
            obj  = gson.fromJson(json,type);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}

