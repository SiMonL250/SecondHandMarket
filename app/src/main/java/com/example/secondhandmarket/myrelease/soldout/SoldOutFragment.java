package com.example.secondhandmarket.myrelease.soldout;

import static com.example.secondhandmarket.databinding.FragmentNewReleaseSoldoutBinding.inflate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.myrelease.MyReleaseAdapter;
import com.example.secondhandmarket.myrelease.Requestget;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoldOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoldOutFragment extends Fragment {
    private RecyclerView mRecyclerViewList;
    private MyReleaseAdapter myReleaseAdapter;
    private TextView tvNone;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public SoldOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SoldOutFragment.
     */
    public static SoldOutFragment newInstance(String param1, String param2) {
        SoldOutFragment fragment = new SoldOutFragment();
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
        ViewBinding binding = inflate(inflater,container,false);
        View view= binding.getRoot();
        mRecyclerViewList = view.findViewById(R.id.recyeView);
        tvNone = view.findViewById(R.id.none);
        Requestget rg = new Requestget();
        rg.get(rg.getUrlsouldout(), 14, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                ResponseBodyBean responseBodyBean = new ResponseBodyBean();
                assert body != null;
                responseBodyBean = new Gson().fromJson(new String(body.bytes()),responseBodyBean.getClass());
                System.out.println(responseBodyBean.getCode());


                if(responseBodyBean.getCode() == 200){
                    Message msg = Message.obtain();
                    msg.what = 0x09;
                    msg.obj = responseBodyBean.getData().getRecords();//List

                    new Handler(Looper.getMainLooper()) {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            if (msg.what == 0x09) {
                                if(msg.obj !=null){
                                    List<GotCommodityBean> list = (List<GotCommodityBean>) msg.obj;
                                    myReleaseAdapter = new MyReleaseAdapter(list);
                                    mRecyclerViewList.setAdapter(myReleaseAdapter);

                                    if(myReleaseAdapter.getItemCount()!= 0)
                                        tvNone.setVisibility(View.GONE);

                                }
                            }
                        }
                    }.sendMessage(msg);
                }

            }
        });
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

}