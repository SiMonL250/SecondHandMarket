package com.example.secondhandmarket.ui.release;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;

import java.io.File;
import java.util.List;

public class PickPictureAdapter extends RecyclerView.Adapter<PickPictureAdapter.ViewHolderPick> {
    private Context context;
    private List<File> list;

    public interface OnItemClickListener{
        void click(View view, int positon);
        void del(View view);

    }

    OnItemClickListener listener;
    public PickPictureAdapter(Context context, List<File> list) {
        this.context = context;
        this.list = list;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderPick onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.load_pic_items,null);
        return new ViewHolderPick(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPick holder, int position) {
        String path = list.get(position).getPath();
        TextView tv = holder.filePath;
        tv.setText(path);
        tv.setSingleLine(true);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setFocusable(true);
        tv.setFocusableInTouchMode(true);

        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                list.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                if(listener!=null){
                    listener.del(view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolderPick extends RecyclerView.ViewHolder {
        TextView filePath;
        ImageView ivDel;
        public ViewHolderPick(@NonNull View itemView) {
            super(itemView);
            filePath = itemView.findViewById(R.id.tv_path);
            ivDel =itemView.findViewById(R.id.iv_del);
        }
    }
}
