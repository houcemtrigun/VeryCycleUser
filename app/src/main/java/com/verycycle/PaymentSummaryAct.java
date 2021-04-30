package com.verycycle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;
import com.verycycle.databinding.ActivityPaymentSummaryBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.model.PaymentSummaryModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentSummaryAct extends AppCompatActivity {
    public String TAG = "PaymentSummaryAct";
    VeryCycleUserInterface apiInterface;
    ActivityPaymentSummaryBinding binding;
    String requestId = "", providerId = "", providerName = "", providerImage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_summary);
        initView();
    }


    private void initView() {
        if (getIntent() != null) {
            requestId = getIntent().getStringExtra("request_id");
            providerId = getIntent().getStringExtra("ProviderId");
            providerName = getIntent().getStringExtra("ProviderName");
            providerImage = getIntent().getStringExtra("ProviderImage");
            if (NetworkReceiver.isConnected())
                GetPaymentSummary(requestId);
             else
                App.showToast(PaymentSummaryAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);
        }

        binding.btnPay.setOnClickListener(v -> {
            RateDialog();

        });


    }

    private void GetPaymentSummary(String request_id) {
        Map<String, String> map = new HashMap<>();
        map.put("request_id", request_id);
        Log.e(TAG, "PAYMEN SUMMARY REQUEST" + map);
        Call<PaymentSummaryModel> chatCount = apiInterface.getPaymentSummary(map);
        chatCount.enqueue(new Callback<PaymentSummaryModel>() {
            @Override
            public void onResponse(Call<PaymentSummaryModel> call, Response<PaymentSummaryModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    PaymentSummaryModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e(TAG, "PAYMEN SUMMARY RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        binding.tvAmount.setText("$" + data.result.totalAmount + "");
                        binding.tvServiceAmount.setText("$" + data.result.serviceAmount);
                        binding.tvExtraAmount.setText("$" + data.result.extraAmount);
                        binding.tvMainTotal.setText("$" + data.result.totalAmount + "");
                        binding.tvAmount.setText("$" + data.result.totalAmount + "");
                        binding.tv2.setText(data.result.date + " " + data.result.time);
                    } else if (data.status.equals("0")) {

                        App.showToast(PaymentSummaryAct.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<PaymentSummaryModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void RateDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PaymentSummaryAct.this);
        builder1.setMessage(getResources().getString(R.string.payment_successfully_done));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                "Give Rate",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        startActivity(new Intent(PaymentSummaryAct.this, RatingAct.class).putExtra("request_id", requestId)
                                .putExtra("ProviderId",providerId).putExtra("ProviderName",providerName).putExtra("ProviderImage",providerImage));
                        finish();
                        dialog.dismiss();

                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


}
