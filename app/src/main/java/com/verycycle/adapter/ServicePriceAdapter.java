package com.verycycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.verycycle.R;
import com.verycycle.databinding.ItemServicePriceBinding;
import com.verycycle.databinding.ItemSubProblemBinding;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.listener.OnItemPositionListener2;
import com.verycycle.model.ServicesModel;
import com.verycycle.model.ServicesPriceModel;

import java.util.ArrayList;

public class ServicePriceAdapter extends RecyclerView.Adapter<ServicePriceAdapter.MyViewHolder> {
    Context context;
    ArrayList<ServicesPriceModel.Result> arrayList;
    OnItemPositionListener2 listener;

    public ServicePriceAdapter(Context context, ArrayList<ServicesPriceModel.Result> arrayList, OnItemPositionListener2 listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemServicePriceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_service_price,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       if(!arrayList.get(position).fromPrice.equals("") && !arrayList.get(position).toPrice.equals("")) {
           holder.binding.tvPrice.setText(arrayList.get(position).fromPrice + " - " + arrayList.get(position).toPrice);
       }
       else {
           holder.binding.tvPrice.setText(arrayList.get(position).fromPrice);

       }
       holder.binding.tv1.setText(arrayList.get(position).priceForFr);
       /* if(!arrayList.get(position).price.equals("")){
            holder.binding.tv2.setVisibility(View.VISIBLE);
            // holder.binding.tv2.setText("€"+arrayList.get(position).price);
        }
        else holder.binding.tv2.setVisibility(View.GONE);*/

       /* if(arrayList.get(position).isChk()== true)
            holder.binding.rlMain.setBackgroundResource(R.drawable.btn_bg_problem);
        else
            holder.binding.rlMain.setBackgroundResource(R.drawable.btn_bg_);*/

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemServicePriceBinding binding;
        public MyViewHolder(@NonNull ItemServicePriceBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.rlMain.setOnClickListener(v -> {
               /* for (int i=0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }
                arrayList.get(getAdapterPosition()).setChk(true);*/
                listener.onPosition2(getAdapterPosition(),binding.tvPrice.getText().toString());
              //  notifyDataSetChanged();
            });
        }
    }
}
