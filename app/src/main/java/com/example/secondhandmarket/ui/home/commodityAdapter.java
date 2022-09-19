package com.example.secondhandmarket.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.commoditybean.GotCommodityBean;
import com.example.secondhandmarket.getURLimage.getURLimage;

import java.util.List;

public class commodityAdapter extends BaseAdapter {
    private final List<GotCommodityBean> list;
    private final Context context;



    public commodityAdapter(List<GotCommodityBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder vh = new viewHolder();
        view = LayoutInflater.from(context).inflate(R.layout.commodity_list_item, null);
        vh.commodityName = view.findViewById(R.id.commodityName);
        vh.commodityPrice = view.findViewById(R.id.commodityPrice);
        vh.commodityPic = view.findViewById(R.id.commodityImg);
        vh.commodityId = view.findViewById(R.id.textViewidd);
        view.setTag(vh);

        GotCommodityBean cb = list.get(i);
        vh.commodityName.setText(cb.getContent() + "  "+cb.getTypeName());
        vh.commodityPrice.setText(Integer.toString(cb.getPrice()));
        vh.commodityId.setText(Long.toString(cb.getId()));

        if(cb.getImageUrlList()!=null){
            new Thread(() -> {
                Bitmap bm = new getURLimage().getimage(cb.getImageUrlList().get(0));
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bm;
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 0) {
//                        System.out.println("111");
                            Bitmap bmp = (Bitmap) msg.obj;
                            vh.commodityPic.setImageBitmap(bmp);
                        }

                    }
                }.sendMessage(msg);
            }).start();
        }

        return view;

    }

    static class viewHolder{
        public ImageView commodityPic;
        public TextView commodityName, commodityPrice, commodityId;
    }
}
