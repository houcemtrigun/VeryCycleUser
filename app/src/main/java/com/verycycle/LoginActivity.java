package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.verycycle.databinding.ActivityLoginBinding;
import com.verycycle.helper.App;
import com.verycycle.helper.DataManager;
import com.verycycle.helper.NetworkReceiver;
import com.verycycle.helper.SessionManager;
import com.verycycle.model.SignupModel;
import com.verycycle.retrofit.ApiClient;
import com.verycycle.retrofit.Constant;
import com.verycycle.retrofit.VeryCycleUserInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.verycycle.retrofit.Constant.emailPattern;

public class LoginActivity extends AppCompatActivity {
    public String TAG = "LoginActivity";
    ActivityLoginBinding binding;
    VeryCycleUserInterface apiInterface;
    String refreshedToken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiInterface = ApiClient.getClient().create(VeryCycleUserInterface.class);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);

        SetupUI();
    }

    private void SetupUI() {
        binding.loginBtn.setOnClickListener(v -> {
            validation();
        });

        binding.singup.setOnClickListener(v -> {
            startActivity(new Intent(this,Register.class));
        });

        binding.btnForgotPass.setOnClickListener(v -> {
            startActivity(new Intent(this,ForgotPasswordAct.class));
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            try {
                refreshedToken = instanceIdResult.getToken();
                Log.e("Token===",refreshedToken);
                // Yay.. we have our new token now.
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        binding.btnEnglish.setOnClickListener(v -> {
            DataManager.updateResources(LoginActivity.this,"en");
            SessionManager.writeString(LoginActivity.this, Constant.LANGUAGE,"en");
            resetartActivity();
        });

        binding.btnFranch.setOnClickListener(v -> {
            DataManager.updateResources(LoginActivity.this,"fr");
            SessionManager.writeString(LoginActivity.this,Constant.LANGUAGE,"fr");
            resetartActivity();
        });

    }

    private void validation() {
        if (binding.etEmail.getText().toString().equals("")) {
            binding.etEmail.setError(getString(R.string.please_enter_email));
            binding.etEmail.setFocusable(true);
        } else if (!binding.etEmail.getText().toString().matches(emailPattern)) {
            binding.etEmail.setError(getString(R.string.wrong_email));
            binding.etEmail.setFocusable(true);
        }
        else if (binding.etPass.getText().toString().equals("")) {
            binding.etPass.setError(getString(R.string.please_enter_pass));
            binding.etPass.setFocusable(true);
        }
        else {
            if (NetworkReceiver.isConnected()) {
                userLogin();
            } else {
                Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();
            }


        }
    }




    public void userLogin(){
        DataManager.getInstance().showProgressMessage(LoginActivity.this,getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",binding.etEmail.getText().toString());
        map.put("password",binding.etPass.getText().toString());
        map.put("type","User");
        map.put("register_id",refreshedToken);
        Log.e(TAG,"Login Request "+map);
        Call<SignupModel> loginCall = apiInterface.userLogin(map);
        loginCall.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SignupModel data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Login Response :"+responseString);
                    if(data.status.equals("1")){
                        SessionManager.writeString(LoginActivity.this, Constant.USER_INFO, responseString);
                         Toast.makeText(LoginActivity.this, getString(R.string.sign_in_successful), Toast.LENGTH_SHORT).show();
                         startActivity(new Intent(LoginActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                         finish();
                    }
                    else if(data.status.equals("0")){
                        App.showToast(LoginActivity.this,data.message, Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

    private void resetartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}