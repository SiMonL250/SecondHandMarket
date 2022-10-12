package com.example.secondhandmarket.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
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
        String name = cb.getContent() + "  "+cb.getTypeName();
        vh.commodityName.setText(name);
//        if(name.length()>10){
//            vh.commodityName.setFocusable(true);
//            vh.commodityName.setFocusableInTouchMode(true);
//            vh.commodityName.setSelected(true);
//        }

        vh.commodityPrice.setText(Integer.toString(cb.getPrice()));
        vh.commodityId.setText(Long.toString(cb.getId()));
       if(cb.getImageUrlList().size() !=0){

           Glide.with(context)
                   .load(cb.getImageUrlList().get(0))
                   .into(vh.commodityPic);
           //预防BitMap 过大
//           new Thread(() -> {
////               Bitmap bm = new getURLimage(context).getimage(cb.getImageUrlList().get(0));
//               Drawable drawable = new getURLimage(context).getDrawableImage(cb.getImageUrlList().get(0));
//               if(drawable !=null){
//                   Message msg = Message.obtain();
//                   msg.what = 0xffff;
//                   msg.obj = drawable;
//                   new Handler(Looper.getMainLooper()) {
//                       @Override
//                       public void handleMessage(@NonNull Message msg) {
//                           super.handleMessage(msg);
//                           if (msg.what == 0xffff) {
////                       System.out.println("111");
////                               Bitmap bmp = (Bitmap) msg.obj;
////                               vh.commodityPic.setImageBitmap(bmp);
//                               vh.commodityPic.setImageDrawable((Drawable) msg.obj);
//                           }
//                       }
//                   }.sendMessage(msg);
//               }
//
//           }).start();
       }



        return view;

    }

    static class viewHolder{
        public ImageView commodityPic;
        public TextView commodityName, commodityPrice, commodityId;
    }
}
