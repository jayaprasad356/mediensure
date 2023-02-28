package com.graymatter.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class OverViewActivity extends AppCompatActivity {

    LinearLayout llDentalNetwork,llPharmacyNetwork,llLabNetwork,llOpdNetwork,llRadiologyNetwork;
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
        llRadiologyNetwork = findViewById(R.id.llRadiologyNetwork);

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

        llRadiologyNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(OverViewActivity.this, RadiologyNetwork.class));

            }
        });
    }
}