package com.example.secondhandmarket.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondhandmarket.R;

public class commodityAdapter extends CursorAdapter {
    private Context mContex;
    private LayoutInflater mLayoutInflater;
    public commodityAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);

        mContex = context;
        mLayoutInflater = LayoutInflater.from(mContex);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View itemView = mLayoutInflater.inflate(R.layout.commodity_list_item,viewGroup,false);
        if(itemView != null){
            commodityViewHolder vh = new commodityViewHolder();
            vh.tvName = itemView.findViewById(R.id.commodityName);
            vh.tvPrice = itemView.findViewById(R.id.commodityPrice);
            vh.ivPicture = itemView.findViewById(R.id.commodityImg);
            vh.addToCart = itemView.findViewById(R.id.addToCart);
            itemView.setTag(vh);
            return itemView;
        }
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }

    private class commodityViewHolder {
        TextView tvName;
        TextView tvPrice;
        ImageView ivPicture;
        ImageView addToCart;
    }
}
