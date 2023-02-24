package com.graymatter.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.graymatter.mediensure.helper.ApiConfig;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.Session;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {


    MaterialCardView cvProfile,cvMenu;
    Button btnAddNewInventory;
    TextView tvName,tvTotalInventory,tvTodayInventory;
    Activity activity;
    Session session;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activity = this;
        session = new Session(this);

        user_detail();
        cvProfile = findViewById(R.id.cvProfile);
        btnAddNewInventory = findViewById(R.id.btnAddNewInventory);
        tvName = findViewById(R.id.tvName);
        tvTotalInventory = findViewById(R.id.tvTotalInventory);
        tvTodayInventory = findViewById(R.id.tvTodayInventory);


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


    private void user_detail() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));



        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("Total", response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {



                        tvTodayInventory.setText("Today - "+jsonObject.getString(Constant.TODAY_INVENTORIES));
                        tvTotalInventory.setText(jsonObject.getString(Constant.TOTAL_INVENTORIES));

//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//
//
////
////                            session.setData(Constant.ID,jsonArray.getJSONObject(0).getString(Constant.ID));
////                            session.setData(Constant.NAME,jsonArray.getJSONObject(0).getString(Constant.NAME));
////                            session.setData(Constant.MOBILE,jsonArray.getJSONObject(0).getString(Constant.MOBILE));
////                            session.setData(Constant.EMAIL,jsonArray.getJSONObject(0).getString(Constant.EMAIL));
//
//                        }

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