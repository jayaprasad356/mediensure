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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.graymatter.mediensure.helper.ApiConfig;
import com.graymatter.mediensure.helper.Constant;
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

public class RadiologyEditActivity extends AppCompatActivity {
    EditText etName,etShopName,etAddress,etFromTime,etToTime,etEmail,etMobile,etOtp;
    Spinner spinner;
    Button btnPickLocation, btnAdd,btnSendOTP;

    ImageButton ibBack;

    Activity activity;
    Session session;
    RelativeLayout rlAddImage;
    public String inventory_id;
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
        spinner = findViewById(R.id.spinnerKM);
        btnPickLocation=findViewById(R.id.btnPickLocation);
        imageView = findViewById(R.id.imageView);
        btnAdd=findViewById(R.id.btnAdd);


        inventory_id =getIntent().getStringExtra(Constant.ID);
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
        etMobile.setEnabled(false);
        etEmail.setText(email);
        etFromTime.setText(startTime);
        etToTime.setText(endTime);
        btnPickLocation.setEnabled(false);
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

        btnPickLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);

        rlAddImage = findViewById(R.id.rlAddImage);
        imageView = findViewById(R.id.imageView);
btnAdd.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        updateRadiology();
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

    private void updateRadiology() {



        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.INVENTORY_ID, inventory_id);
        params.put(Constant.LATITUDE, String.valueOf(latitude));
        params.put(Constant.LONGITUDE, String.valueOf(longitude));
        params.put(Constant.MOBILE, etMobile.getText().toString());
        params.put(Constant.EMAIL, etEmail.getText().toString());
        params.put(Constant.CENTER_ADDRESS, etAddress.getText().toString());
        params.put(Constant.SHOP_NAME, spinner.getSelectedItem().toString());
        params.put(Constant.MANAGER_NAME, etName.getText().toString());
        params.put(Constant.CENTER_NAME, etShopName.getText().toString());
        params.put(Constant.OPERATIONAL_HOURS, etFromTime.getText().toString() + " - " + etToTime.getText().toString());
        params.put(Constant.IMAGE, image);



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
        }, activity, Constant.UPDATE_RADIOLOGY_NETWORK, params, true);
    }
}