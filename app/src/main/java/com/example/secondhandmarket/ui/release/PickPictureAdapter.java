package com.example.secondhandmarket.ui.release;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;

import java.io.File;
import java.util.List;

public class PickPictureAdapter extends RecyclerView.Adapter<PickPictureAdapter.ViewHolderPick> {
    private Context context;
    private List<File> list;
    @NonNull
    @Override
    public ViewHolderPick onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPick holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolderPick extends RecyclerView.ViewHolder {
        ImageView ivPic, ivDel;
        public ViewHolderPick(@NonNull View itemView) {
            super(itemView);

            ivPic = itemView.findViewById(R.id.ivPic);
            ivDel = itemView.findViewById(R.id.ivDel);
        }
    }
}
