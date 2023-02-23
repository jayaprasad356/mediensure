package com.example.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mediensure.helper.Constant;
import com.example.mediensure.helper.Session;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {


    MaterialCardView cvProfile,cvMenu;
    Button btnAddNewInventory;
    TextView tvName;
    Activity activity;
    Session session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activity = this;
        session = new Session(this);

        cvProfile = findViewById(R.id.cvProfile);
        btnAddNewInventory = findViewById(R.id.btnAddNewInventory);
        tvName = findViewById(R.id.tvName);


        tvName.setText("Hi, "+session.getData(Constant.NAME));


        cvProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnAddNewInventory.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, OverViewActivity.class);
            startActivity(intent);
        });

    }
}