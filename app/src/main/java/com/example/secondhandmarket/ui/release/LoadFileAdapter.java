package com.example.secondhandmarket.ui.release;

import static com.example.secondhandmarket.R.layout.load_pic_items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.R2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoadFileAdapter extends RecyclerView.Adapter<LoadFileAdapter.MyViewHolder> {
    Context context;
    List<LoadFileVo> fileVoList;
    View view;
    int PicMaxCount;

    public LoadFileAdapter(Context context, List<LoadFileVo> fileVoList) {
        this.context = context;
        this.fileVoList = fileVoList;
    }

    public LoadFileAdapter(Context context, List<LoadFileVo> fileVoList, int picMaxCount) {
        this.context = context;
        this.fileVoList = fileVoList;
        PicMaxCount = picMaxCount;
    }
    public interface OnItemClickListener {
          void click(View view, int positon);

          void del(View view);
    }
    OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LoadFileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.load_pic_items,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
               //通过默认设置第一个为空文件为添加退保，且在文件个数小于最大限制值的情况。当图片个数等于最大限制值，第一个则不是添加按钮
        if (position == 0 && fileVoList.get(position).getBitmap()==null) {
            holder.ivPic.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);

            holder.ivPic.setOnClickListener(view -> listener.click(view, position));
            holder.ivDel.setVisibility(View.INVISIBLE);

        } else {
         //            Uri uri = Uri.parse(fileVoList.get(position).getFile().getPath());
         //            holder.ivPic.setImageURI(uri);
            holder.ivPic.setImageBitmap(fileVoList.get(position).getBitmap());

            holder.ivDel.setVisibility(View.VISIBLE);
        }

        holder.ivDel.setOnClickListener(view -> {
                         //判断图片是否上传，上传后将无法删除
            if (fileVoList.get(position).isUpload()) {
                Toast.makeText(context, "该图片已上传！", Toast.LENGTH_SHORT).show();
            } else {
                fileVoList.remove(position);
                if (fileVoList.size()==PicMaxCount-1&&fileVoList.get(0).getBitmap()!=null){
                    fileVoList.add(0,new LoadFileVo());
                }//如果数量达到最大数时，前面的加号去掉，然后再减去时，则添加前面的加号
                notifyDataSetChanged();
                if (listener!=null){
                    listener.del(view);//传递接口，计算图片个数显示在界面中
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.ivPic)
        ImageView ivPic;

        @BindView(R2.id.ivDel)
        ImageView ivDel;

        View view;
        MyViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this,view);

        }
    }
}
