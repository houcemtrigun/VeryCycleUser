package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ChhosingATypeOfRepair extends AppCompatActivity {

    private LinearLayout email_li;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chhosing_a_type_of_repair);
        SetuUI();
    }

    private void SetuUI() {

        email_li = (LinearLayout) findViewById(R.id.email_li);
        email_li.setOnClickListener(v -> {
            startActivity(new Intent(this,Payment.class));
        });
    }
}