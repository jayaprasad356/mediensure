package com.graymatter.mediensure;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.graymatter.mediensure.helper.ApiConfig;
import com.graymatter.mediensure.helper.Constant;
import com.graymatter.mediensure.helper.LocationTrack;
import com.graymatter.mediensure.helper.Session;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditPharmacyActivity extends AppCompatActivity {
    EditText etAddress, etMobile, etEmail, etOtp, etFromTime, etToTime, etShopName, etName;
    Button btnPickLocation, btnAdd, btnSendOTP;

    ImageButton ibBack;

    Activity activity;
    Session session;

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;

    public String id;
    public String userId;
    public String shopName;
    public String address;
    public String email;
    public String mobile;
    public String latitude;
    public String longitude;
    public String datetime;
    public String remarks;
    public String status;
    public String operational_hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pharmacy);

        activity = EditPharmacyActivity.this;
        session = new Session(activity);


        etAddress = findViewById(R.id.etAddress);
        etShopName = findViewById(R.id.etShopName);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        btnAdd = findViewById(R.id.btnAdd);
        ibBack = findViewById(R.id.ibBack);

        etFromTime = findViewById(R.id.etFromTime);
        etToTime = findViewById(R.id.etToTime);

        id = getIntent().getStringExtra(Constant.ID);
        userId = getIntent().getStringExtra(Constant.USER_ID);
        shopName = getIntent().getStringExtra(Constant.SHOP_NAME);
        address = getIntent().getStringExtra(Constant.ADDRESS);
        email = getIntent().getStringExtra(Constant.EMAIL);
        mobile = getIntent().getStringExtra(Constant.MOBILE);
        datetime = getIntent().getStringExtra(Constant.DATETIME);
        latitude = getIntent().getStringExtra(Constant.LATITUDE);
        longitude = getIntent().getStringExtra(Constant.LONGITUDE);
        status = getIntent().getStringExtra(Constant.STATUS);
        remarks = getIntent().getStringExtra(Constant.REMARKS);
        operational_hours=getIntent().getStringExtra(Constant.OPERATIONAL_HOURS);
        operational_hours =getIntent().getStringExtra(Constant.OPERATIONAL_HOURS);
        etFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String fromTime = formatTime(hourOfDay, minute);
                                etFromTime.setText(fromTime);

                                // Compare etFromTime and etToTime
                                String toTime = etToTime.getText().toString().trim();
                                if (!toTime.isEmpty()) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                        Date fromDate = sdf.parse(fromTime);
                                        Date toDate = sdf.parse(toTime);

                                        if (fromDate.after(toDate)) {
                                            // Show error message
                                            Toast.makeText(activity, "Please select a correct start time", Toast.LENGTH_SHORT).show();
                                            etFromTime.setText("");
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false);

                timePickerDialog.show();
            }
        });

        etToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String toTime = formatTime(hourOfDay, minute);
                                etToTime.setText(toTime);

                                // Compare etFromTime and etToTime
                                String fromTime = etFromTime.getText().toString().trim();
                                if (!fromTime.isEmpty()) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                                        Date fromDate = sdf.parse(fromTime);
                                        Date toDate = sdf.parse(toTime);

                                        if (toDate.before(fromDate)) {
                                            // Show error message
                                            Toast.makeText(activity, "Please select a correct end time", Toast.LENGTH_SHORT).show();
                                            etToTime.setText("");
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        false);

                timePickerDialog.show();
            }
        });

        if (operational_hours!=null) {
            String[] hoursArray = operational_hours.split(" - ");
            String startTime = hoursArray[0];
            String endTime = hoursArray[1];
            etFromTime.setText(startTime);
            etToTime.setText(endTime);
        }

        etShopName.setText(shopName);
        etAddress.setText(address);
        etMobile.setText(mobile);
        etEmail.setText(email);
        etMobile.setEnabled(false);
        btnPickLocation.setEnabled(false);
        btnPickLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePharmacy();
            }
        });

    }
    private String formatTime(int hour, int minute) {
        String amPm;
        if (hour == 12) {
            amPm = "PM";
        } else if (hour > 12) {
            amPm = "PM";
            hour -= 12;
        } else {
            amPm = "AM";
        }

        return String.format("%d:%02d %s", hour, minute, amPm);
    }
    private void updatePharmacy() {

        Map<String, String> params = new HashMap<>();
        params.put(Constant.SHOP_NAME, etShopName.getText().toString());
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.INVENTORY_ID, id);
        params.put(Constant.LATITUDE, String.valueOf(latitude));
        params.put(Constant.LONGITUDE, String.valueOf(longitude));
        params.put(Constant.MOBILE, etMobile.getText().toString());
        params.put(Constant.EMAIL, etEmail.getText().toString());
        params.put(Constant.ADDRESS, etAddress.getText().toString());


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
        }, activity, Constant.UPDATE_PHARMACY_NETWORK, params, true);

    }
}