 package com.verycycle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.MacAddress;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.verycycle.databinding.ActivityMainBinding;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.GPSTracker;
import com.verycycle.helper.SessionManager;
import com.verycycle.retrofit.Constant;
import com.verycycle.setting.HelpAndFeedBack;
import com.verycycle.setting.HelpSetting;
import com.verycycle.ui.home.HomeFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    GPSTracker gpsTracker;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =    DataBindingUtil.setContentView(this,R.layout.activity_main);
        initViews();

    }

    private void initViews() {
        DataManager.updateResources(MainActivity.this,"fr");
        SessionManager.writeString(MainActivity.this, Constant.LANGUAGE,"fr");

        binding.chlidDashboard.tvUsername.setText(getString(R.string.hello) + " "+DataManager.getInstance().getUserData(MainActivity.this).result.username);

        binding.chlidDashboard.linearNormal.setOnClickListener(v -> { startActivity(new Intent(MainActivity.this, ChoosingTypeOfride.class));});

        binding.chlidDashboard.llUrgent.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this, UrgenRequestAct.class));});

        binding.chlidDashboard.llBike.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,BikeAssembleAct.class));});

        binding.chlidDashboard.navbar.setOnClickListener(v -> {navmenu();});

        binding.childNavDrawer.llHome.setOnClickListener(v -> {navmenu();});

        binding.childNavDrawer.llAccept.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,AcceptReqAct.class));});

        binding.childNavDrawer.llRequest.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,SendRequestAct.class));});

        binding.childNavDrawer.llProfile.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,MyProfile.class));});

        binding.childNavDrawer.llMyHistory.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,MyHistoryAct.class));});

        binding.childNavDrawer.llHelp.setOnClickListener(v -> { startActivity(new Intent(MainActivity.this, HelpAndFeedBack.class));});

        binding.childNavDrawer.llNotification.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,NotificationAct.class));});


        binding.childNavDrawer.llSetting.setOnClickListener(v -> {});

        binding.childNavDrawer.llCardList.setOnClickListener(v -> {startActivity(new Intent(MainActivity.this,CardLitActivity.class));});

        binding.childNavDrawer.btnSignout.setOnClickListener(v -> {
            SessionManager.clear(MainActivity.this,DataManager.getInstance().getUserData(MainActivity.this).result.id);
        });


        if (checkPermissions()) {
            if (isLocationEnabled()) {
                setCurrentLoc();
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        } else {
            requestPermissions();
        }

    }


    private void setCurrentLoc() {
        gpsTracker = new GPSTracker(MainActivity.this);
        binding.chlidDashboard.tvAddress.setText(DataManager.getInstance().getAddress(MainActivity.this,gpsTracker.getLatitude(),gpsTracker.getLongitude()));
    }

    public void navmenu() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }


    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCurrentLoc();
            }
        }
    }


}