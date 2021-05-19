package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.verycycle.databinding.ActivitySelectAddressBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.GPSTracker;
import com.verycycle.helper.NetworkReceiver;

import java.util.Arrays;
import java.util.List;

public class SelectAddressAct extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    ActivitySelectAddressBinding binding;
    GoogleMap map;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String str_image_path = "",cycleId="",problem="",repair_image_path="",address="",lat="",lon="",serviceType="";
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        gpsTracker = new GPSTracker(SelectAddressAct.this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initView();

    }

    private void initView() {
        if (!Places.isInitialized()) {
            Places.initialize(SelectAddressAct.this, getString(R.string.place_api_key));
        }

        binding.toolbar.tvTitle.setText(getString(R.string.select_address));

        binding.toolbar.ivBack.setOnClickListener(v -> {
            finish();
        });

        if(getIntent()!=null){
            cycleId = getIntent().getStringExtra("cycleModel");
            str_image_path = getIntent().getStringExtra("cycleImage");
            problem = getIntent().getStringExtra("problem");
            repair_image_path = getIntent().getStringExtra("repairImage");
           // date = getIntent().getStringExtra("date");
         //   time = getIntent().getStringExtra("time");
            address = getIntent().getStringExtra("address");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");
            serviceType = getIntent().getStringExtra("serviceType");
        }


        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(SelectAddressAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });

        binding.ivLocation.setOnClickListener(v -> {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            address = DataManager.getInstance().getAddress(SelectAddressAct.this, gpsTracker.getLatitude(), gpsTracker.getLongitude());
            binding.tvAddress.setText(address);
            map.clear();
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Marker in Location"));
            map.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(latitude, longitude))));

        });


        binding.btnContinue.setOnClickListener(v -> {
            Toast.makeText(SelectAddressAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SelectAddressAct.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    Log.e("addressStreet====", place.getAddress());
                    address = place.getAddress();
                    binding.tvAddress.setText(place.getAddress());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    map.clear();
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title("Marker in Location"));
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(latitude, longitude))));

                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }

        }

    }

    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(14).build();
    }




}
