package com.verycycle;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.verycycle.databinding.ActivityTrackBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.GPSTracker;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.helper.UpdateLocationService;
import com.verycycle.maps.DrawPollyLine;
import com.verycycle.model.BookingDetailModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.Constant;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackAct extends AppCompatActivity implements OnMapReadyCallback {
    public static String TAG = "TrackAct";
    ActivityTrackBinding binding;
    GoogleMap mMap;
    int PERMISSION_ID = 44;
    double dLatitude = 0.0, dLongitude = 0.0;
    VeryCycleUserInterface apiInterface;
    String request_id = "", DriverId = "", DriverName = "", image = "", mobile = "", status = "", booking_image = "", ServiceAddress = "";
    private PolylineOptions lineOptions;
    private LatLng PickUpLatLng, DropOffLatLng, carLatLng, prelatLng;
    private MarkerOptions PicUpMarker, DropOffMarker, carMarker1;
    Marker carMarker;
    BookingDetailModel data1;
    double tolerance = 10; // meters
    ArrayList<LatLng> polineLanLongLine = new ArrayList<>();
    boolean isMarkerRotating = false;
    private float start_rotation;
    GPSTracker gpsTracker;
    AlertDialog alert33;
    double amount =0.00, anualAmount=0.00;


    BroadcastReceiver LocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("latitude") != null) {
                if (NetworkReceiver.isConnected())
                    getdriverLocation();
                else
                    App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
            }
        }
    };


    BroadcastReceiver TripStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("status") != null) {
                if (intent.getStringExtra("status").equals("chat")) {
                    if (NetworkReceiver.isConnected()) {
                        //  request_id = intent.getStringExtra("request_id");
                        getChatCount();
                    } else
                        App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                } else if (intent.getStringExtra("status").equals("Cancel_by_driver")) {
                    startActivity(new Intent(TrackAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
                else if (intent.getStringExtra("status").equals("Cancel_by_driver")) {
                    startActivity(new Intent(TrackAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
                else if (intent.getStringExtra("status").equals("Way")) {
                    if (NetworkReceiver.isConnected()) {
                        getBookingDetail(intent.getStringExtra("request_id"));
                    }
                    else
                        App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                }

                else if (intent.getStringExtra("status").equals("send_request")) {
                    if (NetworkReceiver.isConnected()) {
                        getBookingDetail(intent.getStringExtra("request_id"));
                    }
                    else
                        App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                }

                else {
                    if (NetworkReceiver.isConnected())
                        getBookingDetail(intent.getStringExtra("request_id"));
                    else
                        App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_SHORT);
                }
            }
        }
    };

    private void getdriverLocation() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", SessionManager.readString(getApplicationContext(), Constant.driver_id, ""));
        Log.e("MapMap", "GET PROVIDER LATLONG REQUEST" + map);
        Call<Map<String, String>> subCategoryCall = apiInterface.getDriverLocation(map);
        subCategoryCall.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "GET PROVIDER LATLONG RESPONSE" + dataResponse);
                        dLatitude = Double.parseDouble(data.get("lat"));
                        dLongitude = Double.parseDouble(data.get("lon"));
                        carLatLng = new LatLng(dLatitude, dLongitude);
                        AddCarMarker(carLatLng);


                        Log.e("Current Locatoion===", dLatitude + "," + dLongitude);
                    } else if (data.get("status").equals("0")) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        // gpsTracker = new GPSTracker(TrackAct.this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_track);
        if (getIntent() != null) {
            request_id = getIntent().getStringExtra("request_id");
        }

        initView();
        getdriverLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initView() {
        binding.ivBack.setOnClickListener(v -> {
            startActivity(new Intent(TrackAct.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });

        /*binding.btnRate.setOnClickListener(v -> {
            if(NetworkReceiver.isConnected()) giveRateDriver(); else App.showToast(TrackAct.this,getString(R.string.network_failure),Toast.LENGTH_SHORT);
        });

        binding.ivCancel.setOnClickListener(v -> {
            startActivity(new Intent(TrackAct.this, RideCancelAct.class).putExtra("request_id", request_id));
        });*/

        binding.layoutforChat.setOnClickListener(v -> {
            startActivity(new Intent(TrackAct.this, MsgChatAct.class)
                    .putExtra("DriverId", DriverId)
                    .putExtra("DriverName", DriverName)
                    .putExtra("DriverImage", image)
                    .putExtra("request_id", request_id));
        });

        binding.layoutforCall.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
            startActivity(intent);
        });


        PicUpMarker = new MarkerOptions().title("Pick Up Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
        DropOffMarker = new MarkerOptions().title("Drop Off Location")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_marker));

        carMarker1 = new MarkerOptions().title("Car")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //   gpsTracker = new GPSTracker(TrackAct.this);
        if (!request_id.equals(""))
            getBookingDetail(request_id);
    }


    public void DriverArriveDialog() {
        final Dialog dialog = new Dialog(TrackAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_driver_arrived);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        binding.titler.setText(getString(R.string.provider_arrived));

        dialog.setCanceledOnTouchOutside(true);
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvMsg = dialog.findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.VISIBLE);


        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }

    public void TripStartDialog() {
        final Dialog dialog = new Dialog(TrackAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_driver_arrived);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(getString(R.string.your_service_has_begin));
        binding.titler.setText(getString(R.string.service_start));
        dialog.setCanceledOnTouchOutside(true);
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvMsg = dialog.findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.GONE);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }


    public void TripFinishDialog() {

        final Dialog dialog = new Dialog(TrackAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_driver_arrived);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);

        tvTitle.setText(getString(R.string.your_service_is_finished));
        binding.titler.setText(getString(R.string.service_end));
        dialog.setCanceledOnTouchOutside(true);
        TextView btnOk = dialog.findViewById(R.id.btnOk);
        TextView tvMsg = dialog.findViewById(R.id.tvMsg);
        tvMsg.setVisibility(View.GONE);

        btnOk.setOnClickListener(v -> {
            dialog.dismiss();
            //   binding.titler.setText("Send Feedback");
            binding.ivBack.setVisibility(View.GONE);
            binding.rlDriver.setVisibility(View.GONE);
            //  binding.rlFeedback.setVisibility(View.VISIBLE);
          /*  Intent i = new Intent(TrackAct.this, TripEndAct.class).putExtra("request_id", request_id)
                    .putExtra("driver_id", DriverId);
            startActivityForResult(i, 1);*/

        });

        dialog.show();

    }

    private void setCurrentLoc() {
        DrawPolyLine();
    }

    private void animateCamera(@NonNull LatLng location) {
        //  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(location)));
    }


    @NonNull
    private CameraPosition getCameraPositionWithBearing(LatLng latLng) {
        return new CameraPosition.Builder().target(latLng).zoom(16).build();
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

    public void getBookingDetail(String request_id) {
        Map<String, String> map = new HashMap<>();
        map.put("request_id", request_id);
        Log.e(TAG, "Request Accept or Cancel Request :" + map);
        Call<BookingDetailModel> call = apiInterface.bookingDetails(map);
        call.enqueue(new Callback<BookingDetailModel>() {
            @Override
            public void onResponse(Call<BookingDetailModel> call, Response<BookingDetailModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    data1 = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Request Accept or Cancel Response :" + responseString);
                    if (data1.status.equals("1")) {
                        DriverName = data1.result.driverDetails.username;
                        DriverId = data1.result.driverDetails.id;
                       if(data1.result.manual_status.equals("send_request")) data1.result.setStatus(data1.result.manual_status);
                        status = data1.result.status;
                        image = data1.result.driverDetails.driverImage;
                        ServiceAddress = data1.result.address;
                        mobile = "+" + data1.result.driverDetails.countryCode + data1.result.driverDetails.mobile;
                       if(!data1.result.amount.equals("")) amount = Double.parseDouble(data1.result.amount); else amount =0.00;
                        if(!data1.result.manual_amount.equals(""))   anualAmount = Double.parseDouble(data1.result.manual_amount); else anualAmount =0.00;
                        double totalAmount = amount+anualAmount;
                        binding.tvAmount.setText("€"+totalAmount +"");
                        binding.tvName.setText(DriverName);
                        Glide.with(TrackAct.this)
                                .load(image)
                                .apply(new RequestOptions().placeholder(R.drawable.user_default))
                                .override(300, 300)
                                .into(binding.ivDriverPropic);
                        prelatLng = null;
                        PickUpLatLng = new LatLng(Double.parseDouble(data1.result.driverDetails.lat), Double.parseDouble(data1.result.driverDetails.lon));
                        DropOffLatLng = new LatLng(Double.parseDouble(data1.result.lat), Double.parseDouble(data1.result.lon));


                        if (checkPermissions()) {
                            if (isLocationEnabled()) {
                                setCurrentLoc();
                            } else {
                                Toast.makeText(TrackAct.this, "Turn on location", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        } else {
                            requestPermissions();
                        }

                        if (data1.result.status.equals("Way")) {
                            DriverOnWayDialog();
                        }
                        else   if (data1.result.status.equals("Arrived")) {
                            DriverArriveDialog();
                        }
                        else if (data1.result.status.equals("send_request"))
                            EstimateConfirmDialog(data1,totalAmount+"");

                        else if (data1.result.status.equals("Start")) {
                            TripStartDialog();
                        } else if (data1.result.status.equals("Finish")) {
                            booking_image = data1.result.bookingImage;
                            // TripFinishDialog();
                            showWorkImageDialog(data1);
                        }

                    } else if (data1.status.equals("0")) {
                        App.showToast(TrackAct.this, data1.message, Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<BookingDetailModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void DriverOnWayDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(TrackAct.this);
        builder1.setMessage(getResources().getString(R.string.provider_is_on_the_way));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        alert33 = builder1.create();
        alert33.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getChatCount();
        callService();
        registerReceiver(LocationReceiver, new IntentFilter("data_update_location1"));
        registerReceiver(TripStatusReceiver, new IntentFilter("Job_Status_Action"));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(LocationReceiver);
        unregisterReceiver(TripStatusReceiver);
        stopService(new Intent(TrackAct.this, UpdateLocationService.class));
    }


    private void callService() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                startService(new Intent(TrackAct.this, UpdateLocationService.class));
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void DrawPolyLine() {
        DrawPollyLine.get(this).setOrigin(PickUpLatLng)
                .setDestination(DropOffLatLng).execute(new DrawPollyLine.onPolyLineResponse() {
            @Override
            public void Success(ArrayList<LatLng> latLngs) {
                mMap.clear();
                polineLanLongLine.clear();
                polineLanLongLine = latLngs;
                lineOptions = new PolylineOptions();
                lineOptions.addAll(latLngs);
                lineOptions.width(10);
                lineOptions.geodesic(true);
                lineOptions.color(R.color.black);
                AddDefaultMarker();
                prelatLng = null;
                AddCarMarker(carLatLng);
            }
        });
    }

    public void AddDefaultMarker() {
        if (mMap != null) {
            mMap.clear();
            if (lineOptions != null)
                mMap.addPolyline(lineOptions);
            if (PickUpLatLng != null) {
                PicUpMarker.position(PickUpLatLng);
                mMap.addMarker(PicUpMarker);
                // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(PickUpLatLng)));
            }
            if (DropOffLatLng != null) {
                DropOffMarker.position(DropOffLatLng);
                mMap.addMarker(DropOffMarker);
            }
        }
    }

    public void AddCarMarker(LatLng latLng) {
        // if (carMarker != null) carMarker.remove();
        try {
            if (prelatLng == null) {
                carMarker1.position(latLng);
                carMarker = mMap.addMarker(carMarker1);
                prelatLng = latLng;
            } else {
                if (prelatLng != latLng) {
                    Log.e("locationChange====", latLng + "");
                    Location temp = new Location(LocationManager.GPS_PROVIDER);
                    temp.setLatitude(latLng.latitude);
                    temp.setLongitude(latLng.longitude);
                    moveVechile(carMarker, temp);
                    rotateMarker(carMarker, temp.getBearing(), start_rotation);
                    prelatLng = latLng;

                }
            }
            animateCamera(carLatLng);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getChatCount() {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(TrackAct.this).result.id);
        Log.e("MapMap", "CHAT COUNT REQUEST" + map);
        Call<Map<String, String>> chatCount = apiInterface.getChatCount(map);
        chatCount.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "CHAT COUNT RESPONSE" + dataResponse);
                        if (data.get("count").equals("0")) {
                            binding.tvCounter.setVisibility(View.GONE);
                        } else {
                            binding.tvCounter.setVisibility(View.VISIBLE);
                            binding.tvCounter.setText(data.get("count"));
                        }

                    } else if (data.get("status").equals("0")) {
                        binding.tvCounter.setVisibility(View.GONE);
                        // Toast.makeText(TrackingActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }


    public void moveVechile(final Marker myMarker, final Location finalPosition) {

        final LatLng startPosition = myMarker.getPosition();

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final float durationInMs = 3000;
        final boolean hideMarker = false;

        handler.post(new Runnable() {
            long elapsed;
            float t;
            float v;

            @Override
            public void run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMs;
                v = interpolator.getInterpolation(t);

                LatLng currentPosition = new LatLng(
                        startPosition.latitude * (1 - t) + (finalPosition.getLatitude()) * t,
                        startPosition.longitude * (1 - t) + (finalPosition.getLongitude()) * t);
                myMarker.setPosition(currentPosition);
                // myMarker.setRotation(finalPosition.getBearing());


                // Repeat till progress is completeelse
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                    // handler.postDelayed(this, 100);
                } else {
                    if (hideMarker) {
                        myMarker.setVisible(false);
                    } else {
                        myMarker.setVisible(true);
                    }
                }
            }
        });


    }


    public void rotateMarker(final Marker marker, final float toRotation, final float st) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final float startRotation = marker.getRotation();
        final long duration = 1555;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                float rot = t * toRotation + (1 - t) * startRotation;


                marker.setRotation(-rot > 180 ? rot / 2 : rot);
                start_rotation = -rot > 180 ? rot / 2 : rot;
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void showWorkImageDialog(BookingDetailModel data1) {
        Dialog dialog = new Dialog(TrackAct.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_work_image);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.dmap);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions().position(DropOffLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_marker)));
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(DropOffLatLng)));
            }
        });

        TextView tvAddress = dialog.findViewById(R.id.tvAddress);
        TextView tvProName = dialog.findViewById(R.id.tvProName);
        ImageView ivWorkImage = dialog.findViewById(R.id.ivWorkImage);
        RelativeLayout btnRate = dialog.findViewById(R.id.btnRate);

        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrackAct.this, PaymentSummaryAct.class).putExtra("request_id", data1.result.id)
                        .putExtra("ProviderId",DriverId).putExtra("ProviderName",DriverName).putExtra("ProviderImage",image));
                finish();
            }
        });

        Glide.with(getApplicationContext())
                .load(booking_image)
                .apply(new RequestOptions().placeholder(R.drawable.user_default1))
                .override(300, 300)
                .into(ivWorkImage);
        tvAddress.setText(ServiceAddress);
        tvProName.setText(DriverName + "'s Completed Service");


        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        dialog.show();

    }



    private void estimatePriceMethod(String status) {
        Map<String, String> map = new HashMap<>();
        map.put("request_id", request_id);
        map.put("status", status);
        Log.e(TAG, "Estimate Price REQUEST" + map);
        Call<Map<String, String>> chatCount = apiInterface.estimateMethod(map);
        chatCount.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String, String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "Estimate Price RESPONSE" + dataResponse);
                        getBookingDetail(request_id);

                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(TrackAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    public void EstimateConfirmDialog(BookingDetailModel bookingDetailModel,String amounttt) {
       AlertDialog.Builder builder1 = new AlertDialog.Builder(TrackAct.this);

        builder1.setMessage(getResources().getString(R.string.are_you_satisfy_with));
        builder1.setTitle("€"+amounttt);

        builder1.setCancelable(false);

        builder1.setPositiveButton(
                getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (NetworkReceiver.isConnected()) estimatePriceMethod("accept_request");
                        else App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);
                    }
                });

        builder1.setNegativeButton(
                getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        if (NetworkReceiver.isConnected()) estimatePriceMethod("cancel_request");
                        else App.showToast(TrackAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);
                    }
                });

        alert33 = builder1.create();
        alert33.show();
    }
}
