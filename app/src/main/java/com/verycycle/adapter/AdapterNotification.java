package com.verycycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.verycycle.R;
import com.verycycle.databinding.ItemCompleteBinding;
import com.verycycle.databinding.ItemNotificationBinding;
import com.verycycle.model.HistoryModel;
import com.verycycle.model.NotificationModel;

import java.util.ArrayList;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.MyViewHolder> {
    Context context;
    ArrayList<NotificationModel.Result> arrayList;


    public AdapterNotification(Context context,ArrayList<NotificationModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_notification,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvDateTime.setText(arrayList.get(position).dateTime);
        holder.binding.tvFrom.setText(arrayList.get(position).message);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemNotificationBinding binding;
        public MyViewHolder(@NonNull ItemNotificationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}