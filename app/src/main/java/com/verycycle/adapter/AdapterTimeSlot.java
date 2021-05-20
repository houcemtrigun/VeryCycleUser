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

        public AdapterTimeSlot(Context context) {
            this.context = context;
         //   this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemTimeSlotBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_time_slot,parent,false);
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

       /*    if(arrayList.get(position).timeSlot.get(0).available.equals("yes")){
                holder.binding.tv910.setText(arrayList.get(position).timeSlot.get(0).time);
                if(arrayList.get(position).timeSlot.get(0).isChk()==false) holder.binding.tv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv910.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv910.setText(arrayList.get(position).timeSlot.get(0).available);

            }

            if(arrayList.get(position).timeSlot.get(1).available.equals("yes")){
                holder.binding.tv1011.setText(arrayList.get(position).timeSlot.get(1).time);
                if(arrayList.get(position).timeSlot.get(1).isChk()==false) holder.binding.tv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv1011.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv910.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv1011.setText(arrayList.get(position).timeSlot.get(1).available);

            }


            if(arrayList.get(position).timeSlot.get(2).available.equals("yes")){
                holder.binding.tv1112.setText(arrayList.get(position).timeSlot.get(2).time);
                if(arrayList.get(position).timeSlot.get(2).isChk()==false) holder.binding.tv1112.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv1112.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv1112.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv1112.setText(arrayList.get(position).timeSlot.get(2).available);
            }

            if(arrayList.get(position).timeSlot.get(3).available.equals("yes")){
                holder.binding.tv1201.setText(arrayList.get(position).timeSlot.get(3).time);
                if(arrayList.get(position).timeSlot.get(3).isChk()==false) holder.binding.tv1201.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv1201.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv1201.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv1201.setText(arrayList.get(position).timeSlot.get(3).available);

            }

            if(arrayList.get(position).timeSlot.get(4).available.equals("yes")){
                holder.binding.tv0102.setText(arrayList.get(position).timeSlot.get(4).time);

                if(arrayList.get(position).timeSlot.get(4).isChk()==false) holder.binding.tv0102.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv0102.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0102.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv1011.setText(arrayList.get(position).timeSlot.get(4).available);

            }


            if(arrayList.get(position).timeSlot.get(5).available.equals("yes")){
                holder.binding.tv0203.setText(arrayList.get(position).timeSlot.get(5).time);
                if(arrayList.get(position).timeSlot.get(5).isChk()==false) holder.binding.tv0203.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv0203.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0203.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv0203.setText(arrayList.get(position).timeSlot.get(5).available);
            }


            if(arrayList.get(position).timeSlot.get(6).available.equals("yes")){
                holder.binding.tv0304.setText(arrayList.get(position).timeSlot.get(6).time);
                if(arrayList.get(position).timeSlot.get(6).isChk()==false) holder.binding.tv0304.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv0304.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0304.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv0304.setText(arrayList.get(position).timeSlot.get(6).available);

            }


            if(arrayList.get(position).timeSlot.get(7).available.equals("yes")){
                holder.binding.tv0405.setText(arrayList.get(position).timeSlot.get(7).time);
                if(arrayList.get(position).timeSlot.get(7).isChk()==false) holder.binding.tv0405.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv0405.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0405.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv0405.setText(arrayList.get(position).timeSlot.get(7).available);

            }


            if(arrayList.get(position).timeSlot.get(8).available.equals("yes")){
                holder.binding.tv0506.setText(arrayList.get(position).timeSlot.get(8).time);
                if(arrayList.get(position).timeSlot.get(8).isChk()==false) holder.binding.tv0506.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv0506.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0506.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv0506.setText(arrayList.get(position).timeSlot.get(8).available);

            }


            if(arrayList.get(position).timeSlot.get(9).available.equals("yes")){
                holder.binding.tv0607.setText(arrayList.get(position).timeSlot.get(9).time);
                if(arrayList.get(position).timeSlot.get(9).isChk()==false) holder.binding.tv0607.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                else holder.binding.tv0607.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0607.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv0607.setText(arrayList.get(position).timeSlot.get(9).available);

            }


            if(arrayList.get(position).timeSlot.get(10).available.equals("yes")){
                holder.binding.tv0708.setText(arrayList.get(position).timeSlot.get(10).time);
                if(arrayList.get(position).timeSlot.get(10).isChk()==false) holder.binding.tv0708.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                    else holder.binding.tv0708.setBackgroundResource(R.drawable.rounded_green_5dp);
            }
            else {
                holder.binding.tv0708.setBackgroundResource(R.drawable.rounded_shap_white_5dp);
                holder.binding.tv0708.setText(arrayList.get(position).timeSlot.get(10).available);

            }
                  */



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

/*
                binding.tv910.setOnClickListener(v -> {
                   if(arrayList.get(getItemViewType()).timeSlot.get(0).available.equals("yes")){
                       if(arrayList.get(getAdapterPosition()).timeSlot.get(0).isChk()==false){
                           arrayList.get(getAdapterPosition()).timeSlot.get(0).setChk(true);
                           notifyDataSetChanged();
                       }else {
                           arrayList.get(getAdapterPosition()).timeSlot.get(0).setChk(false);
                           notifyDataSetChanged();
                       }
                   }
                });
*/

            }
        }
    }

