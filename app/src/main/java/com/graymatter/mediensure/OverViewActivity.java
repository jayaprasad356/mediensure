package com.graymatter.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class OverViewActivity extends AppCompatActivity {

    LinearLayout llDentalNetwork,llPharmacyNetwork,llLabNetwork,llOpdNetwork;
    ImageButton ibBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_view);

        llOpdNetwork = findViewById(R.id.llOpdNetwork);
        llLabNetwork = findViewById(R.id.llLabNetwork);
        llPharmacyNetwork = findViewById(R.id.llPharmacyNetwork);
        llDentalNetwork = findViewById(R.id.llDentalNetwork);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(v -> {
      onBackPressed();
        });

        llOpdNetwork.setOnClickListener(v -> {
            Intent intent = new Intent(OverViewActivity.this, OPDActivity.class);
            startActivity(intent);

        });

        llLabNetwork.setOnClickListener(v -> {
            startActivity(new Intent(OverViewActivity.this, LabNetworkActivity.class));

        });

        llPharmacyNetwork.setOnClickListener(v -> {
            startActivity(new Intent(OverViewActivity.this, PharmacyNetworkActivity.class));

        });

        llDentalNetwork.setOnClickListener(v -> {
            startActivity(new Intent(OverViewActivity.this, DentalProviderNetworkActivity.class));

        });
    }
}