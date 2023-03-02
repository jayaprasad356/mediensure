package com.graymatter.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.Session;

public class RadiologyEditActivity extends AppCompatActivity {
    EditText etName,etShopName,etAddress,etFromTime,etToTime,etEmail,etMobile,etOtp;
    Spinner spinner;
    Button btnPickLocation, btnAdd,btnSendOTP;

    ImageButton ibBack;

    Activity activity;
    Session session;
    RelativeLayout rlAddImage;
    public String id;
    public String center_name;
    public String email;
    public String mobile;
    public String manager_name;
    public String center_address;
    public String operational_hours;
    public String latitude;
    public String longitude;
    public String datetime;
    public String image;
    public String remarks;
    public String status;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiology_edit);

        activity = this;
        session = new Session(activity);

        etName = findViewById(R.id.etName);
        etShopName = findViewById(R.id.etShopName);
        etAddress = findViewById(R.id.etAddress);
        etFromTime = findViewById(R.id.etFromTime);
        etToTime = findViewById(R.id.etToTime);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etOtp = findViewById(R.id.etOtp);
        spinner = findViewById(R.id.spinnerKM);


        id=getIntent().getStringExtra(Constant.ID);
        center_name=getIntent().getStringExtra(Constant.CENTER_NAME);
        email=getIntent().getStringExtra(Constant.EMAIL);
        mobile=getIntent().getStringExtra(Constant.MOBILE);
        manager_name=getIntent().getStringExtra(Constant.MANAGER_NAME);
        center_address=getIntent().getStringExtra(Constant.CENTER_ADDRESS);
        operational_hours=getIntent().getStringExtra(Constant.OPERATIONAL_HOURS);
        latitude=getIntent().getStringExtra(Constant.LATITUDE);
        longitude=getIntent().getStringExtra(Constant.LONGITUDE);
        datetime=getIntent().getStringExtra(Constant.DATETIME);
        image=getIntent().getStringExtra(Constant.IMAGE);
        remarks=getIntent().getStringExtra(Constant.REMARKS);
        status=getIntent().getStringExtra(Constant.STATUS);

        operational_hours =getIntent().getStringExtra(Constant.OPERATIONAL_HOURS);
        String[] hoursArray = operational_hours.split(" - ");
        String startTime = hoursArray[0];
        String endTime = hoursArray[1];
        etShopName.setText(center_name);
        etName.setText(manager_name);
        etAddress.setText(center_address);
        etMobile.setText(mobile);
        etEmail.setText(email);
        etFromTime.setText(startTime);
        etToTime.setText(endTime);





        rlAddImage = findViewById(R.id.rlAddImage);
        imageView = findViewById(R.id.imageView);

    }
}