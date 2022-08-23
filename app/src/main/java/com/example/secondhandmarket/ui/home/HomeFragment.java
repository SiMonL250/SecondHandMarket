package com.example.secondhandmarket.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    private  ListView goodsList;
    private FragmentHomeBinding binding;
    private SimpleAdapter mAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        goodsList = view.findViewById(R.id.commodityList);
        mAdapter = new SimpleAdapter(getActivity(),getdata(),R.layout.commodity_list_item,new String[]{"title","p"},new int[]{R.id.commodityName,R.id.commodityPrice});
        goodsList.setAdapter(mAdapter);
        goodsList.setOnItemClickListener(this);
        return view;
    }
//将List改一改，将Commodity类放进去，直接用SimpleAdapter；
    private List<Map<String,String>> getdata() {
        String [] titles={"水果1","水果2","水果3","水果4","水果5","水果6","水果7"};
        List<Map<String,String>> list= new ArrayList<>();
        for(int i=0;i<7;i++){
            Map map = new HashMap();
            map.put("title",titles[i]);
            map.put("p",titles[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("click");
    }

}