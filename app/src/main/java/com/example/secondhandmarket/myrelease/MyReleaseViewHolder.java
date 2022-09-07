package com.example.secondhandmarket.myrelease;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;

public class MyReleaseViewHolder extends RecyclerView.ViewHolder {
    TextView myReleaseName, myReleasePrice, myReleaseID;
    ImageView myReleaseImage, myReleaseDelete;
    public MyReleaseViewHolder(@NonNull View itemView) {
        super(itemView);
        myReleaseName = itemView.findViewById(R.id.tv_my_nam);
        myReleasePrice = itemView.findViewById(R.id.tv_my_pri);
        myReleaseImage = itemView.findViewById(R.id.iv_my_pic);
        myReleaseDelete = itemView.findViewById(R.id.iv_my_del);
        myReleaseID = itemView.findViewById(R.id.tv_my_id);
    }
}
