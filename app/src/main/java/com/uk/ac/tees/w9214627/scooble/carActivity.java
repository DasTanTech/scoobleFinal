package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

public class carActivity extends AppCompatActivity {

    private TextView gCompany,gModel,gPrice,gSpeed,gSeats,gEngine;
    private ImageView gImageView;
    private Button gBookBtn;
    private FloatingActionButton gSaveBtn;

    private String CID,UID;
    private int saveCheck ;

    private DatabaseReference gCarRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        UID = firebaseHelper.gUser.getUid();
        CID = getIntent().getStringExtra(COMMON.CAR_ID);

        gCompany = (TextView)findViewById(R.id.car_company);
        gModel = (TextView)findViewById(R.id.car_model);
        gPrice = (TextView)findViewById(R.id.car_price);
        gSpeed = (TextView)findViewById(R.id.car_speed);
        gEngine = (TextView)findViewById(R.id.car_engine);
        gSeats = (TextView)findViewById(R.id.car_seats);
        gImageView = (ImageView)findViewById(R.id.car_imageView);
        gBookBtn = (Button)findViewById(R.id.car_bookBtn);
        gSaveBtn = (FloatingActionButton)findViewById(R.id.car_saveBtn);

        loadCarData(CID);

        gSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (saveCheck == 0)
                {
                    firebaseHelper.saveCar(CID,UID);
                    gSaveBtn.setImageResource(R.drawable.ic_bookmark_active);
                    saveCheck = 1;
                }else if (saveCheck == 1)
                {
                    firebaseHelper.deSaveCar(CID,UID);
                    gSaveBtn.setImageResource(R.drawable.ic_bookmark_white);
                    saveCheck = 0;
                }

            }
        });

        gBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toBooking(CID);
            }
        });

    }

    private void toBooking(String cid) {
        Intent toBooking = new Intent(carActivity.this,bookingActivity.class);
        toBooking.putExtra(COMMON.CAR_ID,cid);
        startActivity(toBooking);
    }

    private void loadCarData(String cid) {

        gCarRef = firebaseHelper.gDatabase.getReference().child(COMMON.CARS_REF).child(cid);
        gCarRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    gCompany.setText(String.valueOf(snapshot.child(COMMON.CAR_COMPANY).getValue()));
                    gModel.setText(String.valueOf(snapshot.child(COMMON.CAR_MODEL).getValue()));
                    gPrice.setText(String.valueOf(snapshot.child(COMMON.CAR_PRICE).getValue())+" $");
                    gSpeed.setText(String.valueOf(snapshot.child(COMMON.CAR_SPEED).getValue())+" km/h");
                    gSeats.setText(String.valueOf(snapshot.child(COMMON.CAR_SEATS).getValue())+" seats");
                    gEngine.setText(String.valueOf(snapshot.child(COMMON.CAR_ENGINE).getValue()));
                    Glide.with(carActivity.this).load(String.valueOf(snapshot.child(COMMON.CAR_PICURL).getValue())).into(gImageView);

                    if (snapshot.child(COMMON.SAVED_USERS).hasChild(UID))
                        gSaveBtn.setImageResource(R.drawable.ic_bookmark_active);
                    else
                        gSaveBtn.setImageResource(R.drawable.ic_bookmark_white);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}