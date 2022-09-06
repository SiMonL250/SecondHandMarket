package com.example.secondhandmarket.ui.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.IOException;
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
    private EditText etName, etAddr;
    private String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_information);
        ivAvatar = findViewById(R.id.iv_avatar);
        etName = findViewById(R.id.et_name);
        etAddr = findViewById(R.id.et_addr);
    }

    public void ChangeInfoClick(View view) {
        int id = view.getId();
        if(id == R.id.iv_avatar){
            //检查权限
            if(ContextCompat.checkSelfPermission(ChangeMyInformationActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ChangeMyInformationActivity.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },1);
            }
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            Log.d("ChangeInfoClick",MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
            //noinspection deprecation
            startActivityForResult(intent, 0x01);

        }
        if(id == R.id.btn_sumbit){
            String name = etName.getText().toString();
            String addr = etAddr.getText().toString();


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1 && resultCode == RESULT_OK) {
            if (data != null) {
                ivAvatar.setImageURI(data.getData());
                Log.d("onActivityResult: ",data.getData().toString());
                postfile();
            }
        }
    }

    private void postfile(){
        String url = "http://47.107.52.7:88/member/tran/image/upload";

        Headers headers = new Headers.Builder()
                .add("Accept","application/json, text/plain, */*")
                .add("appId", new appMobSDK().appID)
                .add("appSecret", new appMobSDK().appSecret)
                .add("Content-Type", "multipart/form-data")
                .build();

        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(url)
                // 将请求头加至请求中
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))
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
                public void onResponse(Call call, Response response) throws IOException {
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
                    imageUrl = mpostFileBean.getData().getImageUrlList().get(0);
                    Toast.makeText(ChangeMyInformationActivity.this, mpostFileBean.getMsg(), Toast.LENGTH_SHORT).show();

                }
            });
        }catch (NetworkOnMainThreadException ex){
            ex.printStackTrace();
        }
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
            private int imageCode;
            private List<String> imageUrlList;

            public int getImageCode() {
                return imageCode;
            }

            public List<String> getImageUrlList() {
                return imageUrlList;
            }
        }
    }

    private void postchange(String avatar, String userid){

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
                        Toast.makeText(ChangeMyInformationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            private String id;
            private String appKey;
            private String userName;
            private int money;
            private String avatar;

            public String getId() {
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