package com.verycycle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.verycycle.R;
import com.verycycle.databinding.ItemProblemBinding;
import com.verycycle.databinding.ItemTimeSlotBinding;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.DateTimeModel;
import com.verycycle.model.ProblemModel;

import java.util.ArrayList;

public class AdapterTimeSlot extends RecyclerView.Adapter<AdapterTimeSlot.MyViewHolder> {
    Context context;
        ArrayList<DateTimeModel.Result> arrayList;
        OnItemPositionListener listener;

        public AdapterTimeSlot(Context context,ArrayList<DateTimeModel.Result> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTimeSlotBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_time_slot,parent,false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {





        }

        @Override
        public int getItemCount() {
/*
            return arrayList.size();
*/
            return 3;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ItemTimeSlotBinding binding;
            public MyViewHolder(@NonNull ItemTimeSlotBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;

                binding.tv910.setOnClickListener(v -> {

                });

            }
        }
    }

