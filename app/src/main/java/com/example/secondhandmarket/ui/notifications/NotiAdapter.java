package com.example.secondhandmarket.ui.notifications;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.MyViewHolder>{
    private Context context;
    private List<myNotifi.data> list;
    private OnItemClickListener itemClickListener;


    public NotiAdapter(Context context, List<myNotifi.data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.notification_items_layout,null);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        myNotifi.data mNotification = list.get(position);
        holder.tvName.setText(mNotification.getUsername());//TODO 修改这里
        holder.tvAccount.setText(Integer.toString(mNotification.getUnReadNum()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClickListener!=null)
                    itemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvAccount;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvnotiname);
            tvAccount = itemView.findViewById(R.id.tvnoRead);
        }
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    //设置点击事件的方法
    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}
