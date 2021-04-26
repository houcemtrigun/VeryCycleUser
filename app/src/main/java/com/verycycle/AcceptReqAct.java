package com.verycycle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterAcceptBooking;
import com.verycycle.databinding.ActivityAcceptReqBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.model.RequestModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptReqAct extends AppCompatActivity {
    public String TAG ="AcceptReqAct";
    ActivityAcceptReqBinding binding;
    AdapterAcceptBooking adapter;
    VeryCycleUserInterface apiInterface;
    ArrayList<RequestModel.Result> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_accept_req);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        adapter = new AdapterAcceptBooking(AcceptReqAct.this, arrayList);
        binding.rvAccept.setAdapter(adapter);

        binding.ivBack.setOnClickListener(v -> {finish();});
    }


    private void getAcceptRrquest() {
        DataManager.getInstance().showProgressMessage(AcceptReqAct.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",DataManager.getInstance().getUserData(AcceptReqAct.this).result.id);
        Log.e(TAG,"get Accept Request "+map);
        Call<RequestModel> loginCall = apiInterface.getAcceptReq(map);
        loginCall.enqueue(new Callback<RequestModel>() {
            @Override
            public void onResponse(Call<RequestModel> call, Response<RequestModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    RequestModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"get Accept Request Response :"+responseString);
                    if(data.status.equals("1")){
                        binding.tvNotFound.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    }
                    else if(data.status.equals("0")){
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RequestModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkReceiver.isConnected())  getAcceptRrquest();
        else App.showToast(AcceptReqAct.this,getString(R.string.network_failure), Toast.LENGTH_LONG);
    }
}
