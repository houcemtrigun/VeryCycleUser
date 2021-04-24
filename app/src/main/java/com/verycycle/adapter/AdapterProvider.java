package com.verycycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.verycycle.R;
import com.verycycle.databinding.ItemProviderListBinding;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.ProviderModel;

import java.util.ArrayList;


public class AdapterProvider extends RecyclerView.Adapter<AdapterProvider.MyViewHolder> {
        Context context;
        ArrayList<ProviderModel.Result> arrayList;
        OnItemPositionListener listener;

        public AdapterProvider(Context context,ArrayList<ProviderModel.Result>arrayList,OnItemPositionListener listener) {
            this.context = context;
            this.arrayList = arrayList;
            this.listener =listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemProviderListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_provider_list,parent,false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.binding.tvName.setText(arrayList.get(position).username + " " );
            //holder.binding.rate.setRating(Float.parseFloat(arrayList.get(position).rating));
            holder.binding.tvDistance.setText(arrayList.get(position).km);
            holder.binding.tvAddress.setText(arrayList.get(position).address);
            Glide.with(context)
                    .load(arrayList.get(position).image)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .override(100,100)
                    .into(holder.binding.ivProfile);

            if(arrayList.get(position).isChk()){
                holder.binding.layoutMain.setBackgroundColor(context.getResources().getColor(R.color.black));
                holder.binding.tvName.setTextColor(context.getResources().getColor(R.color.white));
                holder.binding.tvDistance.setTextColor(context.getResources().getColor(R.color.white));
                holder.binding.tvAddress.setTextColor(context.getResources().getColor(R.color.white));
            }else {
                holder.binding.layoutMain.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.tvName.setTextColor(context.getResources().getColor(R.color.black));
                holder.binding.tvDistance.setTextColor(context.getResources().getColor(R.color.black));
                holder.binding.tvAddress.setTextColor(context.getResources().getColor(R.color.black));
            }

        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ItemProviderListBinding binding;
            public MyViewHolder(@NonNull ItemProviderListBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;

                binding.layoutMain.setOnClickListener(v -> {
                  for(int i=0;i<arrayList.size();i++){
                      arrayList.get(getAdapterPosition()).setChk(false);
                  }
                    arrayList.get(getAdapterPosition()).setChk(true);
                    listener.onPosition(getAdapterPosition());
                    notifyDataSetChanged();

                });
            }
        }
    }


