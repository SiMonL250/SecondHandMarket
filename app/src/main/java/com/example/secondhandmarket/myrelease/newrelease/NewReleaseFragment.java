package com.example.secondhandmarket.myrelease.newrelease;

import static com.example.secondhandmarket.databinding.FragmentNewReleaseSoldoutBinding.inflate;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.commoditybean.ResponseBodyBean;
import com.example.secondhandmarket.myrelease.MyReleaseAdapter;
import com.example.secondhandmarket.myrelease.Requestget;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewReleaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewReleaseFragment extends Fragment {
    private RecyclerView mRecyclerViewList;
    private MyReleaseAdapter myReleaseAdapter;
    private ResponseBodyBean responseBodyBean;
    List<String> mDataList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewBinding binding = inflate(inflater,container,false);
        View view = binding.getRoot();
        mRecyclerViewList = view.findViewById(R.id.recyeView);
        for(int i=0; i<10; i++){
            mDataList.add("test" +1);
        }
        myReleaseAdapter = new MyReleaseAdapter(mDataList);
        mRecyclerViewList.setAdapter(myReleaseAdapter);

        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    private void initdata(){
        //String url2, int currentint, int sizeint, String userid, Callback callback
        new Requestget().get(new Requestget().getUrlmyRelease(),0,100,12,callback);
    }
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            Looper.prepare();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Looper.prepare();
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            ResponseBody body = response.body();
            responseBodyBean = new ResponseBodyBean();
            try {
                assert body != null;
                responseBodyBean = new Gson().fromJson(new String(body.bytes()), responseBodyBean.getClass());
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    };
}