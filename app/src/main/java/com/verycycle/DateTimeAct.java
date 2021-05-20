package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.verycycle.adapter.AdapterTimeSlot;
import com.verycycle.databinding.ActivityDateTimeBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.model.DateTimeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DateTimeAct extends AppCompatActivity {
    ActivityDateTimeBinding binding;
    public static String date = "", time = "";
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String str_image_path = "", cycleId = "", problem = "", repair_image_path = "", address = "", type = "Normal repair";
    AdapterTimeSlot adapter;
    ArrayList<DateTimeModel.Result>arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_date_time);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        if (!Places.isInitialized()) {
            Places.initialize(DateTimeAct.this, getString(R.string.place_api_key));
        }

        if (getIntent() != null) {
            cycleId = getIntent().getStringExtra("cycleModel");
            str_image_path = getIntent().getStringExtra("cycleImage");
            problem = getIntent().getStringExtra("problem");
            repair_image_path = getIntent().getStringExtra("repairImage");
        }





        // binding.tvDate.setOnClickListener(v -> { DataManager.DatePicker(DateTimeAct.this,binding.tvDate::setText);});
        //   binding.tvTime.setOnClickListener(v -> { DataManager.TimePicker(DateTimeAct.this,binding.tvTime::setText);});

/*
        binding.btnContinue.setOnClickListener(v -> {
            if(date.equals("")){
                App.showToast(DateTimeAct.this,getString(R.string.please_select_date), Toast.LENGTH_LONG);
            }

           else if(time.equals("")){
                App.showToast(DateTimeAct.this,getString(R.string.please_select_time), Toast.LENGTH_LONG);
            }

           else if(address.equals("")){
                App.showToast(DateTimeAct.this,getString(R.string.please_select_address), Toast.LENGTH_LONG);
            }
           else {
                startActivity(new Intent(this, ProviderListAct.class).putExtra("cycleModel",cycleId)
                      .putExtra("cycleImage",str_image_path).putExtra("problem",problem)
                        .putExtra("repairImage",repair_image_path)
                        .putExtra("date",date)
                        .putExtra("time",time)
                        .putExtra("address",address)
                        .putExtra("lat",latitude+"")
                        .putExtra("lon",longitude+"")
                        .putExtra("serviceType",type));
            }
        });
*/



/*
        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(DateTimeAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });
*/
        adapter = new AdapterTimeSlot(DateTimeAct.this);
        binding.rvdateTime.setAdapter(adapter);


        binding.btnContinue.setOnClickListener(v -> {
            startActivity(new Intent(this, SelectAddressAct.class).putExtra("cycleModel",cycleId)
                    .putExtra("cycleImage",str_image_path).putExtra("problem",problem)
                    .putExtra("repairImage",repair_image_path)
                    //.putExtra("date",date)
                   // .putExtra("time",time)
                    .putExtra("address",address)
                    .putExtra("lat",latitude+"")
                    .putExtra("lon",longitude+"")
                    .putExtra("serviceType",type));
        });


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


}
