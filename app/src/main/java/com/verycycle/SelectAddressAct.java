package com.verycycle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.google.gson.Gson;
import com.verycycle.databinding.ActivitySelectAddressBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.GPSTracker;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectAddressAct extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    ActivitySelectAddressBinding binding;
    GoogleMap map;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String str_image_path = "",cycleId="",problem="",repair_image_path="",address="",lat="",lon="",serviceType="",time="";
    GPSTracker gpsTracker;
    int PERMISSION_ID = 44;
    VeryCycleUserInterface apiInterface;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_address);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
     //   mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
     //   mapFragment.getMapAsync(this);

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
            time = getIntent().getStringExtra("time");
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
          //  Toast.makeText(SelectAddressAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
           // startActivity(new Intent(SelectAddressAct.this,SelectAddressAct.class));

            sendBookingRequest("");
            //finish();
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        address = DataManager.getInstance().getAddress(SelectAddressAct.this,gpsTracker.getLatitude(),gpsTracker.getLongitude());
        binding.tvAddress.setText(address);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title("Marker in Location"));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(new LatLng(latitude, longitude))));

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

    private void setCurrentLoc() {
        gpsTracker = new GPSTracker(SelectAddressAct.this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.e("Location====","Latitude=== :"+gpsTracker.getLatitude() + "  " + "Longitute=== : " + gpsTracker.getLongitude());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void sendBookingRequest(String providerId) {
        DataManager.getInstance().showProgressMessage(SelectAddressAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart,filePart1;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("cycle_image", file.getName(), RequestBody.create(MediaType.parse("cycle_image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        if (!repair_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(repair_image_path));
            filePart1 = MultipartBody.Part.createFormData("repaire_image", file.getName(), RequestBody.create(MediaType.parse("repaire_image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart1 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }


        RequestBody cycle_id = RequestBody.create(MediaType.parse("text/plain"),cycleId);
        RequestBody problm = RequestBody.create(MediaType.parse("text/plain"), problem);
        RequestBody datE = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody timE = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody addreSS = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody latitude1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody longitude1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
        RequestBody provider_id = RequestBody.create(MediaType.parse("text/plain"), "");
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(SelectAddressAct.this).result.id);
        RequestBody serviceType1 = RequestBody.create(MediaType.parse("text/plain"), serviceType);


        Call<Map<String,String>> signupCall = apiInterface.sendRequest(cycle_id, problm, datE, timE, addreSS,latitude1,longitude1,user_id, provider_id,serviceType1,filePart,filePart1);
        signupCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        String request_id = data.get("request_id");
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                       // Toast.makeText(SelectAddressAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SelectAddressAct.this,PaymentAct.class)
                        .putExtra("request_id",request_id));
                        finish();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(SelectAddressAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String,String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }

}
