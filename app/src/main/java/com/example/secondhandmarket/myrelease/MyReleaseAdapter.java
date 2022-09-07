package com.example.secondhandmarket.myrelease;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;

import java.util.List;
import java.util.Map;

public class MyReleaseAdapter extends RecyclerView.Adapter<MyReleaseViewHolder> {
    private List<Map<String,String>> mDataList;//TODO 修改List

    public MyReleaseAdapter(List<Map<String,String>> list){
        this.mDataList = list;
    }
    @NonNull
    @Override
    public MyReleaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View item = View.inflate(parent.getContext(), R.layout.mynewreleaseorsoldoutlayout,null);
        return new MyReleaseViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReleaseViewHolder holder, int position) {
        Map<String,String> ms = mDataList.get(position);
        holder.myReleaseName.setText(ms.get("commodityName"));
        holder.myReleasePrice.setText(ms.get("commodityPrice"));
        holder.myReleaseID.setText(ms.get("commodityId"));//主键id
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
