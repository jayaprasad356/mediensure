package com.graymatter.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.graymatter.mediensure.helper.ApiConfig;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.LocationTrack;
import com.graymatter.mediensure.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditOPDActivity extends AppCompatActivity {

    EditText etaddress, etemail, etmobile,etOtp;
    Spinner spinner;
    Activity activity;
    Session session;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    Button btnPickLocation, btnAdd,btnSendOTP;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String TAG = "OTPACT";
    private String mVerificationId = "";
    RadioGroup rgLabService,rgRadiology;




    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    RelativeLayout rlAddImage;
    private ImageView imageView;

    ImageButton ibBack;
    String Radiology="yes";
    String Lab="yes";
    public String inventory_id;
    public String user_id;
    public String name;
    public String address;
    public String email;
    public String mobile;
    public String latitude;
    public String longitude;
    public String datetime;
    public String remarks;
    public String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_opdactivity);

        activity = EditOPDActivity.this;
        session = new Session(activity);

        etaddress = findViewById(R.id.etAddress);
        etemail = findViewById(R.id.etemail);
        etmobile = findViewById(R.id.etMobile);
        spinner = findViewById(R.id.spinner);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        btnAdd = findViewById(R.id.btnAdd);
        rlAddImage = findViewById(R.id.rlAddImage);
        imageView = findViewById(R.id.imageView);
        rgLabService = findViewById(R.id.rbLabService);
        rgRadiology = findViewById(R.id.rgRadiology);

         inventory_id = getIntent().getStringExtra(Constant.ID);
         user_id = getIntent().getStringExtra(Constant.USER_ID);
         name = getIntent().getStringExtra(Constant.NAME);
         email = getIntent().getStringExtra(Constant.EMAIL);
         mobile = getIntent().getStringExtra(Constant.MOBILE);
         address = getIntent().getStringExtra(Constant.ADDRESS);
         latitude = getIntent().getStringExtra(Constant.LATITUDE);
         longitude = getIntent().getStringExtra(Constant.LONGITUDE);
         datetime = getIntent().getStringExtra(Constant.DATETIME);
         remarks = getIntent().getStringExtra(Constant.REMARKS);
         status = getIntent().getStringExtra(Constant.STATUS);

         if (name.equals("Clinic")){
             spinner.setSelection(0);
         }else {
             spinner.setSelection(1);
         }
         etaddress.setText(address);
         etemail.setText(email);
         etmobile.setText(mobile);
         etmobile.setEnabled(false);
         btnPickLocation.setEnabled(false);
        btnPickLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
         btnAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 updateOPD();
             }
         });

    }
    private void updateOPD() {

        //add data to database


        //api

        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.INVENTORY_ID, inventory_id);
        params.put(Constant.MOBILE, etmobile.getText().toString().trim());
        params.put(Constant.EMAIL, etemail.getText().toString().trim());
        params.put(Constant.ADDRESS, etaddress.getText().toString().trim());
        params.put(Constant.NAME, spinner.getSelectedItem().toString().trim());
        params.put(Constant.LATITUDE, String.valueOf(latitude));
        params.put(Constant.LONGITUDE, String.valueOf(longitude));
        params.put(Constant.IMAGE, String.valueOf(imageView));
        params.put(Constant.LAB_SERVICE, Lab.toString().trim());
        params.put(Constant.RADIOLOGY_SERVICE, Radiology.toString().trim());



        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("LOGIN_RES", response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);


                        startActivity(new Intent(activity, TabActivity.class));
                        finish();
                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.UPDATE_OPD_NETWORK, params, true);


    }
}