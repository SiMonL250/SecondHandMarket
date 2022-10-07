package com.example.secondhandmarket.ui.account;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ChangeMyInformationActivity extends AppCompatActivity {
    private ImageView ivAvatar;
    private String imageUrl;
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_information);
        ivAvatar = findViewById(R.id.iv_avatar);
        btn = findViewById(R.id.btn_sumbit);
        btn.setOnClickListener(v->{
            if(imageUrl != null){
                long userId = new GetUserInfor(ChangeMyInformationActivity.this).getUSerID();
                if(userId != -1){
                    postchange(imageUrl, userId);
                }

            }

        });
    }

    public void ChangeInfoClick(View view) {
        int id = view.getId();
        if(id == R.id.iv_avatar){
            //检查权限
            if(ContextCompat.checkSelfPermission(ChangeMyInformationActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ChangeMyInformationActivity.this,new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },1);
            }
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            Log.d("ChangeInfoClick",MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
            //noinspection deprecation
            startActivityForResult(intent, 0x01);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x01 && resultCode == RESULT_OK) {
            if (data != null) {
                ivAvatar.setImageURI(data.getData());
                Uri uri = data.getData();
//TODO try to post file
                List<File> list = new ArrayList<>();
                File f = new File(uri.toString());
                postfile(list);
            }
        }
    }

    private void postfile(List<File> fList){
        String url = "http://47.107.52.7:88/member/tran/image/upload";

        Headers headers = new Headers.Builder()
                .add("Accept","application/json, text/plain, */*")
                .add("appId", new appMobSDK().appID)
                .add("appSecret", new appMobSDK().appSecret)
                .add("Content-Type", "multipart/form-data")
                .build();

        Map<String,Object> map = new HashMap<>();

        map.put("fileList",fList);
        String body = map.toString();

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(url)
                // 将请求头加至请求中
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            //发起请求，传入callback进行回调
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(ChangeMyInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    ResponseBody body = response.body();
                    postFileBean mpostFileBean = new postFileBean();
                    try{
                        assert body != null;
                        mpostFileBean = new Gson().
                                fromJson(new String(body.bytes()), mpostFileBean.getClass());
                        //传递图片URL，给修改信息的API

                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    System.out.println(mpostFileBean.getCode()+mpostFileBean.getMsg());
                    //imageUrl = mpostFileBean.getData().getImageUrlList().get(0);
                    if(mpostFileBean.getCode() == 200){
                        Message msg = Message.obtain();
                        msg.what = 0xc1;
                        msg.obj = mpostFileBean.getData().getImageUrlList().get(0);

                        new Handler(Looper.getMainLooper()){
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 0xc1){
                                    imageUrl = (String) msg.obj;
                                    System.out.println(imageUrl);
                                }
                            }
                        }.sendMessage(msg);

                    }
                }
            });
        }catch (NetworkOnMainThreadException ex){
            ex.printStackTrace();
        }
    }
    public static String getRealPathFromURI(Uri contentUri, Context mContext) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(mContext, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    /*
    复制
{
msg:"string"
code:0
data:{
imageCode:0
imageUrlList:[
{}
]
}
}*/
    static class postFileBean{
        private String msg;
        private int code;
        private data data;

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }

        public postFileBean.data getData() {
            return data;
        }

        class data{
            private long imageCode;
            private List<String> imageUrlList;

            public long getImageCode() {
                return imageCode;
            }

            public List<String> getImageUrlList() {
                return imageUrlList;
            }
        }
    }

    private void postchange(String avatar, long userid){

            // url路径
            String url = "http://47.107.52.7:88/member/tran/user/update";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", "6e7ad529141b4ec18c355eff7abfd160")
                    .add("appSecret", "63421994d54e2abe54902b678072a31a94e66")
                    .add("Accept", "application/json, text/plain, */*")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("avatar", avatar);
            bodyMap.put("userId", userid);
            // 将Map转换为字符串类型加入请求体中
            String body = new Gson().toJson(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(ChangeMyInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        ChangeRsponseBodyBean changresponsebodybean = new ChangeRsponseBodyBean();
                        assert body != null;
                        changresponsebodybean = new Gson().fromJson(new String(body.bytes()),changresponsebodybean.getClass());
                    }
                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
    }
    class ChangeRsponseBodyBean {
        private int code;
        private String msg;
        private data data;

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public ChangeRsponseBodyBean.data getData() {
            return data;
        }

        class data {
            private long id;
            private String appKey;
            private String userName;
            private int money;
            private String avatar;

            public long getId() {
                return id;
            }

            public String getAppKey() {
                return appKey;
            }

            public String getUserName() {
                return userName;
            }

            public int getMoney() {
                return money;
            }

            public String getAvatar() {
                return avatar;
            }
        }
    }

}