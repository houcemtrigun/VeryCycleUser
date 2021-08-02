package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.braintreepayments.cardform.view.CardForm;
import com.google.gson.Gson;
import com.verycycle.databinding.ActvityAddCardBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.model.AddCard;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardAct extends AppCompatActivity  {
    public String TAG = "AddCardAct";
    ActvityAddCardBinding binding;
    VeryCycleUserInterface apiInterface;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.actvity_add_card);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        initViews();
    }

    private void initViews() {
        binding.cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .mobileNumberExplanation("Make sure SMS is enabled for this mobile number")
                .actionLabel(getString(R.string.purchase))
                .setup(this);

        binding.header1.tvTitle.setText(getString(R.string.add_card_));

        binding.header1.ivBack1.setOnClickListener(v -> finish());

        binding.btnAdd.setOnClickListener(v -> {
            if(NetworkReceiver.isConnected()) addCard(); else Toast.makeText(AddCardAct.this,getString(R.string.network_failure),Toast.LENGTH_LONG).show();
        });
    }


    public void addCard() {
        DataManager.getInstance().showProgressMessage(AddCardAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("card_holder_name", binding.cardForm.getCardholderName());
        map.put("card_number", binding.cardForm.getCardNumber());
        map.put("expiry_date",  binding.cardForm.getExpirationYear());
        map.put("expiry_month",  binding.cardForm.getExpirationMonth());
        map.put("cvc_code", binding.cardForm.getCvv()+"");
        map.put("user_id", DataManager.getInstance().getUserData(AddCardAct.this).result.id);
        Log.e(TAG, "Add Bank Card Request :" + map);
        Call<AddCard> callNearCar = apiInterface.addCardss(map);
        callNearCar.enqueue(new Callback<AddCard>() {
            @Override
            public void onResponse(Call<AddCard> call, Response<AddCard> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    AddCard data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e(TAG, "Add Bank Card Response :" + dataResponse);
                    if (data.status.equals("1")) {
                       finish();
                    } else if (data.status.equals("0")) {
                        Toast.makeText(AddCardAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AddCard> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });

    }

}
