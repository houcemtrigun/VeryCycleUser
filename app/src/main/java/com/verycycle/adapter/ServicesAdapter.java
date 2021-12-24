package com.verycycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.verycycle.R;
import com.verycycle.databinding.ItemSubProblemBinding;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.ServicesModel;
import com.verycycle.model.SubProblmModel;

import java.util.ArrayList;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyViewHolder> {
    Context context;
    ArrayList<ServicesModel.Result> arrayList;
    OnItemPositionListener listener;

    public ServicesAdapter(Context context, ArrayList<ServicesModel.Result> arrayList, OnItemPositionListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubProblemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sub_problem,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tv1.setText(arrayList.get(position).nameFr);
      /*  if(!arrayList.get(position).price.equals("")){
            holder.binding.tv2.setVisibility(View.VISIBLE);
            // holder.binding.tv2.setText("€"+arrayList.get(position).price);
        }
        else holder.binding.tv2.setVisibility(View.GONE);*/

        if(arrayList.get(position).isChk()== true)
            holder.binding.rlMain.setBackgroundResource(R.drawable.btn_bg_problem);
        else
            holder.binding.rlMain.setBackgroundResource(R.drawable.btn_bg_);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubProblemBinding binding;
        public MyViewHolder(@NonNull ItemSubProblemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.tv1.setOnClickListener(v -> {
                for (int i=0;i<arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }
                arrayList.get(getAdapterPosition()).setChk(true);
                listener.onPosition(getAdapterPosition());
                notifyDataSetChanged();
            });
        }
    }
}
