package com.verycycle;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.verycycle.databinding.ActivityPaymentSummaryBinding;

public class PaymentSummaryAct extends AppCompatActivity {
    ActivityPaymentSummaryBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_summary);
     initView();
    }

    private void initView() {
        binding.btnPay.setOnClickListener(v -> {
            startActivity(new Intent(PaymentSummaryAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }
}
