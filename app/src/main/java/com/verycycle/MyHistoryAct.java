package com.verycycle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.tabs.TabLayout;
import com.verycycle.adapter.MyAdapter;
import com.verycycle.databinding.ActivityMyHistoryBinding;

public class MyHistoryAct extends AppCompatActivity {
    ActivityMyHistoryBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_my_history);
        initViews();
    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> {
            finish();
        });
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.completed)));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.cancelled)));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.viewPager.setAdapter(new MyAdapter(this,getSupportFragmentManager(), binding.tabLayout.getTabCount()));
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
