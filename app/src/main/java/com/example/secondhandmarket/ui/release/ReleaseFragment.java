package com.example.secondhandmarket.ui.release;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.FileUtils;
import com.example.secondhandmarket.MainActivity;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.transform.Result;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ReleaseFragment extends Fragment {
    private Context context;


    private EditText etInputReleaseContents, etInputReleasePrice;
    private Spinner spCommoTypeName;
    private ArrayAdapter<String> adaptertypeNames;
    private TextView tvReleaseButton;
    private long imageCode;

    //上传图片用的handler 获取imagecode
    private  Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            super.handleMessage(msg);

            if (msg.what == 0xfa) {
                imageCode = (long) msg.obj;
            }

        }
    };
    public ReleaseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }
    private final ArrayList<String> imagesPath = new ArrayList<>();
    private ImageView iv_Pic;
    private TextView tv_account;
    private RecyclerView rv_Pic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release, container, false);

        etInputReleaseContents = view.findViewById(R.id.input_released_commodity_contents);
        etInputReleasePrice = view.findViewById(R.id.input_released_commodity_price);
        spCommoTypeName = view.findViewById(R.id.spinner_released_commodity_typename);
        tvReleaseButton = view.findViewById(R.id.release_button);
        iv_Pic = view.findViewById(R.id.release_select_pic);
        tv_account = view.findViewById(R.id.release_account_pic);
        rv_Pic = view.findViewById(R.id.rv_pic);

        {
            getTypeNameList();
//用来获取typeid 和typeName
            //TODO 完成点击事件获取 typeId TypeName
            spCommoTypeName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //获取id

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        ActivityResultLauncher launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Uri uri = null;
                        if (result.getData() != null) {
                            uri = result.getData().getData(); //result.getData()返回一个Intent类
//                            Log.d("ACTresult", uri.toString());
                        }
                        String path = new FileUtils().getRealPathFromUri(context,uri);
//                        System.out.println(path);
                        //把path加入list
                        imagesPath.add(path);

                        PickPictureAdapter pickPictureAdapter = new PickPictureAdapter(context,imagesPath);
                        rv_Pic.setAdapter(pickPictureAdapter);
                        rv_Pic.setLayoutManager(new LinearLayoutManager(context));
                        System.out.println(imagesPath.size());
                        tv_account.setText(Integer.toString(imagesPath.size()));

                        pickPictureAdapter.setListener(new PickPictureAdapter.OnItemClickListener() {
                            @Override
                            public void click(View view, int positon) {
                            }
                            @Override
                            public void del(View view) {
                                tv_account.setText(Integer.toString(imagesPath.size()));
                            }
                        });
                    }
                });

        tvReleaseButton.setOnClickListener(v->{
            System.out.println(imagesPath);
        });
        iv_Pic.setOnClickListener(v->{
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },1);
            }
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            launcher.launch(intent);
        });


       return view;
    }

    private void getTypeNameList() {
            String url = "http://47.107.52.7:88/member/tran/goods/type";
            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", new appMobSDK().appID)
                    .add("appSecret", new appMobSDK().appSecret)
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
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        ResponseBody body = response.body();
                        assert body !=null;
                        TypeNameListBean typeNameListBean = new TypeNameListBean();
                        typeNameListBean = new Gson().fromJson(new String(body.bytes()),typeNameListBean.getClass());

                        Message msg = Message.obtain();
                        msg.what = 0xfb;
                        msg.obj = typeNameListBean.getData();

                        new Handler(Looper.getMainLooper()){
                            @SuppressLint("ResourceType")
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 0xfb){//设置数据
                                    try{
                                        List<TypeNameListData> l = (List<TypeNameListData>) msg.obj;

                                        List<String> dataTypeNames = new ArrayList<>();
                                        for(int i=0; i<l.size(); i++){

                                            TypeNameListData tbd = l.get(i);
                                            //处理这个字符串就可以直接拿到typeid和typename
                                            dataTypeNames.add(tbd.getId() +"-"+ tbd.getType());
//                                       System.out.println(tbd.getId() + tbd.getType());

                                        }
//                                        if(dataTypeNames.size() ==0){
//
//                                            dataTypeNames = Arrays.asList("1-手机");
//                                        }
                                        adaptertypeNames = new ArrayAdapter<>(context,
                                                R.layout.spinerlayout, R.id.text, dataTypeNames);
//                                    adaptertypeNames.setDropDownViewResource(R.layout.);
                                        spCommoTypeName.setAdapter(adaptertypeNames);
                                    }catch(NullPointerException e){
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }.sendMessage(msg);
                    }
                });
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }

    }


}


/*
{
msg:"string"
code:0
data:{
addr:"string"
appKey:"string"
avatar:"string"
content:"string"
createTime:0
id:0
imageCode:0
imageUrlList:[
{}
]
price:0
status:0
tUserId:0
tuserId:0
typeId:0
typeName:"string"
username:"string"
}
}
 */