package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterSubProblem;
import com.verycycle.adapter.ServicePriceAdapter;
import com.verycycle.adapter.ServicesAdapter;
import com.verycycle.databinding.ActivityShowServicesBinding;
import com.verycycle.databinding.ActivitySubCatBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.ServicesModel;
import com.verycycle.model.SubProblmModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowServicesAct extends AppCompatActivity implements OnItemPositionListener {
    public String TAG = "ShowServicesAct";
    ActivityShowServicesBinding binding;
    VeryCycleUserInterface apiInterface;
    ArrayList<ServicesModel.Result> arrayList;
    ServicesAdapter adapter;
    String serviceName = "",serviceId="",subproblemId="",title="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_show_services);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        binding.ivBack.setOnClickListener(v -> finish());

        if(getIntent()!=null) {
            subproblemId = getIntent().getStringExtra("subproblem_id");
            title = getIntent().getStringExtra("title");

        }
        adapter = new ServicesAdapter(ShowServicesAct.this, arrayList, ShowServicesAct.this);
        binding.rvSubProblem.setAdapter(adapter);


        binding.tvTitle.setText(title);
        if (NetworkReceiver.isConnected()) getAllServicess(subproblemId);
        else App.showToast(ShowServicesAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);
    }



    public void getAllServicess(String id) {
        DataManager.getInstance().showProgressMessage(ShowServicesAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("problem_id",id );
        Log.e(TAG, "GET All Services REQUEST" + map);
        Call<ServicesModel> loginCall = apiInterface.getServices(map);
        loginCall.enqueue(new Callback<ServicesModel>() {
            @Override
            public void onResponse(Call<ServicesModel> call, Response<ServicesModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    ServicesModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "GET All Services  Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        App.showToast(ShowServicesAct.this, data.message, Toast.LENGTH_SHORT);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ServicesModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    public void onPosition(int position) {
        serviceName = arrayList.get(position).nameFr;
        serviceId = arrayList.get(position).id;
        SessionManager.writeString(ShowServicesAct.this,"service_name",serviceName);
        SessionManager.writeString(ShowServicesAct.this,"service_id",arrayList.get(position).id);
        startActivity(new Intent(ShowServicesAct.this, ServicePriceAct.class)
                .putExtra("service_id",arrayList.get(position).id)
                .putExtra("title",serviceName));
      finish();

    }
}
