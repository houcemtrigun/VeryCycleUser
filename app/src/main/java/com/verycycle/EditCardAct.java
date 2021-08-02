package com.verycycle;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.braintreepayments.cardform.view.CardForm;
import com.google.gson.Gson;
import com.stripe.android.model.Card;
import com.verycycle.databinding.ActivityEditCardBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.model.EditCardModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCardAct extends AppCompatActivity {
    ActivityEditCardBinding binding;
    String cardNumber="",monthvalid="",monthyear="",cxx="",id="",cardHolder="";
    Card.Builder card;
    VeryCycleUserInterface apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_card);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
       initViews();
    }

    private void initViews() {
        if(getIntent()!=null){
            cardHolder = getIntent().getStringExtra("cardHolder");
            cardNumber = getIntent().getStringExtra("cardNumber");
            monthvalid = getIntent().getStringExtra("monthvalid");
            monthyear = getIntent().getStringExtra("monthyear");
            id = getIntent().getStringExtra("id");
            cxx = getIntent().getStringExtra("cxx");

        }
        cardInit();
        binding.btnUpdate.setOnClickListener(v -> validation());

        binding.cardForm.getCardEditText().setText(cardNumber);
        binding.cardForm.getCvvEditText().setText(cxx);
        binding.cardForm.getExpirationDateEditText().setText(monthvalid  + monthyear);

        binding.header1.tvTitle.setText(getString(R.string.edit_card));

        binding.header1.ivBack1.setOnClickListener(v -> finish());
    }

    private void cardInit() {
        binding.cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                // .mobileNumberExplanation("SMS is required on this number")
                .setup(EditCardAct.this);

        binding.cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

    }


    private void validation() {
        Card.Builder card = new Card.Builder(binding.cardForm.getCardNumber(),
                Integer.valueOf(binding.cardForm.getExpirationMonth()),
                Integer.valueOf(binding.cardForm.getExpirationYear()),
                binding.cardForm.getCvv());

        if (!card.build().validateCard()) {
            Toast.makeText(EditCardAct.this, getString(R.string.card_not_valid), Toast.LENGTH_SHORT).show();

        }
        else {
            if(NetworkReceiver.isConnected()) {
                updateCard(binding.cardForm.getCardNumber(), binding.cardForm.getExpirationMonth().toString(),
                        binding.cardForm.getExpirationYear().toString()
                        , binding.cardForm.getCvv());
            }
            else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void updateCard(String cardNumber, String expiryMonth,String expiryYear, String cvv) {
        DataManager.getInstance().showProgressMessage(EditCardAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("card_holder_name",cardHolder);
        map.put("card_number",cardNumber);
        map.put("expiry_date",expiryYear);
        map.put("expiry_month",expiryMonth);
        map.put("cvc_code",cvv);
        Log.e("MapMap", "EDIT CARD REQUEST" + map);
        Call<EditCardModel> payCall = apiInterface.editCard( map);
        payCall.enqueue(new Callback<EditCardModel>() {
            @Override
            public void onResponse(Call<EditCardModel> call, Response<EditCardModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    EditCardModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "EDIT CARD RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        Toast.makeText(EditCardAct.this, getString(R.string.card_updated), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (data.status.equals("0")) {
                        Toast.makeText(EditCardAct.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<EditCardModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });


    }

}
