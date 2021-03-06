package com.verycycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.verycycle.R;
import com.verycycle.databinding.ItemCompleteBinding;
import com.verycycle.databinding.ItemProviderListBinding;
import com.verycycle.model.HistoryModel;

import java.util.ArrayList;

public class  AdapterComplete extends RecyclerView.Adapter<AdapterComplete.MyViewHolder> {
    Context context;
    ArrayList<HistoryModel.Result>arrayList;
    double amount=0.00,anualAmount=0.00;


    public AdapterComplete(Context context,ArrayList<HistoryModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCompleteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_complete,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(!arrayList.get(position).amount.equals("")) amount = Double.parseDouble(arrayList.get(position).amount); else amount =0.00;
        if(!arrayList.get(position).manual_amount.equals(""))   anualAmount = Double.parseDouble(arrayList.get(position).manual_amount); else anualAmount =0.00;
        double totalAmount = amount+anualAmount;

      holder.binding.tvDateTime.setText(arrayList.get(position).accept_time_slote);
      holder.binding.tvPrice.setText("€"+totalAmount);
      holder.binding.tvFrom.setText(arrayList.get(position).address);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCompleteBinding binding;
        public MyViewHolder(@NonNull ItemCompleteBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
