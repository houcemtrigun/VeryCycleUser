package com.verycycle.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.verycycle.R;
import com.verycycle.adapter.AdapterComplete;
import com.verycycle.databinding.FragmentCompleteBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.model.HistoryModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedFragment extends Fragment {
    public String TAG = "CompletedFragment";
    VeryCycleUserInterface apiInterface;
    FragmentCompleteBinding binding;
    AdapterComplete adapter;
    ArrayList<HistoryModel.Result>arrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complete,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        arrayList = new ArrayList<>();
        adapter = new AdapterComplete(getActivity(),arrayList);
        binding.rvReservation.setAdapter(adapter);

        if(NetworkReceiver.isConnected()) getHistory();
        else  App.showToast(getContext(),getString(R.string.network_failure), Toast.LENGTH_LONG);
    }

    private void getHistory() {
        DataManager.getInstance().showProgressMessage(getActivity(),getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).result.id);
        map.put("status", "Finish");
        Log.e(TAG, "Get History Complete REQUEST" + map);
        Call<HistoryModel> chatCount = apiInterface.getMyHistorty(map);
        chatCount.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    HistoryModel data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "Get History Complete RESPONSE" + dataResponse);
                    if (data.status.equals("1")) {
                        binding.tvNotavl.setVisibility(View.GONE);
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();
                        binding.tvNotavl.setVisibility(View.VISIBLE);
                        // Toast.makeText(TrackingActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }

        });
    }
}
