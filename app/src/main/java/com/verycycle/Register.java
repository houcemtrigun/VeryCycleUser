package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.verycycle.databinding.ActivityRegisterBinding;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_register);
        SetupUI();
    }

    private void SetupUI() {
        binding.regLi.setOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
        });

        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,LoginActivity.class));
        });
    }
}