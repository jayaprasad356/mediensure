package com.example.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mediensure.helper.ApiConfig;
import com.example.mediensure.helper.Constant;
import com.example.mediensure.helper.Session;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    ImageButton ibBack;
    FloatingActionButton fab;
    Activity activity;
    Session session;
    EditText etName,etEmail,etMobile;
    Button btnUpdate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = this;
        session = new Session(this);

        ibBack = findViewById(R.id.ibBack);
        fab = findViewById(R.id.fab);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(v -> {
            Map<String, String> params = new HashMap<>();
            params.put(Constant.USER_ID, session.getData(Constant.ID));
            params.put(Constant.NAME, etName.getText().toString());
            params.put(Constant.EMAIL, etEmail.getText().toString());
            params.put(Constant.MOBILE, etMobile.getText().toString());


            ApiConfig.RequestToVolley((result, response) -> {

                if (result) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean(Constant.SUCCESS)) {


                            Intent intent = new Intent(activity, HomeActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, activity, Constant.UPDATE_PROFILE, params, false);
        });

        fab.setOnClickListener(v -> {
            session.logoutUser(activity);
            Intent intent  = new Intent(activity,LoginActivity.class);
            startActivity(intent);
        });

        ibBack.setOnClickListener(v -> {
            onBackPressed();
        });

        user_detail();





    }

    private void user_detail() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));



        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("LOGIN_RES", response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            etName.setText(object.getString(Constant.NAME));
                            etEmail.setText(object.getString(Constant.EMAIL));
                            etMobile.setText(object.getString(Constant.MOBILE));

                            session.setData(Constant.ID,jsonArray.getJSONObject(0).getString(Constant.ID));
                            session.setData(Constant.NAME,jsonArray.getJSONObject(0).getString(Constant.NAME));
                            session.setData(Constant.MOBILE,jsonArray.getJSONObject(0).getString(Constant.MOBILE));
                            session.setData(Constant.EMAIL,jsonArray.getJSONObject(0).getString(Constant.EMAIL));

                        }

                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.USERDETAILS, params, true);
    }
}