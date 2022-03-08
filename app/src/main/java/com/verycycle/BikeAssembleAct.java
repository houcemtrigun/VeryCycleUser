package com.verycycle;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.verycycle.adapter.AdapterAssemble;
import com.verycycle.adapter.AdapterCycleModel;
import com.verycycle.databinding.ActivityBikeAssembleBinding;
import com.verycycle.databinding.ActivityChoosingTypeOfrideBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.GPSTracker;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.model.AssembleModel;
import com.verycycle.model.CycleModel;
import com.verycycle.model.SignupModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.Constant;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BikeAssembleAct extends AppCompatActivity {
    public String TAG = "BikeAssembleAct";
    ActivityBikeAssembleBinding binding;
    String str_image_path = "",BikeTypeID="",BikeText="",address="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    private Uri uriSavedImage;
    ArrayList<AssembleModel.Result> arrayList;
    AdapterAssemble adapter;
    VeryCycleUserInterface apiInterface;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_bike_assemble);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        adapter = new AdapterAssemble(BikeAssembleAct.this,arrayList);
        binding.spinnerBikeType.setAdapter(adapter);
        setCurrentLoc();



        binding.linerCycle.setOnClickListener(v -> {
            if(checkPermisssionForReadStorage())
                showImageSelection();
        });



        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(BikeAssembleAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });




        binding.ivBack.setOnClickListener(v -> { finish(); });

        binding.btnSendReq.setOnClickListener(v -> { validation(); });

        binding.spinnerBikeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BikeTypeID = arrayList.get(position).id;
                if(BikeTypeID.equals("7")) binding.cardEd.setVisibility(View.VISIBLE);
                else binding.cardEd.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getCarTypessss();
    }


    private void validation() {
        if(BikeTypeID.equals("")){
            App.showToast(BikeAssembleAct.this,getString(R.string.please_select_bike_type), Toast.LENGTH_LONG);
        }

        else if(BikeTypeID.equals("7")){
           if(binding.edtext.getText().toString().equals(""))
           { binding.edtext.setFocusable(true);
               binding.edtext.setError(getString(R.string.required));
           }
           else if(str_image_path.equals("")){
               App.showToast(BikeAssembleAct.this,getString(R.string.please_upload_cycle_image),Toast.LENGTH_LONG);
           }
           else {
               if(NetworkReceiver.isConnected())  SendAssembleReq(BikeTypeID,binding.edtext.getText().toString());
               else Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
           }

        }
        else if(str_image_path.equals("")){
            App.showToast(BikeAssembleAct.this,getString(R.string.please_upload_cycle_image),Toast.LENGTH_LONG);
        }
        else {
          if(NetworkReceiver.isConnected())  SendAssembleReq(BikeTypeID,"");
          else Toast.makeText(this, getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCarTypessss() {
        DataManager.getInstance().showProgressMessage(BikeAssembleAct.this, getString(R.string.please_wait));
        Call<AssembleModel> signupCall = apiInterface.getAllTypess();
        signupCall.enqueue(new Callback<AssembleModel>() {
            @Override
            public void onResponse(Call<AssembleModel> call, Response<AssembleModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    AssembleModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Cycle Model Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                        binding.spinnerBikeType.setSelection(0);
                        // car_id =  carArrayList.get(0).id;


                    } else if (data.status.equals("0")) {
                        App.showToast(BikeAssembleAct.this, data.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AssembleModel> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
                call.cancel();
            }
        });
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(BikeAssembleAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }

    private void openCamera() {

        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/VeryCycleUser/Images/");

        if (!dirtostoreFile.exists()) {
            dirtostoreFile.mkdirs();
        }

        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());

        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/VeryCycleUser/Images/" + "IMG_" + timestr + ".jpg");

        str_image_path = tostoreFile.getPath();

        uriSavedImage = FileProvider.getUriForFile(BikeAssembleAct.this,
                BuildConfig.APPLICATION_ID + ".provider",
                tostoreFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(intent, REQUEST_CAMERA);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            binding.ivCamera.setVisibility(View.GONE);
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(BikeAssembleAct.this, data.getData());
                Glide.with(BikeAssembleAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivCycle);

            } else if (requestCode == REQUEST_CAMERA) {
                Glide.with(BikeAssembleAct.this)
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivCycle);
            }

            else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
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


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(BikeAssembleAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(BikeAssembleAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(BikeAssembleAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BikeAssembleAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(BikeAssembleAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(BikeAssembleAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(BikeAssembleAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(BikeAssembleAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(BikeAssembleAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BikeAssembleAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }

    private void SendAssembleReq(String id,String name) {
        DataManager.getInstance().showProgressMessage(BikeAssembleAct.this, getString(R.string.please_wait));
        MultipartBody.Part filePart;
        if (!str_image_path.equalsIgnoreCase("")) {
            File file = DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody BikeId = RequestBody.create(MediaType.parse("text/plain"),id );
        RequestBody BikeName = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(BikeAssembleAct.this).result.id);
        RequestBody date = RequestBody.create(MediaType.parse("text/plain"),DataManager.getCurrent1() );
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"),DataManager.getCurrentTime() );
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(latitude));
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(longitude));
        RequestBody addresses = RequestBody.create(MediaType.parse("text/plain"),address );
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),"bike_assemble" );

        Call<Map<String,String>> signupCall = apiInterface.addCarAsmReq(user_id, BikeId, BikeName,date,time,lat,lon,addresses,type, filePart);
        signupCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "Bike Type  RESPONSE" + dataResponse);
                        Toast.makeText( BikeAssembleAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(BikeAssembleAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
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






    private void setCurrentLoc() {
        gpsTracker = new GPSTracker(BikeAssembleAct.this);
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
        address = DataManager.getInstance().getAddress(BikeAssembleAct.this,gpsTracker.getLatitude(),gpsTracker.getLongitude());
        binding.tvAddress.setText(address);
        Log.e("Location====","Latitude=== :"+gpsTracker.getLatitude() + "  " + "Longitute=== : " + gpsTracker.getLongitude());

    }


}
