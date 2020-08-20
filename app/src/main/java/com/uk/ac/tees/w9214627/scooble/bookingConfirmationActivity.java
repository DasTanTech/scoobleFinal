package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

public class bookingConfirmationActivity extends FragmentActivity implements OnMapReadyCallback {

    private LocationManager gLocationManager;
    private AlertDialog gGPSAlertDialog;
    private boolean gLocationPermissionGranted = false;

    private SupportMapFragment gSupportMapFragment;
    private GoogleMap gMap;

    private TextView gCompany, gModel, gCity, gDate, gAgencyname, gAgencyContact, gBID;
    private ImageView gImageView;
    private String UID, BID,AID;
    private String LAT, LNG;


    private DatabaseReference gBookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);
        UID = firebaseHelper.gUser.getUid();
        BID = getIntent().getStringExtra(COMMON.BOOKING_ID);
        AID = getIntent().getStringExtra(COMMON.AGENCY_ID);

        gLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gCompany = (TextView) findViewById(R.id.conformBooking_company);
        gModel = (TextView) findViewById(R.id.conformBooking_model);
        gImageView = (ImageView) findViewById(R.id.conformBooking_imageView);
        gCity = (TextView) findViewById(R.id.conformBooking_city);
        gDate = (TextView) findViewById(R.id.conformBooking_date);
        gAgencyname = (TextView) findViewById(R.id.conformBooking_agencyName);
        gAgencyContact = (TextView) findViewById(R.id.conformBooking_agencyContact);
        gBID = (TextView)findViewById(R.id.conformBooking_BID);


    }

    private void loadData() {
        gBookingRef = firebaseHelper.gDatabase.getReference().child(COMMON.AGENCY_REF).child(AID);
        gBookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    gAgencyname.setText(String.valueOf(snapshot.child(COMMON.AGENCY_NAME).getValue()));
                    gAgencyContact.setText(String.valueOf(snapshot.child(COMMON.AGENCY_PHONE).getValue()));
                    gCompany.setText(String.valueOf(snapshot.child(COMMON.BOOKINGS).child(BID).child(COMMON.CAR_COMPANY).getValue()));
                    gModel.setText(String.valueOf(snapshot.child(COMMON.BOOKINGS).child(BID).child(COMMON.CAR_MODEL).getValue()));
                    gCity.setText(String.valueOf(snapshot.child(COMMON.BOOKINGS).child(BID).child(COMMON.CAR_CITY).getValue()));
                    gDate.setText(String.valueOf(snapshot.child(COMMON.BOOKINGS).child(BID).child(COMMON.BOOKING_DATE).getValue()));
                    Glide.with(bookingConfirmationActivity.this).load(String.valueOf(snapshot.child(COMMON.BOOKINGS).child(BID).child(COMMON.CAR_PICURL).getValue()))
                            .into(gImageView);

                    gBID.setText("Booking ID: "+BID);
                    initMaps();
                }
                else
                    Toast.makeText(bookingConfirmationActivity.this, UID, Toast.LENGTH_SHORT).show();
               
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkDeviceGPS();
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
                loadData();
            } else {
                ActivityCompat.requestPermissions(this, permissions, COMMON.LOCATION_REQUEST);
            }

        } else {
            ActivityCompat.requestPermissions(this, permissions, COMMON.LOCATION_REQUEST);
        }
    }



    private void initMaps() {
        gSupportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.conformBooking_map);
        if (gSupportMapFragment != null)
            gSupportMapFragment.getMapAsync(bookingConfirmationActivity.this);
        else
            Toast.makeText(this, "Map not Loaded..", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng sydney = new LatLng(51.517085,-0.183265);
        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("Your car is here."));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,16));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toHome();
    }

    private void toHome() {
        Intent toHome = new Intent(bookingConfirmationActivity.this,homeActivity.class);
        startActivity(toHome);
        finish();
    }
}