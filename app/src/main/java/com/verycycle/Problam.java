package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.verycycle.adapter.AdapterProblem;
import com.verycycle.databinding.ActivityProblamBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.listener.OnItemPositionListener;
import com.verycycle.model.ProblemModel;
import com.verycycle.model.SignupModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.Constant;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Problam extends AppCompatActivity implements OnItemPositionListener {
    public String TAG = "Problam";
    ActivityProblamBinding binding;
    String cycleId = "", str_image_path, problem = "";
    VeryCycleUserInterface apiInterface;
    ArrayList<ProblemModel.Result> arrayList;
    AdapterProblem adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_problam);
        SetUpUI();
    }

    private void SetUpUI() {
        arrayList = new ArrayList<>();
        if (getIntent() != null) {
            cycleId = getIntent().getStringExtra("cycleModel");
            str_image_path = getIntent().getStringExtra("cycleImage");
        }

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnContinue.setOnClickListener(v -> {
            if (problem.equals("")) {
                App.showToast(Problam.this, getString(R.string.please_select_problem), Toast.LENGTH_LONG);
            } else {
                startActivity(new Intent(Problam.this, ChhosingATypeOfRepair.class).putExtra("cycleModel", cycleId)
                        .putExtra("cycleImage", str_image_path).putExtra("problem", problem));
            }
        });

        adapter = new AdapterProblem(Problam.this, arrayList, Problam.this);
        binding.rvProblem.setAdapter(adapter);

        if (NetworkReceiver.isConnected()) getAllProblem();
        else App.showToast(Problam.this, getString(R.string.network_failure), Toast.LENGTH_LONG);

     /*   binding.tv1.setOnClickListener(v -> {SelectedProb(1);});
        binding.tv2.setOnClickListener(v -> {SelectedProb(2);});
        binding.tv3.setOnClickListener(v -> {SelectedProb(3);});
        binding.tv4.setOnClickListener(v -> {SelectedProb(4);});
        binding.tvOther.setOnClickListener(v -> {SelectedProb(5);});
*/


    }

/*
    public void SelectedProb(int i){
       if(i==1){
           binding.tv1.setBackgroundResource(R.drawable.btn_bg_problem);
           binding.tv2.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv3.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv4.setBackgroundResource(R.drawable.btn_bg_);
           binding.tvOther.setBackgroundResource(R.drawable.btn_bg_);
           problem = binding.tv1.getText().toString();
       }
       else if(i==2){
           binding.tv1.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv2.setBackgroundResource(R.drawable.btn_bg_problem);
           binding.tv3.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv4.setBackgroundResource(R.drawable.btn_bg_);
           binding.tvOther.setBackgroundResource(R.drawable.btn_bg_);
           problem = binding.tv2.getText().toString();
        }
       else if(i==3){
           binding.tv1.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv2.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv3.setBackgroundResource(R.drawable.btn_bg_problem);
           binding.tv4.setBackgroundResource(R.drawable.btn_bg_);
           binding.tvOther.setBackgroundResource(R.drawable.btn_bg_);
           problem = binding.tv3.getText().toString();
       }
       else if(i==4){
           binding.tv1.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv2.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv3.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv4.setBackgroundResource(R.drawable.btn_bg_problem);
           binding.tvOther.setBackgroundResource(R.drawable.btn_bg_);
           problem = binding.tv4.getText().toString();
       }
       else if(i==5){
           binding.tv1.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv2.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv3.setBackgroundResource(R.drawable.btn_bg_);
           binding.tv4.setBackgroundResource(R.drawable.btn_bg_);
           binding.tvOther.setBackgroundResource(R.drawable.btn_bg_problem);
           problem = binding.tvOther.getText().toString();
       }
    }
*/


    public void getAllProblem() {
        DataManager.getInstance().showProgressMessage(Problam.this, getString(R.string.please_wait));
        Call<ProblemModel> loginCall = apiInterface.getProblemList();
        loginCall.enqueue(new Callback<ProblemModel>() {
            @Override
            public void onResponse(Call<ProblemModel> call, Response<ProblemModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    ProblemModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG, "Problem List  Response :" + responseString);
                    if (data.status.equals("1")) {
                        arrayList.clear();
                        arrayList.addAll(data.result);
                        adapter.notifyDataSetChanged();
                    } else if (data.status.equals("0")) {
                        App.showToast(Problam.this, data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ProblemModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void onPosition(int position) {
        problem = arrayList.get(position).name;
        binding.tvPrice.setText(getString(R.string.continue_) + "  " + "€" + arrayList.get(position).price);
        SessionManager.writeString(Problam.this,"price",arrayList.get(position).price);
    }
}