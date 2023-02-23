package com.example.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mediensure.helper.ApiConfig;
import com.example.mediensure.helper.Constant;
import com.example.mediensure.helper.Session;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    MaterialButton btnSignUp;
    TextView tvSignIn ;
    Activity activity ;
    Session session;

    EditText etname,etemail,etmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        activity = RegisterActivity.this;
        session = new Session(activity);

        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);

        etname = findViewById(R.id.etname);
        etemail = findViewById(R.id.etemail);
        etmobile = findViewById(R.id.etmobile);

        btnSignUp.setOnClickListener(v -> {

         //mobile length should be 10 and mail should be valid and name should not be empty

            if (etname.getText().toString().isEmpty()) {
                etname.setError("Enter Name");
                etname.requestFocus();
            } else if (etemail.getText().toString().isEmpty()) {
                etemail.setError("Enter Email");
                etemail.requestFocus();
            } else if (etmobile.getText().toString().isEmpty()) {
                etmobile.setError("Enter Mobile");
                etmobile.requestFocus();
            } else if (etmobile.getText().toString().length() != 10) {
                etmobile.setError("Enter Valid Mobile");
                etmobile.requestFocus();
            } else {

                //register user
                register_user() ;
            }


        });




        tvSignIn.setOnClickListener(v -> {

            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        });

    }

    private void register_user() {


        //api

        Map<String, String> params = new HashMap<>();
        params.put(Constant.MOBILE,etmobile.getText().toString().trim());
        params.put(Constant.EMAIL,etemail.getText().toString().trim());
        params.put(Constant.NAME,etname.getText().toString().trim());
        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("LOGIN_RES",response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);
                        session.setBoolean("is_logged_in", true);
                        session.setData(Constant.ID,jsonArray.getJSONObject(0).getString(Constant.ID));
                        session.setData(Constant.NAME,jsonArray.getJSONObject(0).getString(Constant.NAME));
                        session.setData(Constant.MOBILE,jsonArray.getJSONObject(0).getString(Constant.MOBILE));
                        session.setData(Constant.EMAIL,jsonArray.getJSONObject(0).getString(Constant.EMAIL));
                        startActivity(new Intent(activity, LoginActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, activity, Constant.SIGN_UP, params,true);




    }
}