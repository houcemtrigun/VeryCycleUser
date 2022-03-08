package com.verycycle;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterSubProblem;
import com.verycycle.adapter.ServicePriceAdapter;
import com.verycycle.databinding.ActivityServicePriceBinding;
import com.verycycle.databinding.ActivityShowServicesBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.listener.OnItemPositionListener2;
import com.verycycle.model.ServicesModel;
import com.verycycle.model.ServicesPriceModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicePriceAct extends AppCompatActivity implements OnItemPositionListener2 {
    public String TAG = "ServicePriceAct";
    ActivityServicePriceBinding binding;
    VeryCycleUserInterface apiInterface;
    ArrayList<ServicesPriceModel.Result> arrayList;
    ServicePriceAdapter adapter;
    String serviceName = "",serviceId="",price="",priceId="",title="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_service_price);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();


        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnContinue1.setOnClickListener(v -> {
           // if (price.equals("")) {
            //   App.showToast(ServicePriceAct.this, getString(R.string.please_select_problem), Toast.LENGTH_LONG);
           // } else {
                SessionManager.writeString(ServicePriceAct.this,"price","");
                startActivity(new Intent(ServicePriceAct.this, ChhosingATypeOfRepair.class));
               // Problam.tvPrice.setText(getString(R.string.continue_) + "  " + price);
              //  finish();
         //   }
        });

        adapter = new ServicePriceAdapter(ServicePriceAct.this, arrayList, ServicePriceAct.this);
        binding.rvSubProblem.setAdapter(adapter);

        if(getIntent()!=null) {
            serviceId = getIntent().getStringExtra("service_id");
            title = getIntent().getStringExtra("title");

        }
        binding.tvTitle.setText(title);
        if (NetworkReceiver.isConnected()) getServicePrice(serviceId);
        else App.showToast(ServicePriceAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);
    }


    public void getServicePrice(String id) {
        DataManager.getInstance().showProgressMessage(ServicePriceAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("service_id",id );
        Log.e(TAG, "GET Price Services REQUEST" + map);
        Call<ServicesPriceModel> loginCall = apiInterface.getServicesPrice(map);
        loginCall.enqueue(new Callback<ServicesPriceModel>() {
            @Override
            public void onResponse(Call<ServicesPriceModel> call, Response<ServicesPriceModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    ServicesPriceModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "GET Price Services  Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                        binding.tvDescription.setText(Html.fromHtml(data.descriptionFr)+"");
                    } else if (data.status.equals("0")) {
                       NotAvailable();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ServicesPriceModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void onPosition2(int position,String price) {
        this.price = price;
        priceId = arrayList.get(position).id;
       // binding.layoutPrice.setVisibility(View.VISIBLE);
      //  binding.tvPrice33.setText(price);
       // binding.tvService.setText(arrayList.get(position).priceForFr);

       // SessionManager.writeString(ServicePriceAct.this,"price",price);
        SessionManager.writeString(ServicePriceAct.this,"priceId",arrayList.get(position).id);
    }


    public void NotAvailable(){
        AlertDialog.Builder  builder1 = new AlertDialog.Builder(ServicePriceAct.this);
        builder1.setMessage(getResources().getString(R.string.service_not_available));
        builder1.setCancelable(false);

        builder1.setPositiveButton(
                getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });



        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
