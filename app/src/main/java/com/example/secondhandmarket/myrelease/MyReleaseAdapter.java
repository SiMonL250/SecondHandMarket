package com.example.secondhandmarket.myrelease;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.secondhandmarket.R;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.getURLimage.getURLimage;
import com.example.secondhandmarket.myrelease.newrelease.NewReleaseFragment;
import com.example.secondhandmarket.singleGood.CommodityInformationActivity;

import java.util.List;
import java.util.Map;

public class MyReleaseAdapter extends RecyclerView.Adapter<MyReleaseViewHolder> {
    private List<GotCommodityBean> mDataList;//TODO 修改List

    public MyReleaseAdapter(List<GotCommodityBean> list){
        this.mDataList = list;
    }
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
            String pic = gc.getImageUrlList().get(0);
            new Thread(() -> {
                Bitmap bm = new getURLimage().getimage(pic);
                Message msg = new Message();
                msg.what = 0x18;
                msg.obj = bm;
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 0x18) {
//                        System.out.println("111");
                            Bitmap bmp = (Bitmap) msg.obj;
                            holder.myReleaseImage.setImageBitmap(bmp);
                        }
                    }
                }.sendMessage(msg);
            }).start();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
