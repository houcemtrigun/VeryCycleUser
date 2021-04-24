package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.verycycle.databinding.ActivityProblamBinding;
import com.verycycle.helper.App;

public class Problam extends AppCompatActivity {
  ActivityProblamBinding binding;
  String cycleId="",str_image_path,problem="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(this,R.layout.activity_problam);
        SetUpUI();
    }

    private void SetUpUI() {
        if(getIntent()!=null){
            cycleId = getIntent().getStringExtra("cycleModel");
            str_image_path = getIntent().getStringExtra("cycleImage");
        }

        binding.btnContinue.setOnClickListener(v -> {
            if(problem.equals("")){
                App.showToast(Problam.this,getString(R.string.please_select_problem), Toast.LENGTH_LONG);
            }
            else {
                startActivity(new Intent(Problam.this, ChhosingATypeOfRepair.class).putExtra("cycleModel",cycleId)
                        .putExtra("cycleImage",str_image_path).putExtra("problem",problem));
            }
        });

        binding.tv1.setOnClickListener(v -> {SelectedProb(1);});
        binding.tv2.setOnClickListener(v -> {SelectedProb(2);});
        binding.tv3.setOnClickListener(v -> {SelectedProb(3);});
        binding.tv4.setOnClickListener(v -> {SelectedProb(4);});
        binding.tvOther.setOnClickListener(v -> {SelectedProb(5);});
    }

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
}