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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.graymatter.mediensure.helper.ApiConfig;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.LocationTrack;
import com.graymatter.mediensure.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditLabActivity extends AppCompatActivity {
    EditText etMobile, etEmail, etAddress, etCenterManager, etCenterOperationalHours, etCenterAddress, etOtp,etCenterName;
    Button btnAdd, btnPickLocation, btnSendOTP;
    ImageButton ibBack;

    Activity activity;
    Session session;
    private ImageView imageView;
    RadioGroup homeVisit, radiology;
    RadioButton homevisitYes,homeVisitNo, radiologyYes,radiologyNo;
    String homevisitData = "yes", radiologyData = "yes";

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    public String id;
    public String centerName;
    public String email;
    public String mobile;
    public String managerName;
    public String centerAddress;
    public String operationalHours;
    public String radiologyTest;
    public String HomeVisit;
    public String latitude;
    public String longitude;
    public String datetime;
    public String image;
    public String remarks;
    public String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lab);
        activity = EditLabActivity.this;
        session = new Session(activity);


        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etCenterManager = findViewById(R.id.etCenterManager);
        etCenterName=findViewById(R.id.etCenterName);
        etCenterOperationalHours = findViewById(R.id.etCenterOperationalHours);
        etCenterAddress = findViewById(R.id.etCenterAddress);
        btnAdd = findViewById(R.id.btnAdd);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        imageView = findViewById(R.id.imageView);
        ibBack = findViewById(R.id.ibBack);

        homeVisit = findViewById(R.id.rgHomeVisit);
        homevisitYes = findViewById(R.id.rbYes);
        homeVisitNo = findViewById(R.id.rbNo);

        radiology = findViewById(R.id.rgRadiology);
        radiologyYes = findViewById(R.id.rbyesradio);
        radiologyNo = findViewById(R.id.rbNoRadio);

        id = getIntent().getStringExtra(Constant.ID);
        centerName = getIntent().getStringExtra(Constant.CENTER_NAME);
        email = getIntent().getStringExtra(Constant.EMAIL);
        mobile = getIntent().getStringExtra(Constant.MOBILE);
        managerName = getIntent().getStringExtra(Constant.MANAGER_NAME);
        centerAddress = getIntent().getStringExtra(Constant.CENTER_ADDRESS);
        operationalHours = getIntent().getStringExtra(Constant.OPERATIONAL_HOURS);
        radiologyTest = getIntent().getStringExtra(Constant.RADIOLOGY_TEST);
        HomeVisit = getIntent().getStringExtra(Constant.HOME_VISIT);
        image = getIntent().getStringExtra(Constant.IMAGE);
        datetime = getIntent().getStringExtra(Constant.DATETIME);
        latitude = getIntent().getStringExtra(Constant.LATITUDE);
        longitude = getIntent().getStringExtra(Constant.LONGITUDE);
        status = getIntent().getStringExtra(Constant.STATUS);
        remarks = getIntent().getStringExtra(Constant.REMARKS);
        etCenterName.setText(centerName);
        etEmail.setText(email);
        etMobile.setText(mobile);
        etMobile.setEnabled(false);
        etCenterManager.setText(managerName);
        etCenterAddress.setText(centerAddress);
        etCenterOperationalHours.setText(operationalHours);
        btnPickLocation.setEnabled(false);
        btnPickLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);
        homeVisit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                String radioButtonText = checkedRadioButton.getText().toString();
                // Toast.makeText(getApplicationContext(), "Selected: " + radioButtonText, Toast.LENGTH_SHORT).show();
                homevisitData = radioButtonText.toString().trim();
            }
        });
        radiology.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                String radioButtonText = checkedRadioButton.getText().toString();
                // Toast.makeText(getApplicationContext(), "Selected: " + radioButtonText, Toast.LENGTH_SHORT).show();
                radiologyData = radioButtonText.toString().trim();
            }
        });
        if (HomeVisit.equals("Yes")){
            homevisitYes.setChecked(true);
        }else {
            homeVisitNo.setChecked(true);

        }
        if (radiologyTest.equals("Available")){
            radiologyYes.setChecked(true);
        }else {
            radiologyNo.setChecked(true);
        }
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLabNW();
            }
        });

    }
    private void updateLabNW() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.INVENTORY_ID, id);
        params.put(Constant.CENTER_NAME,etCenterName.getText().toString());
        params.put(Constant.LATITUDE, String.valueOf(latitude));
        params.put(Constant.LONGITUDE, String.valueOf(longitude));
        params.put(Constant.MOBILE, etMobile.getText().toString());
        params.put(Constant.EMAIL, etEmail.getText().toString());
        params.put(Constant.MANAGER_NAME, etCenterManager.getText().toString());
        params.put(Constant.OPERATIONAL_HOURS, etCenterOperationalHours.getText().toString());
        params.put(Constant.CENTER_ADDRESS, etCenterAddress.getText().toString());
        params.put(Constant.IMAGE, String.valueOf(imageView));
        params.put(Constant.RADIOLOGY_TEST, radiologyData);
        params.put(Constant.HOME_VISIT, homevisitData);


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
        }, activity, Constant.UPDATE_LAB_NETWORK, params, true);
    }
}