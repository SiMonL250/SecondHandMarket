package com.example.secondhandmarket.ui.notifications;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NotiMessages extends AppCompatActivity {
    private List<MessageList.data.theMessage> list;
    private ListView lvList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti_messages);
        lvList=findViewById(R.id.lv_message_list);

        Intent intent = getIntent();
        long fromId = intent.getLongExtra("id",-1);
        long userId = new GetUserInfor(NotiMessages.this).getUSerID();
        if(fromId != -1){
            new Thread(()->{
                String url = "http://47.107.52.7:88/member/tran/chat/message?fromUserId="
                        +fromId+"&userId="+userId;
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
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseBody body = response.body();
                            assert body!=null;
                            MessageList messageList =
                                    new Gson().fromJson(new String(body.bytes()),MessageList.class);

                            Message msg = Message.obtain();
                            msg.what=0x111111;
                            msg.obj = messageList.getData().getRecords();//list
                            new Handler(Looper.getMainLooper()){
                                @Override
                                public void handleMessage(@NonNull Message msg) {
                                    super.handleMessage(msg);
                                    if(msg.what == 0x111111){
                                        list = (List<MessageList.data.theMessage>) msg.obj;

                                        List<Map<String,String>> l = new ArrayList<>();
                                        for(MessageList.data.theMessage a : list){
                                            Map<String,String> map = new HashMap<>();
                                            map.put("fromName",a.getFromUsername());
                                            map.put("time",Long.toString(a.getCreateTime()));
                                            map.put("message",a.getContent());
                                            l.add(map);
                                        }
                                        String[] from = {"fromName","time","message"};
                                        int[] to = {R.id.tv_msg_name,R.id.tv_msg_time,R.id.tv_msg_content};
                                        SimpleAdapter simpleAdapter = new SimpleAdapter(NotiMessages.this,
                                                l,R.layout.msg_layout_items,from,to);
                                        lvList.setAdapter(simpleAdapter);
                                    }

                                }
                            }.sendMessage(msg);
                        }
                    });
                }catch (NetworkOnMainThreadException ex){
                    ex.printStackTrace();
                }

            }).start();

        }
    }
    /*
    复制
{
msg:"string"
code:0
data:{
current:0
records:[
{
content:"string"
createTime:0
fromUserId:0
fromUsername:"string"
id:0
status:true
userId:0
username:"string"
}
]
size:0
total:0
}
}
     */
    class MessageList{
        private String msg;
        private int code;
        private data data;

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }

        public data getData() {
            return data;
        }

        class data {
            private long current;
            private List<theMessage> records;
            private long size;
            private long total;

            public long getCurrent() {
                return current;
            }

            public List<theMessage> getRecords() {
                return records;
            }

            public long getSize() {
                return size;
            }

            public long getTotal() {
                return total;
            }

            private class theMessage {
                private String content;
                private long createTime;
                private long fromUserId;
                private String fromUsername;
                private long id;
                private boolean status;
                private long userId;
                private String username;

                public String getContent() {
                    return content;
                }

                public long getCreateTime() {
                    return createTime;
                }

                public long getFromUserId() {
                    return fromUserId;
                }

                public String getFromUsername() {
                    return fromUsername;
                }

                public long getId() {
                    return id;
                }

                public boolean isStatus() {
                    return status;
                }

                public long getUserId() {
                    return userId;
                }

                public String getUsername() {
                    return username;
                }
            }
        }
    }
}