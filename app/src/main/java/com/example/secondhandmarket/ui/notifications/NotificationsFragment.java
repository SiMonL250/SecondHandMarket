package com.example.secondhandmarket.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.example.secondhandmarket.databinding.FragmentNotificationsBinding;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NotificationsFragment extends Fragment {
    private Context mcontext;
    private RecyclerView mRecyclerView;
    private NotiAdapter mNotiAdapter;
    private LinearLayout lfa;
    List<myNotifi.data> myNotifiList = new ArrayList<>();//TODO 根据API返回参数修改myNotifi类
    private FragmentNotificationsBinding binding;
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0xeeee){
                //初始化myNotifiList；
                //set adapter
                myNotifiList = (List<myNotifi.data>) msg.obj;
                mNotiAdapter = new NotiAdapter(mcontext,myNotifiList);
                mRecyclerView.setAdapter(mNotiAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mNotiAdapter.setItemClickListener(position -> {
                    Intent intent = new Intent(mcontext,NotiMessages.class);
                    intent.putExtra("id",myNotifiList.get(position).getFromUserId());
                    startActivity(intent);
                });
                if(mNotiAdapter.getItemCount() == 0){
                    TextView noMsg = new TextView(mcontext);
                    noMsg.setText("无消息");
                    noMsg.setTextSize(48);
                    noMsg.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    noMsg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.
                            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    noMsg.setLayoutParams(layoutParams);

                    lfa.addView(noMsg);
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mcontext = getContext();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        //API在这里获取数据，把for删除
//        利用GetUserInfor 类获取用户Id

        mRecyclerView = view.findViewById(R.id.notification_list);
        lfa=view.findViewById(R.id.lfa);
        long userId = new GetUserInfor(mcontext).getUSerID();
        if(userId != -1){
            getnotification(userId);
        }


        return view;
    }

    private void getnotification(long userId) {
        new Thread(()->{
            String url = "http://47.107.52.7:88/member/tran/chat/user?userId="+userId;
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
                    .add("Content-Type", "application/json")
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
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Looper.prepare();
                        Toast.makeText(mcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        ResponseBody body = response.body();
                        assert body!=null;
                        myNotifi myNotifibean = new myNotifi();
                        myNotifibean = new Gson().fromJson(new String(body.bytes()),myNotifibean.getClass());
                        System.out.println(myNotifibean.getData().size()+myNotifibean.getMsg());
                        Message message = Message.obtain();
                        message.what = 0xeeee;
                        message.obj = myNotifibean.getData();//应为list
                        //handler
                        handler.sendMessage(message);
                    }
                });
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//根据API返回的数据改写notification_items_layout文件
    //修改viewHolder  和Adapter

}