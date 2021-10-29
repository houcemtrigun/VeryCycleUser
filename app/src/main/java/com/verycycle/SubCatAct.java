package com.verycycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterProblem;
import com.verycycle.adapter.AdapterSubProblem;
import com.verycycle.databinding.ActivitySubCatBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.ProblemModel;
import com.verycycle.model.SubProblmModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCatAct extends AppCompatActivity implements OnItemPositionListener {
    public String TAG = "SubCatAct";
    ActivitySubCatBinding binding;
    VeryCycleUserInterface apiInterface;
    ArrayList<SubProblmModel.Result> arrayList;
    AdapterSubProblem adapter;
    String problem = "",problmId="",price="",title="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sub_cat);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();


        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnContinue.setOnClickListener(v -> {
            if (problem.equals("")) {
                App.showToast(SubCatAct.this, getString(R.string.please_select_problem), Toast.LENGTH_LONG);
            } else {
                SessionManager.writeString(SubCatAct.this,"price",price);
                Problam.tvPrice.setText(getString(R.string.continue_) + "  " + "€" + price);
                finish();
            }
        });

        adapter = new AdapterSubProblem(SubCatAct.this, arrayList, SubCatAct.this);
        binding.rvSubProblem.setAdapter(adapter);

       if(getIntent()!=null) {
           problmId = getIntent().getStringExtra("problem_id");
           title = getIntent().getStringExtra("title");
       }
       binding.tvTitle.setText(title);
       if (NetworkReceiver.isConnected()) getAllSubProblem(problmId);
       else App.showToast(SubCatAct.this, getString(R.string.network_failure), Toast.LENGTH_LONG);

    }

    public void getAllSubProblem(String id) {
        DataManager.getInstance().showProgressMessage(SubCatAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("problem_details_id",id );
        Log.e(TAG, "GET Sub Problem List REQUEST" + map);
        Call<SubProblmModel> loginCall = apiInterface.getSubProblemList(map);
        loginCall.enqueue(new Callback<SubProblmModel>() {
            @Override
            public void onResponse(Call<SubProblmModel> call, Response<SubProblmModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SubProblmModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Sub Problem List  Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        App.showToast(SubCatAct.this, data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SubProblmModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onPosition(int position) {
        problem = arrayList.get(position).name;
        price = arrayList.get(position).price;
        SessionManager.writeString(SubCatAct.this,"sub_problem",problem);
        SessionManager.writeString(SubCatAct.this,"subproblem_id",arrayList.get(position).id);

    }
}
