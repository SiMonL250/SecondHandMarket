package com.example.secondhandmarket.myrelease;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.secondhandmarket.GetUserInfor;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.ui.home.commodityResponseBody.GotCommodityBean;
import com.example.secondhandmarket.ui.release.PickPictureAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyReleaseAdapter extends RecyclerView.Adapter<MyReleaseViewHolder> {
    private List<GotCommodityBean> mDataList;
    private Context context;
    public MyReleaseAdapter(List<GotCommodityBean> list, Context context){
        this.mDataList = list;
        this.context = context;
    }
    public interface OnItemClickListener1{
        void click(View view, int positon);
        void del(View view);

    }

    public void setListener(OnItemClickListener1 listener) {
        this.listener = listener;
    }

    OnItemClickListener1 listener;
    @NonNull
    @Override
    public MyReleaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View item = View.inflate(parent.getContext(), R.layout.mynewreleaseorsoldoutlayout,null);

        return new MyReleaseViewHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyReleaseViewHolder holder, int position) {
        GotCommodityBean gc = mDataList.get(position);
        holder.myReleaseName.setText(gc.getContent() + "  "+gc.getTypeName());
        holder.myReleasePrice.setText(Integer.toString(gc.getPrice()));
        holder.myReleaseID.setText(Long.toString(gc.getId()));//主键id

        if(gc.getImageUrlList()!=null){
            Glide.with(context)
                    .load(gc.getImageUrlList().get(0))
                    .into(holder.myReleaseImage);
        }

        holder.myReleaseDelete.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                mDataList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
//           System.out.println(goodid);
                long id = new GetUserInfor(context).getUSerID();
                long goodid = Long.parseLong(holder.myReleaseID.getText().toString());
                new RequestDelete().delete(goodid,id,new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Looper.prepare();
                        Toast.makeText(context, new String(response.body().bytes()), Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        //刷新Fragment

                    }
                });
                if (listener!=null){
                    listener.del(view);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
