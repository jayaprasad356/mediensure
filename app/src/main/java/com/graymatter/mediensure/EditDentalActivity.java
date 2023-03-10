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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditDentalActivity extends AppCompatActivity {

    EditText etAddress, etMobile, etEmail, etOtp, etFromTime, etToTime,etName,etIncharege;
    Button btnPickLocation, btnAdd, btnSendOTP;

    ImageButton ibBack;

    Activity activity;
    Session session;
    RadioGroup oralXrayRG;
    RadioButton rbYes,rbNo;
    String xray;
    public String id;
    public String userId;
    public String clinicName;
    public String oralXray;
    public String email;
    public String mobile;
    public String address;
    public String latitude;
    public String longitude;
    public String datetime;
    public String remarks;
    public String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dental);


        activity = EditDentalActivity.this;
        session = new Session(activity);

        etAddress = findViewById(R.id.etAddress);
        etName=findViewById(R.id.etName);
        etIncharege=findViewById(R.id.etIncharge);
        etMobile = findViewById(R.id.etMobile);
        etEmail = findViewById(R.id.etEmail);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        btnAdd = findViewById(R.id.btnAdd);
        oralXrayRG = findViewById(R.id.oralXrayRG);
        rbYes = findViewById(R.id.rbYes);
        rbNo = findViewById(R.id.rbNo);
        etFromTime = findViewById(R.id.etFromTime);
        etToTime = findViewById(R.id.etToTime);
        Intent intent = getIntent();

        id = intent.getStringExtra(Constant.ID);
        userId = intent.getStringExtra(Constant.USER_ID);
        clinicName = intent.getStringExtra(Constant.CLINIC_NAME);
        oralXray = intent.getStringExtra(Constant.ORAL_XRAY);
        email = intent.getStringExtra(Constant.EMAIL);
        address = intent.getStringExtra(Constant.ADDRESS);
        mobile = intent.getStringExtra(Constant.MOBILE);
        datetime = intent.getStringExtra(Constant.DATETIME);
        latitude = intent.getStringExtra(Constant.LATITUDE);
        longitude = intent.getStringExtra(Constant.LONGITUDE);
        status = intent.getStringExtra(Constant.STATUS);
        remarks = intent.getStringExtra(Constant.REMARKS);
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

        etName.setText(clinicName);
        etIncharege.setText(address);
        etMobile.setText(mobile);
        etMobile.setEnabled(false);
        etEmail.setText(email);
        etAddress.setText(address);

        btnPickLocation.setEnabled(false);
        btnPickLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);

        oralXrayRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                String radioButtonText = checkedRadioButton.getText().toString();
                // Toast.makeText(getApplicationContext(), "Selected: " + radioButtonText, Toast.LENGTH_SHORT).show();

                xray = radioButtonText.toString().trim();


            }
        });
        if (oralXray.equals("Yes")){
            rbYes.setChecked(true);
        }else {
            rbNo.setChecked(true);
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDentalNW();
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
    private void updateDentalNW() {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID,session.getData(Constant.ID));
        params.put(Constant.INVENTORY_ID, id);
        params.put(Constant.CLINIC_NAME, etName.getText().toString());

        params.put(Constant.LATITUDE, String.valueOf(latitude));
        params.put(Constant.LONGITUDE, String.valueOf(longitude));
        params.put(Constant.MOBILE, etMobile.getText().toString());
        params.put(Constant.EMAIL, etEmail.getText().toString());
        params.put(Constant.ADDRESS, etAddress.getText().toString());
        params.put(Constant.ORAL_XRAY, xray);


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
        }, activity, Constant.UPDATE_DENTAL_NETWORK, params, true);
    }
}