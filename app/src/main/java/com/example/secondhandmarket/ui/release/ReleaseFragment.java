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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.FileUtils;
import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.UseAPI;
import com.example.secondhandmarket.appSecret.AppSecret;
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
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ReleaseFragment extends Fragment {
    private Context context;


    private EditText etInputReleaseContents, etInputReleasePrice,etInputReleaseadds;
    private Spinner spCommoTypeName;
    private ArrayAdapter<String> adaptertypeNames;
    private TextView tvReleaseButton;

    private String TypeName;
    private int TypeID;
    private String [] TypeNameList=new String[20];
    private int [] TypeIDList=new int[20];
    //上传图片用的handler 获取imagecode


    //paths:用于显示，images用于API参数
    private final ArrayList<File> images = new ArrayList<>();
    private final ArrayList<String> paths = new ArrayList<>();
    private ImageView iv_Pic;
    private TextView tv_account;
    private RecyclerView rv_Pic;
    public ReleaseFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release, container, false);
        etInputReleaseadds=view.findViewById(R.id.input_released_commodity_addr);
        etInputReleaseContents = view.findViewById(R.id.input_released_commodity_contents);
        etInputReleasePrice = view.findViewById(R.id.input_released_commodity_price);
        spCommoTypeName = view.findViewById(R.id.spinner_released_commodity_typename);
        tvReleaseButton = view.findViewById(R.id.release_button);
        iv_Pic = view.findViewById(R.id.release_select_pic);
        tv_account = view.findViewById(R.id.release_account_pic);
        rv_Pic = view.findViewById(R.id.rv_pic);

        {
            new UseAPI().getTypeNameList(callbackTypeName);
            //用来获取typeid 和typeName
            //TODO 完成点击事件获取 typeId TypeName
            spCommoTypeName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //获取id
                    TypeName=TypeNameList[i];
                    TypeID=TypeIDList[i];
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
                        images.add(new File(path));
                        paths.add(path);

                        PickPictureAdapter pickPictureAdapter = new PickPictureAdapter(context,images);
                        rv_Pic.setAdapter(pickPictureAdapter);
                        rv_Pic.setLayoutManager(new LinearLayoutManager(context));
                        System.out.println(images.size());
                        tv_account.setText(Integer.toString(images.size()));

                        pickPictureAdapter.setListener(new PickPictureAdapter.OnItemClickListener() {
                            @Override
                            public void click(View view, int positon) {

                            }

                            @Override
                            public void del(View view) {
                                tv_account.setText(Integer.toString(images.size()));
                            }
                        });
                    }
                });
        tvReleaseButton.setOnClickListener(v->{

            long userId = new GetUserInfor(context).getUSerID();

            String Adds=etInputReleaseadds.getText().toString();
            String Content=etInputReleaseContents.getText().toString();

            if(userId!=-1&&!etInputReleasePrice.getText().toString().equals("")
                    &&!Content.equals("")&&!Adds.equals("") &&images.size()!=0){
                postfile(images);
            }
            else Toast.makeText(context, "格式不正确，请检查", Toast.LENGTH_SHORT).show();



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


    private final Callback callbackTypeName = new Callback() {
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
                                TypeIDList[i]=tbd.getId();
                                TypeNameList[i]=tbd.getType();

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
    };

    //添加商品（添加商品信息）
    private final Gson gson = new Gson();
    private void Addgoods(String Adds,String Content,long Price,long TypeID,String TypeName,long userId,long ImageCode) {
        new Thread(()->{
            // url路径
            String url = "http://47.107.52.7:88/member/tran/goods/add";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("appId", new AppSecret().appID)
                    .add("appSecret", new AppSecret().appSecret)
                    .add("Accept", "application/json, text/plain, */*")
                    .build();


            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();

            bodyMap.put("addr", Adds);
            bodyMap.put("content", Content);
            bodyMap.put("id", "1");
            bodyMap.put("imageCode", ImageCode);
            bodyMap.put("price", Price);
            bodyMap.put("typeId", TypeID);
            bodyMap.put("typeName", TypeName);
            bodyMap.put("userId", userId);
            // 将Map转换为字符串类型加入请求体中
            String body = gson.toJson(bodyMap);

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
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            Looper.prepare();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {

            RelResponce relResponce = new RelResponce();
            ResponseBody body = response.body();
            assert body != null;
            try{
                String json = new String(body.bytes());
                Gson gson = new Gson();
                relResponce = gson.fromJson(json, relResponce.getClass());

            }catch (IOException e){
                e.printStackTrace();
            }
            if(relResponce.getCode()==200){
                Looper.prepare();
                Toast.makeText(context, "上传成功！", Toast.LENGTH_SHORT).show();
                Looper.loop();
//              startActivity(new Intent(context, Success.class));
//                getActivity().finish();
            }
//            Looper.prepare();
//            Toast.makeText(context, relResponce.getMsg(), Toast.LENGTH_SHORT).show();
//            Looper.loop();
        }
    };

    private static class RelResponce{
        private String msg;
        private int code;
        private String data;

        public String getMsg() {
            return msg;
        }

        public int getCode() {
            return code;
        }

        public String getData() {
            return data;
        }

    }


    //上传文件
    private void postfile(ArrayList<File> fList){
        String url = "http://47.107.52.7:88/member/tran/image/upload";

        Headers headers = new Headers.Builder()
                .add("Accept","application/json, text/plain, */*")
                .add("appId", new AppSecret().appID)
                .add("appSecret", new AppSecret().appSecret)
                .add("Content-Type", "multipart/form-data")
                .build();

        MediaType mediaType = MediaType.Companion.parse("text/x-markdown; charset=utf-8");

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <= fList.size() - 1; i++) {
            RequestBody fileBody = RequestBody.Companion.create(fList.get(i), mediaType);
            requestBody.addFormDataPart("fileList", fList.get(i).toString(), fileBody);
        }

        RequestBody body = requestBody.build();

        Request request = new Request.Builder()
                .url(url)
                // 将请求头加至请求中
                .headers(headers)
                .post(body)
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            //发起请求，传入callback进行回调
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

                @Override
                public void onResponse(Call call, Response response) {
                    ResponseBody body = response.body();
                    postFileBean mpostFileBean = new postFileBean();
                    try{
                        assert body != null;
                        mpostFileBean = new Gson().
                                fromJson(new String(body.bytes()), mpostFileBean.getClass());

                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    long userId = new GetUserInfor(context).getUSerID();

                    long imageCode=mpostFileBean.getData().getImageCode();
                    double price=Double.parseDouble(etInputReleasePrice.getText().toString());
                    long Price=(long)price;
                    String Adds=etInputReleaseadds.getText().toString();
                    String Content=etInputReleaseContents.getText().toString();

                    Addgoods(Adds,Content,Price,TypeID,TypeName,userId,imageCode);
                    System.out.println(imageCode);
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
        private postFileBean.data data;

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
