package com.example.secondhandmarket.ui.release;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondhandmarket.R;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReleaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReleaseFragment extends Fragment {
    private ImageView ivUploadPicture;
    private EditText etInputReleaseContents, etInputReleasePrice, etInputTypeName;
    private TextView tvReleaseButton;

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
       etInputTypeName = view.findViewById(R.id.input_released_commodity_typename);
       tvReleaseButton = view.findViewById(R.id.release_button);

/*
            Map<String, Object> bodyMap = new HashMap<>();
            put("price", 0);
            put("imageCode", 0);
            put("typeName", "string");
            put("typeId", 0);
            put("id", 0);
            put("addr", "string");
            put("userId", 0);
            put("content", "string");
 */
       tvReleaseButton.setOnClickListener(v->{
           //点击事件
           String content = etInputReleaseContents.getText().toString();
           String typeName = etInputTypeName.getText().toString();
           int typeId;//跟据typeNmae生成；
           int price = Integer.parseInt(etInputReleasePrice.getText().toString());
           int id = 0;
           String addr;//登陆界面获取
           int ImageCode;
           int userId;//登陆界面获取
           Map<String, Object> map;

       });
        ivUploadPicture.setOnClickListener(v->{

        });
       return view;
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