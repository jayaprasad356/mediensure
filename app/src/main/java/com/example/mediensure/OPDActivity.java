package com.example.mediensure;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mediensure.helper.ApiConfig;
import com.example.mediensure.helper.Constant;
import com.example.mediensure.helper.LocationTrack;
import com.example.mediensure.helper.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OPDActivity extends AppCompatActivity {


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




    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    double longitude;
    double latitude;


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    RelativeLayout rlAddImage;
    private ImageView imageView;

    ImageButton ibBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opdactivity);


        activity = OPDActivity.this;
        session = new Session(activity);

        etaddress = findViewById(R.id.etAddress);
        etemail = findViewById(R.id.etemail);
        etmobile = findViewById(R.id.etMobile);
        spinner = findViewById(R.id.spinner);
        btnPickLocation = findViewById(R.id.btnPickLocation);
        btnAdd = findViewById(R.id.btnAdd);
        rlAddImage = findViewById(R.id.rlAddImage);
        imageView = findViewById(R.id.imageView);
        btnSendOTP = findViewById(R.id.btnSendOTP);
        etOtp = findViewById(R.id.etOtp);



        btnAdd.setOnClickListener(v -> {



            if (etOtp.getText().length() == 6){
                if (!mVerificationId.equals("")){
                    verifyPhoneNumberWithCode(mVerificationId,etOtp.getText().toString());

                }
                else {
                    Toast.makeText(activity, "Invalid OTP", Toast.LENGTH_SHORT).show();
                }


            }
            else {
                Toast.makeText(activity, "Enter OTP", Toast.LENGTH_SHORT).show();
            }

            //etmobile length 10 and email validation and spinner validation and address validation


        });


        btnSendOTP.setOnClickListener(v -> {


            showOtp();
        });

        ibBack = findViewById(R.id.ibBack);

        ibBack.setOnClickListener(v -> {
            onBackPressed();
        });


        rlAddImage.setOnClickListener(v -> {

            //add image from camera and gallery

            showImagePickDialog();

        });

        btnPickLocation.setOnClickListener(v -> {
            gpslocation();

            //btnPiLoction bacckgrount Disable and tick drawable

            btnPickLocation.setEnabled(false);
            btnPickLocation.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_check_circle_24, 0);

        });



        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

    }


    private void showOtp() {
        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;

            }
        };
        startPhoneNumberVerification("+91"+etmobile.toString().trim());

    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");


                          if (etemail.getText().toString().isEmpty()) {

                                etemail.setError("Enter Valid Email");
                                etemail.requestFocus();

                            } else if (etaddress.getText().toString().isEmpty()) {

                                etaddress.setError("Enter Valid Address");
                                etaddress.requestFocus();

                            } else if (spinner.getSelectedItem().toString().equals("Select")) {

                                Toast.makeText(activity, "Select Valid City", Toast.LENGTH_SHORT).show();

                            }

                            // check the location is enable or not
                            else if (btnPickLocation.isEnabled()) {

                                Toast.makeText(activity, "Please Pick Location", Toast.LENGTH_SHORT).show();

                            }


                            else {

                                //add data to database

                                addOPD();


                            }



                            // Update UI
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }


    private void showImagePickDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(new CharSequence[]{"Camera", "Gallery"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                pickImageFromCamera();
                                break;
                            case 1:
                                pickImageFromGallery();
                                break;
                        }
                    }
                });
        builder.show();
    }


    private void pickImageFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Handle image captured from camera
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                // Handle image picked from gallery
                Uri imageUri = data.getData();
                imageView.setImageURI(imageUri);
            }

            // Set the visibility of the ImageView to VISIBLE
            imageView.setVisibility(View.VISIBLE);
            rlAddImage.setVisibility(View.GONE);
        }
    }



    private void gpslocation() {

        locationTrack = new LocationTrack(activity);


        if (locationTrack.canGetLocation()) {


            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();

            Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {

            locationTrack.showSettingsAlert();
        }
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission((String) perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission((String) perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void addOPD() {

        //add data to database


        //api

        Map<String, String> params = new HashMap<>();
        params.put(Constant.USER_ID, session.getData(Constant.ID));
        params.put(Constant.MOBILE, etmobile.getText().toString().trim());
        params.put(Constant.EMAIL, etemail.getText().toString().trim());
        params.put(Constant.ADDRESS, etaddress.getText().toString().trim());
        params.put(Constant.NAME, spinner.getSelectedItem().toString().trim());
        params.put(Constant.LATITUDE, String.valueOf(latitude));
        params.put(Constant.LONGITUDE, String.valueOf(longitude));
        params.put(Constant.IMAGE, String.valueOf(imageView));



        ApiConfig.RequestToVolley((result, response) -> {
            Log.d("LOGIN_RES", response);
            if (result) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean(Constant.SUCCESS)) {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                        JSONArray jsonArray = jsonObject.getJSONArray(Constant.DATA);


                        startActivity(new Intent(activity, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(activity, jsonObject.getString(Constant.MESSAGE), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, activity, Constant.ADD_OPD_NETWORK, params, true);


    }
}