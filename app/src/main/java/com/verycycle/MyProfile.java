package com.verycycle;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.verycycle.databinding.ActivityMyProfileBinding;
import com.verycycle.helper.DataManager;

public class MyProfile extends AppCompatActivity {
    ActivityMyProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_profile);
        initViews();
    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> {finish();});

        binding.btnEditProfile.setOnClickListener(v->{startActivity(new Intent(MyProfile.this,EditProfileAct.class));});



    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvUsername.setText(DataManager.getInstance().getUserData(MyProfile.this).result.username);
        binding.tvAddress.setText(DataManager.getInstance().getUserData(MyProfile.this).result.email);
        Glide.with(MyProfile.this)
                .load(DataManager.getInstance().getUserData(MyProfile.this).result.image)
                .override(80,80)
                .apply(new RequestOptions().placeholder(R.drawable.user_default1))
                .into(binding.userImg);
    }
}
