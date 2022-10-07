package com.example.secondhandmarket.ui.release;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.R2;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
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

    //选择图片的组件
    List<LoadFileVo> fileList = new ArrayList<>();
    LoadFileAdapter adapter = null;

    private RecyclerView rvPic;

    @SuppressLint("NonConstantResourceId")
    @BindView(R2.id.tvNum)
    TextView tvNum;


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

        etInputReleaseContents = view.findViewById(R.id.input_released_commodity_contents);
        etInputReleasePrice = view.findViewById(R.id.input_released_commodity_price);
        spCommoTypeName = view.findViewById(R.id.spinner_released_commodity_typename);
        tvReleaseButton = view.findViewById(R.id.release_button);

        rvPic = view.findViewById(R.id.rvPic);
        initAdapter();

        getTypeNameList();
//用来获取typeid 和typeName
        //TODO 完成点击事件获取 typeId TypeName
        spCommoTypeName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              //获取id
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        tvReleaseButton.setOnClickListener(v->{

        });

       return view;
    }

    private void initAdapter(){
        fileList.add(new LoadFileVo());
        adapter = new LoadFileAdapter(context, fileList,8);
        rvPic.setAdapter(adapter);
        rvPic.setLayoutManager(new GridLayoutManager(context, 3));//3 coloum
        adapter.setListener(new LoadFileAdapter.OnItemClickListener() {
            @Override
            public void click(View view, int positon) {
                if(fileList.size()>8){
                    Toast.makeText(context, "一次最多上传8张图片", Toast.LENGTH_SHORT).show();
                }else {
                    selectPic();
                }
            }

            @Override
            public void del(View view) {
                tvNum.setText((fileList.size()-1)+"/8");
            }
        });
    }

    private void selectPic() {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
        }

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

                                    List<TypeNameListData> l = (List<TypeNameListData>) msg.obj;

                                    List<String> dataTypeNames = new ArrayList<>();
                                    for(int i=0; i<l.size(); i++){

                                        TypeNameListData tbd = l.get(i);
                                        //处理这个字符串就可以直接拿到typeid和typename
                                        dataTypeNames.add(tbd.getId() +"-"+ tbd.getType());
//                                       System.out.println(tbd.getId() + tbd.getType());
                                    }
                                    adaptertypeNames = new ArrayAdapter<>(context,
                                            R.layout.spinerlayout, R.id.text, dataTypeNames);
//                                    adaptertypeNames.setDropDownViewResource(R.layout.);
                                    spCommoTypeName.setAdapter(adaptertypeNames);
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