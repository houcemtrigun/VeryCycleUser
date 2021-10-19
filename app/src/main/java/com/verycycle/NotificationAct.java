package com.verycycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterComplete;
import com.verycycle.adapter.AdapterNotification;
import com.verycycle.databinding.ActivityNotificationBinding;
import com.verycycle.databinding.FragmentCompleteBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.model.HistoryModel;
import com.verycycle.model.NotificationModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAct extends AppCompatActivity {
    public String TAG = "NotificationAct";
    VeryCycleUserInterface apiInterface;
    ActivityNotificationBinding binding;
    AdapterNotification adapter;
    ArrayList<NotificationModel.Result> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding =    DataBindingUtil.setContentView(this,R.layout.activity_notification);
        initViews();
    }

    private void initViews() {
        binding.ivBack.setOnClickListener(v -> finish());

        arrayList = new ArrayList<>();
        adapter = new AdapterNotification(NotificationAct.this,arrayList);
        binding.rvNotification.setAdapter(adapter);

        if(NetworkReceiver.isConnected()) getAllNoti();
        else  App.showToast(NotificationAct.this,getString(R.string.network_failure), Toast.LENGTH_LONG);
    }



    private void getAllNoti() {
        DataManager.getInstance().showProgressMessage(NotificationAct.this,getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("provider_id", DataManager.getInstance().getUserData(NotificationAct.this).result.id);
        map.put("status", "USER");
        Log.e(TAG, "Get All Notification REQUEST" + map);
        Call<NotificationModel> chatCount = apiInterface.getNotification(map);
        chatCount.enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(Call<NotificationModel> call, Response<NotificationModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    NotificationModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e(TAG, "Get All Notification RESPONSE" + dataResponse);
                    if(data.result.size()>0){
                        binding.tvNotavl.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotavl.setVisibility(View.VISIBLE);
                    }

                   /* if (data.status.equals("1")) {
                        binding.tvNotavl.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotavl.setVisibility(View.VISIBLE);
                        // Toast.makeText(TrackingActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<NotificationModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }
}
