package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class agencyEditProfileActivity extends AppCompatActivity {

    private TextInputLayout gName,gEmail,gPhone;

    private Button gUpdatebtn;
    private String NAME,EMAIL,PHONE;
    private HashMap<String,Object> agencyDataMap = new HashMap<>();


    private DatabaseReference gAgencyRef;
    private String AID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_edit_profile);

        AID = firebaseHelper.gAuth.getUid();
        gAgencyRef = firebaseHelper.gDatabase.getReference().child(COMMON.AGENCY_REF).child(AID);

        gName = (TextInputLayout)findViewById(R.id.agencyEditProfileFrag_name);
        gEmail = (TextInputLayout)findViewById(R.id.agencyEditProfileFrag_email);
        gPhone = (TextInputLayout)findViewById(R.id.agencyEditProfileFrag_phone);

        gUpdatebtn = (Button)findViewById(R.id.agencyEditProfileFrag_updateBtn);

        loadProfileData(AID);

        gUpdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NAME = gName.getEditText().getText().toString();
                EMAIL = gEmail.getEditText().getText().toString();
                PHONE = gPhone.getEditText().getText().toString();

                if (TextUtils.isEmpty(NAME))
                    Toast.makeText(agencyEditProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(EMAIL))
                    Toast.makeText(agencyEditProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(PHONE))
                    Toast.makeText(agencyEditProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else {
                    update(NAME,EMAIL,PHONE);
                }
            }
        });
    }

    private void loadProfileData(String uid){

        gAgencyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NAME = String.valueOf(snapshot.child(COMMON.USER_FULL_NAME).getValue());
                gName.getEditText().setText(NAME);
                EMAIL = String.valueOf(snapshot.child(COMMON.USER_EMAIL_ID).getValue());
                gEmail.getEditText().setText(EMAIL);
                PHONE = String.valueOf(snapshot.child(COMMON.USER_PHONE).getValue());
                gPhone.getEditText().setText(PHONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void update(String name, String email, String phone) {

        agencyDataMap.put(COMMON.AGENCY_NAME,name);
        agencyDataMap.put(COMMON.AGENCY_EMAIL,email);
        agencyDataMap.put(COMMON.AGENCY_PHONE,phone);

        gAgencyRef.updateChildren(agencyDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    toHome();
                else
                    Toast.makeText(agencyEditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toHome() {
        Intent toHome = new Intent(agencyEditProfileActivity.this,agencyHomeActivity.class);
        startActivity(toHome);
        finish();
    }
}