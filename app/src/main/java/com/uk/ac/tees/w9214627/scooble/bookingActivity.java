package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class bookingActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager gLocationManager;
    private AlertDialog gGPSAlertDialog;
    private boolean gLocationPermissionGranted = false;

    private SupportMapFragment gSupportMapFragment;
    private GoogleMap gMap;
    private GoogleMapOptions gMapOptions;
    private GoogleMap.OnCameraIdleListener gListener;

    private FusedLocationProviderClient gFusedLocationProviderClient;
    private Location curLocation;
    private Geocoder gGeoCoder;

    private String CID,UID,DATE = null,BID = null,AID;
    private String COMPANY,MODEL,PICURL,CITY;
    private double LAT,LNG;
    private TextView gCompany,gModel;
    private TextInputLayout gCity,gDate;
    private ImageView gImageView;
    private Button gConformbtn;
    private CheckBox gCheck;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private DatabaseReference gCarRef,gBookingRef,gAgencyBookingRef;

    private HashMap<String,Object> bookingData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        UID = firebaseHelper.gUser.getUid();
        CID = getIntent().getStringExtra(COMMON.CAR_ID);

        gLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        gGeoCoder = new Geocoder(this);

        gCompany = (TextView)findViewById(R.id.booking_company);
        gModel = (TextView)findViewById(R.id.booking_model);
        gImageView = (ImageView)findViewById(R.id.booking_imageView);
        gCity = (TextInputLayout)findViewById(R.id.booking_city);
        gDate = (TextInputLayout)findViewById(R.id.booking_date);
        gConformbtn = (Button)findViewById(R.id.booking_Btn);
        gCheck = (CheckBox)findViewById(R.id.booking_conform);

        gDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        bookingActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                DATE = dayOfMonth+"-"+month+"-"+year;
                gDate.getEditText().setText(dayOfMonth+"-"+month+"-"+year);
            }
        };

        gConformbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DATE == null)
                    Toast.makeText(bookingActivity.this, "Select a date", Toast.LENGTH_SHORT).show();
                else if (!gCheck.isChecked())
                    Toast.makeText(bookingActivity.this, "Accept the Terms and Conditions.", Toast.LENGTH_SHORT).show();
                else
                {
                    bookTheCar(CID,UID,DATE);
                }
            }
        });

    }

    private void bookTheCar(String cid, String uid, String date) {

        BID = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF).child(uid).child(COMMON.BOOKINGS).push().getKey();

        bookingData.put(COMMON.CAR_COMPANY,COMPANY);
        bookingData.put(COMMON.CAR_MODEL,MODEL);
        bookingData.put(COMMON.CAR_PICURL,PICURL);
        bookingData.put(COMMON.CAR_CITY,CITY);
        bookingData.put(COMMON.BOOKING_DATE,date);
        bookingData.put(COMMON.USER_ID,uid);
        bookingData.put(COMMON.CAR_ID,cid);
        bookingData.put(COMMON.AGENCY_ID,AID);
        bookingData.put(COMMON.BOOKING_ID,BID);
        bookingData.put(COMMON.CAR_LAT,LAT);
        bookingData.put(COMMON.CAR_LNG,LNG);

        gBookingRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF).child(uid).child(COMMON.BOOKINGS).child(BID);
        gBookingRef.setValue(bookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    updateAgency(bookingData);
                }
            }
        });


    }

    private void updateAgency(HashMap<String, Object> bookingData) {
        gAgencyBookingRef = firebaseHelper.gDatabase.getReference().child(COMMON.AGENCY_REF).child(AID).child(COMMON.BOOKINGS).child(BID);
        gAgencyBookingRef.setValue(bookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    toConformBooking(BID,CID,AID);
                }
            }
        });
    }

    private void toConformBooking(String bid, String cid, String aid) {
        Intent toConform = new Intent(bookingActivity.this,bookingConfirmationActivity.class);
        toConform.putExtra(COMMON.BOOKING_ID,bid);
        toConform.putExtra(COMMON.CAR_ID,cid);
        toConform.putExtra(COMMON.AGENCY_ID,aid);
        startActivity(toConform);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDeviceGPS();
    }

    private void loadCarData(String cid) {
        gCarRef = firebaseHelper.gDatabase.getReference().child(COMMON.CARS_REF).child(cid);
        gCarRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    AID = String.valueOf(snapshot.child(COMMON.CAR_AGENCY_ID).getValue());
                    COMPANY = String.valueOf(snapshot.child(COMMON.CAR_COMPANY).getValue());
                    gCompany.setText(COMPANY);
                    MODEL = String.valueOf(snapshot.child(COMMON.CAR_MODEL).getValue());
                    gModel.setText(MODEL);
                    CITY = String.valueOf(snapshot.child(COMMON.CAR_CITY).getValue());
                    gCity.getEditText().setText(CITY);
                    PICURL = String.valueOf(snapshot.child(COMMON.CAR_PICURL).getValue());
                    Glide.with(bookingActivity.this).load(PICURL).into(gImageView);
                    LAT = Double.parseDouble(String.valueOf(snapshot.child(COMMON.CAR_LAT).getValue()));
                    LNG = Double.parseDouble(String.valueOf(snapshot.child(COMMON.CAR_LNG).getValue()));
                    initMaps();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkDeviceGPS() {
        if (!gLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            showAlertDIalogForGPS();
        else
            getLocationPermissions();
    }

    private void showAlertDIalogForGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Required GPS")
                .setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        getLocationPermissions();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        gGPSAlertDialog = builder.create();
        gGPSAlertDialog.show();
    }

    private void getLocationPermissions() {
        String[] permissions = {COMMON.FINE_LOCATION, COMMON.COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COMMON.FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COMMON.COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gLocationPermissionGranted = true;
                loadCarData(CID);
            } else {
                ActivityCompat.requestPermissions(this, permissions, COMMON.LOCATION_REQUEST);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, COMMON.LOCATION_REQUEST);
        }
    }

    private void initMaps() {
        gSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.booking_map);
        if (gSupportMapFragment != null)
            gSupportMapFragment.getMapAsync(bookingActivity.this);
        else
            Toast.makeText(this, "Map not Loaded..", Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(LAT,LNG);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Your car is here."));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
    }


}