package com.example.secondhandmarket.ui.notifications;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondhandmarket.R;
import com.example.secondhandmarket.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NotiAdapter mNotiAdapter;
    List<myNotifi> myNotifiList = new ArrayList<>();
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        //API在这里获取数据，把for删除
//        利用GetUserInfor 类获取用户Id
        for(int i=0; i<50; i++){
            myNotifi notice = new myNotifi();
            notice.notiName = "名字"+i;
            notice.notiText = "内容"+i;
            myNotifiList.add(notice);
        }
        mRecyclerView = view.findViewById(R.id.notification_list);
        mNotiAdapter = new NotiAdapter();
        mRecyclerView.setAdapter(mNotiAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
//根据API返回的数据改写notification_items_layout文件
    //修改viewHolder  和Adapter
    private class NotiAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = View.inflate(getContext(),R.layout.notification_items_layout,null);
            MyViewHolder mViewHolder = new MyViewHolder(v);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            myNotifi mNotification = myNotifiList.get(position);
            holder.tvName.setText(mNotification.notiName);
            holder.tvNotifiText.setText(mNotification.notiText);
        }

        @Override
        public int getItemCount() {
            return myNotifiList.size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvNotifiText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvnotiname);
            tvNotifiText = itemView.findViewById(R.id.tvnotifi);
        }

    }
}