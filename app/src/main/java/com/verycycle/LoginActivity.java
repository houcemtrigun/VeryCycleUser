package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.verycycle.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);

        SetupUI();
    }

    private void SetupUI() {
        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this,MainActivity.class));
        });

        binding.singup.setOnClickListener(v -> {
            startActivity(new Intent(this,Register.class));
        });

    }
}