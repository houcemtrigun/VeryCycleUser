package com.verycycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ChoosingTypeOfride extends AppCompatActivity {

    private LinearLayout linear_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_type_ofride);

        SetuiUI();
    }

    private void SetuiUI() {

        linear_next = (LinearLayout) findViewById(R.id.linear_next);

        linear_next.setOnClickListener(v -> {
            startActivity(new Intent(this, ChhosingATypeOfRepair.class));
        });
    }
}