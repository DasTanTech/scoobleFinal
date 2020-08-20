package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.HashMap;

public class agencyRegisterActivity extends AppCompatActivity {

    private EditText gName,gEmail,gPassword,gPhone;
    private Button gRegister;
    private TextView gLogin;

    private String NAME,EMAIL,PASSWORD,PHONE,AID;
    private HashMap<String,String> instanceMap = new HashMap<>();

    private DatabaseReference gAgencyInstanceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_register);

        gName = (EditText)findViewById(R.id.agencyRegister_name);
        gEmail = (EditText)findViewById(R.id.agencyRegister_email);
        gPassword = (EditText)findViewById(R.id.agencyRegister_password);
        gPhone = (EditText)findViewById(R.id.agencyRegister_contact);
        gRegister = (Button)findViewById(R.id.agencyRegister_btn);
        gLogin = (TextView)findViewById(R.id.agencyRegister_loginBtn);

        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NAME = gName.getText().toString();
                EMAIL = gEmail.getText().toString();
                PASSWORD = gPassword.getText().toString();
                PHONE = gPhone.getText().toString();

                if (TextUtils.isEmpty(NAME))
                    Toast.makeText(agencyRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(agencyRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(agencyRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(agencyRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(agencyRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else
                {
                    customRegister(EMAIL,PASSWORD);
                }

            }
        });

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLogin();
            }
        });

    }

    private void customRegister(String email, String password) {

        firebaseHelper.gAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            firebaseHelper.gUser = task.getResult().getUser();
                            AID = task.getResult().getUser().getUid();
                            createAgencyInstance(AID,NAME,EMAIL,PHONE);
                        }
                        else
                        {
                            Toast.makeText(agencyRegisterActivity.this, "Error Occured, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void createAgencyInstance(String uid, String name, String email,String phone) {

        instanceMap.put(COMMON.AGENCY_NAME,name);
        instanceMap.put(COMMON.AGENCY_EMAIL,email);
        instanceMap.put(COMMON.AGENCY_PHONE,phone);

        gAgencyInstanceRef = firebaseHelper.gDatabase.getReference().child(COMMON.AGENCY_REF).child(uid);
        gAgencyInstanceRef.setValue(instanceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    toHome();
                else
                    Toast.makeText(agencyRegisterActivity.this, "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toHome() {
        Intent toHome = new Intent(agencyRegisterActivity.this,agencyHomeActivity.class);
        startActivity(toHome);
        finish();
    }
    private void toLogin() {
        Intent toHome = new Intent(agencyRegisterActivity.this,agencyLoginActivity.class);
        startActivity(toHome);

    }
}