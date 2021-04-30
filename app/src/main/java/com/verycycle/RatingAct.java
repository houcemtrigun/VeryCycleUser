package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.verycycle.databinding.ActivityGiveRateBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingAct extends AppCompatActivity {
    public String TAG = "RatingAct";
    VeryCycleUserInterface apiInterface;
    ActivityGiveRateBinding binding;
    String requestId = "", providerId = "", providerName = "", providerImage = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_give_rate);
        initView();
    }

    private void initView() {
        if (getIntent() != null) {
            requestId = getIntent().getStringExtra("request_id");
                providerId = getIntent().getStringExtra("ProviderId");
                providerName = getIntent().getStringExtra("ProviderName");
                providerImage = getIntent().getStringExtra("ProviderImage");

                binding.tvUserName.setText(providerName);
                Glide.with(RatingAct.this)
                    .load(providerImage)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .override(100,100)
                    .into(binding.ivDriverPropic3);

        }

        binding.btnRate.setOnClickListener(v -> {
            if (NetworkReceiver.isConnected())
                giveRateDriver();
            else
                App.showToast(RatingAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);
        });

    }




    public void giveRateDriver(){
        DataManager.getInstance().showProgressMessage(RatingAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("request_id",requestId);
        map.put("receiver_id",providerId);
        map.put("rating", String.valueOf(binding.ratingBar.getRating()));
        map.put("review",binding.editText.getText().toString());
        map.put("sender_id",DataManager.getInstance().getUserData(RatingAct.this).result.id);
        Log.e(TAG,"Give Rate Request "+map);
        Call<Map<String,String>> loginCall = apiInterface.giveRate(map);
        loginCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Give Rate Response :"+responseString);
                    if(data.get("status").equals("1")){
                        // App.showToast(PaymentSummary.this,getString(R.string.));
                        startActivity(new Intent(RatingAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else if(data.get("status").equals("0")){
                        App.showToast(RatingAct.this,data.get("message"),Toast.LENGTH_LONG);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}
