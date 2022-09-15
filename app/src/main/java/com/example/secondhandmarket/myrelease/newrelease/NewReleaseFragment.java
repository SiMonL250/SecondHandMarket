package com.example.secondhandmarket.myrelease.newrelease;

import static com.example.secondhandmarket.databinding.FragmentNewReleaseSoldoutBinding.inflate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.myrelease.MyReleaseAdapter;
import com.example.secondhandmarket.myrelease.RequestDelete;
import com.example.secondhandmarket.myrelease.Requestget;
import com.example.secondhandmarket.myrelease.callbackForMyGoods;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewReleaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewReleaseFragment extends Fragment {
    private RecyclerView mRecyclerViewList;
    private MyReleaseAdapter myReleaseAdapter;
    private ResponseBodyBean responseBodyBean;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewReleaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewReleaseFragment.
     */
    public static NewReleaseFragment newInstance(String param1, String param2) {
        NewReleaseFragment fragment = new NewReleaseFragment();
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewBinding binding = inflate(inflater,container,false);
        View view = binding.getRoot();
        mRecyclerViewList = view.findViewById(R.id.recyeView);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("mysp", Context.MODE_PRIVATE);

        //get id  editor.putInt("userId",responseBodylogin.getData().getId());
        int userId = sharedPreferences.getInt("userId",12);

        List<Map<String, String>> list = initData(userId);

        myReleaseAdapter = new MyReleaseAdapter(list);
        mRecyclerViewList.setAdapter(myReleaseAdapter);

        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }
    //String url2, int currentint, int sizeint, String userid, Callback callback
    //userid 从sharedPreference 里拿
    private List<Map<String,String>> initData(int id){
        //TODO 用SharedPreference 获取ID
        callbackForMyGoods callback = new callbackForMyGoods();
        new Requestget().get(new Requestget().getUrlmyRelease(),1,100, id
                ,callback.callback(1,getContext()));
        return callback.getL()==null?null:callback.getL();
    }
}