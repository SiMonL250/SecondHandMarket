package com.example.secondhandmarket.ui.notifications;

import android.app.Notification;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NotificationsFragment extends Fragment {
    private Context mcontext;
    private RecyclerView mRecyclerView;
    private NotiAdapter mNotiAdapter;
    List<myNotifi> myNotifiList = new ArrayList<>();//TODO 根据API返回参数修改myNotifi类
    private FragmentNotificationsBinding binding;
    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0xeeee){
                //初始化myNotifiList；
                //set adapter
                myNotifiList = (List<myNotifi>) msg.obj;
                mNotiAdapter = new NotiAdapter();
                mRecyclerView.setAdapter(mNotiAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        long userId = new GetUserInfor(mcontext).getUSerID();
        if(userId != -1){
            getnotification(userId);
        }



        return view;
    }

    private void getnotification(long userId) {
        new Thread(()->{
            String url = "";
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

                        Message message = Message.obtain();
                        message.what = 0xeeee;
                        message.obj = myNotifibean;//应为list
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
    private class NotiAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = View.inflate(getContext(),R.layout.notification_items_layout,null);
            MyViewHolder mViewHolder = new MyViewHolder(v);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            myNotifi mNotification = myNotifiList.get(position);
            holder.tvName.setText(mNotification.notiName);//TODO 修改这里
            holder.tvNotifiText.setText(mNotification.notiText);
        }

        @Override
        public int getItemCount() {
            return myNotifiList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNotifiText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvnotiname);
            tvNotifiText = itemView.findViewById(R.id.tvnotifi);
        }

    }
}