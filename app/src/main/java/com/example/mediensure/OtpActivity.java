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

public class OtpActivity extends AppCompatActivity {

    Button btnContinue;
    TextView tvSignIn,tvMobile;
    Activity activity;
    Session session;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        activity = OtpActivity.this;
        session = new Session(activity);


        btnContinue = findViewById(R.id.btnContinue);
        tvMobile = findViewById(R.id.tvMobile);

        tvMobile.setText("+91 "+session.getData(Constant.MOBILE));


        btnContinue.setOnClickListener(v -> {

            Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
            startActivity(intent);

        });


    }
}