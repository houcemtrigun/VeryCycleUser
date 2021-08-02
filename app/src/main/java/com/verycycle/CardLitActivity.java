package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.CardAdapter;
import com.verycycle.databinding.ActivityCardListBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.GetCardModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardLitActivity extends AppCompatActivity implements OnItemPositionListener {
    ActivityCardListBinding binding;
    ArrayList<GetCardModel.Result>arrayList;
    VeryCycleUserInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_card_list);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        initViews();
    }

    private void initViews() {
       arrayList = new ArrayList<>();
       binding.header1.tvTitle.setText(getString(R.string.payment_info));
        if(NetworkReceiver.isConnected()) getCardList();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();

        binding.header1.ivBack1.setOnClickListener(v -> finish());

        binding.actionAdd.setOnClickListener(v ->  startActivity(new Intent(CardLitActivity.this,AddCardAct.class)));

    }

    private void getCardList() {
        DataManager.getInstance().showProgressMessage(CardLitActivity.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(CardLitActivity.this).result.id);
        Log.e("MapMap", "GET CARD REQUEST" + map);
        Call<GetCardModel> payCall = apiInterface.getCardList( map);
        payCall.enqueue(new Callback<GetCardModel>() {
            @Override
            public void onResponse(Call<GetCardModel> call, Response<GetCardModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    GetCardModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "GET CARD RESPONSE" + dataResponse);
                    arrayList.clear();
                    if (data.status.equals("1")) {
                        binding.txtCardNotFound.setVisibility(View.GONE);
                        arrayList.addAll(data.result);
                        binding.rvPayinfo.setAdapter(new CardAdapter(CardLitActivity.this,CardLitActivity.this, (ArrayList<GetCardModel.Result>) data.result));
                    } else if (data.status.equals("0")) {
                        binding.txtCardNotFound.setVisibility(View.VISIBLE);
                        //  Toast.makeText(PaymentInfoActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<GetCardModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });

    }




    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkReceiver.isConnected()) getCardList();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPosition(int position) {
        startActivity(new Intent(CardLitActivity.this,EditCardAct.class)
                .putExtra("cardHolder",arrayList.get(position).cardHolderName)
                .putExtra("id",arrayList.get(position).id)
                .putExtra("cardNumber",arrayList.get(position).cardNumber)
                .putExtra("monthvalid",arrayList.get(position).expiryMonth).putExtra("monthyear",arrayList.get(position).expiryDate).putExtra("cxx",arrayList.get(position).cvcCode));
    }
}
