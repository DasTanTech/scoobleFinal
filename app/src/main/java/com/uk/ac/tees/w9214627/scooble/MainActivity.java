package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

public class MainActivity extends AppCompatActivity {

    private Handler gHandler;
    private static final int TIME_DELAY = 4000;
    private String ID =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logicChecker();

    }

    private void logicChecker(){
        gHandler = new Handler();
        firebaseHelper.gUser = firebaseHelper.gAuth.getCurrentUser();
        if (firebaseHelper.gUser == null)
        {
            gHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toLogin();
                }
            },TIME_DELAY);
        }else
        {
            ID = firebaseHelper.gUser.getUid();
            DatabaseReference checkRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF);
            checkRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(ID))
                    {
                        gHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toHome();
                            }
                        },TIME_DELAY);
                    }else
                    {
                        gHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toAgencyHome();
                            }
                        },TIME_DELAY);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }

    private void toAgencyHome() {
        Intent toHome = new Intent(MainActivity.this,agencyHomeActivity.class);
        startActivity(toHome);
        finish();
    }
    private void toLogin() {
        Intent toLogin = new Intent(MainActivity.this,loginActivity.class);
        startActivity(toLogin);
        finish();
    }
    private void toHome() {
        Intent toHome = new Intent(MainActivity.this,homeActivity.class);
        startActivity(toHome);
        finish();
    }
}