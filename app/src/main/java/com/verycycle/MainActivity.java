 package com.verycycle;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.verycycle.databinding.ActivityMainBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.SessionManager;
import com.verycycle.setting.HelpAndFeedBack;
import com.verycycle.setting.HelpSetting;
import com.verycycle.ui.home.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =    DataBindingUtil.setContentView(this,R.layout.activity_main);
        initViews();

    }

    private void initViews() {

        binding.chlidDashboard.tvUsername.setText(getString(R.string.hello) + " "+DataManager.getInstance().getUserData(MainActivity.this).result.username);

        binding.chlidDashboard.linearNormal.setOnClickListener(v -> { startActivity(new Intent(MainActivity.this, ChoosingTypeOfride.class));});

        binding.chlidDashboard.llUrgent.setOnClickListener(v -> {});

        binding.chlidDashboard.llBike.setOnClickListener(v -> {});

        binding.chlidDashboard.navbar.setOnClickListener(v -> {navmenu();});

        binding.childNavDrawer.llHome.setOnClickListener(v -> {navmenu();});

        binding.childNavDrawer.llAccept.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,AcceptReqAct.class));});

        binding.childNavDrawer.llRequest.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,SendRequestAct.class));});

        binding.childNavDrawer.llProfile.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,MyProfile.class));});

        binding.childNavDrawer.llMyHistory.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,MyHistory.class));});

        binding.childNavDrawer.llHelp.setOnClickListener(v -> { startActivity(new Intent(MainActivity.this, HelpAndFeedBack.class));});

        binding.childNavDrawer.llNotification.setOnClickListener(v -> {});

        binding.childNavDrawer.llSetting.setOnClickListener(v -> {});

        binding.childNavDrawer.btnSignout.setOnClickListener(v -> {
            SessionManager.clear(MainActivity.this,DataManager.getInstance().getUserData(MainActivity.this).result.id);
        });

    }




    public void navmenu() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }


}