package com.example.secondhandmarket.ui.release;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.appkey.appMobSDK;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReleaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReleaseFragment extends Fragment {
    private Context context;
    private ImageView ivUploadPicture;
    private EditText etInputReleaseContents, etInputReleasePrice;
    private Spinner spCommoTypeName;
    private ArrayAdapter<String> adaptertypeNames;
    private TextView tvReleaseButton;
    private long imageCode;
    private Handler handler1 = new MyHandler(this);

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReleaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReleaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReleaseFragment newInstance(String param1, String param2) {
        ReleaseFragment fragment = new ReleaseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_release, container, false);
        ivUploadPicture = view.findViewById(R.id.upload_picture);
        etInputReleaseContents = view.findViewById(R.id.input_released_commodity_contents);
        etInputReleasePrice = view.findViewById(R.id.input_released_commodity_price);
        spCommoTypeName = view.findViewById(R.id.spinner_released_commodity_typename);
        tvReleaseButton = view.findViewById(R.id.release_button);

        getTypeNameList();
//用来获取typeid 和typeName
        spCommoTypeName.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              //获取id
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //选择，上传图片
        ivUploadPicture.setOnClickListener(v->{
            //选择图片后直接new  uplosdPicture对象
            //构造函数参数是context、handler1
        });

        tvReleaseButton.setOnClickListener(v->{

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
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        assert body !=null;
                        TypeNameListBean typeNameListBean = new TypeNameListBean();
                        typeNameListBean = new Gson().fromJson(new String(body.bytes()),typeNameListBean.getClass());

                        Message msg = Message.obtain();
                        msg.what = 0x01;
                        msg.obj = typeNameListBean.getData();

                        new Handler(Looper.getMainLooper()){
                            @SuppressLint("ResourceType")
                            @Override
                            public void handleMessage(@NonNull Message msg) {
                                super.handleMessage(msg);
                                if(msg.what == 0x01){//设置数据

                                    List<TypeNameListData> l = (List<TypeNameListData>) msg.obj;

                                    List<String> dataTypeNames = new ArrayList<>();
                                    for(int i=0; i<l.size(); i++){

                                        TypeNameListData tbd = l.get(i);
                                        dataTypeNames.add(tbd.getId() + tbd.getType());
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

    private static class MyHandler extends Handler {
        private final WeakReference<ReleaseFragment> mTarget;

        public MyHandler(ReleaseFragment activity) {
            mTarget = new WeakReference<ReleaseFragment>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            ReleaseFragment activity = mTarget.get();
            super.handleMessage(msg);
            if (null != activity) {
                //执行业务逻辑
                if (msg.what == 0) {
                    ReleaseFragment ma = mTarget.get();
                    ma.imageCode = (long) msg.obj;
                }
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