package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterProvider;
import com.verycycle.databinding.ActivityProviderListBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.CycleModel;
import com.verycycle.model.ProviderModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderListAct extends AppCompatActivity implements OnItemPositionListener {
    public String TAG =  "ProviderListAct";
    ActivityProviderListBinding binding;
    String str_image_path = "",cycleId="",problem="",repair_image_path="",date="",time="",address="",lat="",lon="",providerId="",serviceType="";
    VeryCycleUserInterface apiInterface;
    ArrayList<ProviderModel.Result>arrayList;
    AdapterProvider adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_provider_list);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        adapter = new AdapterProvider(ProviderListAct.this,arrayList,ProviderListAct.this);
        binding.rvProvider.setAdapter(adapter);

        if(getIntent()!=null){
            cycleId = getIntent().getStringExtra("cycleModel");
            str_image_path = getIntent().getStringExtra("cycleImage");
            problem = getIntent().getStringExtra("problem");
            repair_image_path = getIntent().getStringExtra("repairImage");
            date = getIntent().getStringExtra("date");
            time = getIntent().getStringExtra("time");
            address = getIntent().getStringExtra("address");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");
            serviceType = getIntent().getStringExtra("serviceType");
            if(NetworkReceiver.isConnected()) providerList();
            else App.showToast(ProviderListAct.this,getString(R.string.network_failure),Toast.LENGTH_LONG);
        }

        binding.btnBooking.setOnClickListener(v -> {
            if(providerId.equals(""))
                App.showToast(ProviderListAct.this,getString(R.string.please_select_provider),Toast.LENGTH_LONG);
              else    if(NetworkReceiver.isConnected()) sendBookingRequest(providerId);
            else App.showToast(ProviderListAct.this,getString(R.string.network_failure),Toast.LENGTH_LONG);

        });
    }

    public void providerList(){
        DataManager.getInstance().showProgressMessage(ProviderListAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("lat",lat);
        map.put("lon",lon);
        Log.e(TAG, "Provider List Request :" + map);
            Call<ProviderModel> signupCall = apiInterface.getNearByProvider(map);
            signupCall.enqueue(new Callback<ProviderModel>() {
                @Override
                public void onResponse(Call<ProviderModel> call, Response<ProviderModel> response) {
                    DataManager.getInstance().hideProgressMessage();
                    try {
                        ProviderModel data = response.body();
                        String responseString = new Gson().toJson(response.body());
                        Log.e(TAG, "Provider List Response :" + responseString);
                        if (data.status.equals("1")) {
                            binding.tvNotFound.setVisibility(View.GONE);
                            arrayList.clear();
                            arrayList.addAll(data.result);
                            adapter.notifyDataSetChanged();

                        } else if (data.status.equals("0")) {
                            arrayList.clear();
                            adapter.notifyDataSetChanged();
                            binding.tvNotFound.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ProviderModel> call, Throwable t) {
                    DataManager.getInstance().hideProgressMessage();
                    call.cancel();
                }
            });
        }

    @Override
    public void onPosition(int position) {
       providerId = arrayList.get(position).id ;

    }

    private void sendBookingRequest(String providerId) {
        DataManager.getInstance().showProgressMessage(ProviderListAct.this, getString(R.string.please_wait));
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
        RequestBody datE = RequestBody.create(MediaType.parse("text/plain"), date);
        RequestBody timE = RequestBody.create(MediaType.parse("text/plain"), time);
        RequestBody addreSS = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), lat);
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), lon);
        RequestBody provider_id = RequestBody.create(MediaType.parse("text/plain"), providerId);
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(ProviderListAct.this).result.id);
        RequestBody serviceType1 = RequestBody.create(MediaType.parse("text/plain"), serviceType);
        RequestBody amount = RequestBody.create(MediaType.parse("text/plain"), SessionManager.readString(ProviderListAct.this,"price",""));
        RequestBody vat_amount = RequestBody.create(MediaType.parse("text/plain"),""  );
        RequestBody vat_amount1 = RequestBody.create(MediaType.parse("text/plain"),""  );



        Call<Map<String,String>> signupCall = apiInterface.sendRequest(cycle_id, problm,vat_amount1, datE, timE, addreSS,latitude,longitude,user_id, provider_id,serviceType1,amount,vat_amount,filePart,filePart1);
        signupCall.enqueue(new Callback<Map<String,String>>() {
            @Override
            public void onResponse(Call<Map<String,String>> call, Response<Map<String,String>> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    Map<String,String> data = response.body();
                    if (data.get("status").equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        Toast.makeText(ProviderListAct.this, getString(R.string.request_send_successfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProviderListAct.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if (data.get("status").equals("0")) {
                        Toast.makeText(ProviderListAct.this, data.get("message"), Toast.LENGTH_SHORT).show();
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

