package com.verycycle.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.verycycle.R;
import com.verycycle.databinding.ItemCardBinding;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.GetCardModel;
import com.vinaygaba.creditcardview.CardNumberFormat;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {
    Context context;
    OnItemPositionListener listener;
    ArrayList<GetCardModel.Result>arrayList;

    public CardAdapter(Context context, OnItemPositionListener listener, ArrayList<GetCardModel.Result> arrayList) {
        this.context = context;
        this.listener = listener;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.item_card,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {



        holder.binding.card1.setCardNumber(arrayList.get(position).cardNumber);
        holder.binding.card1.setExpiryDate(arrayList.get(position).expiryMonth+"/"+arrayList.get(position).expiryDate);
        holder.binding.card1.setCardName(arrayList.get(position).cardHolderName);
        holder.binding.card1.setCardNumberFormat(CardNumberFormat.MASKED_ALL_BUT_LAST_FOUR);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemCardBinding binding;
        public MyViewHolder(@NonNull ItemCardBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

             binding.tvEdit.setOnClickListener(v ->listener.onPosition(getAdapterPosition()) );

        }
    }
}

