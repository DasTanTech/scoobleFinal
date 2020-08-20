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

public class customRegisterActivity extends AppCompatActivity {

    private EditText gName,gEmail,gPassword,gRepassword;
    private Button gRegister;
    private TextView gLogin;

    private String NAME,EMAIL,PASSWORD,RE_PASSWORD,UID;
    private HashMap<String,String> instanceMap = new HashMap<>();

    private DatabaseReference gUserInstanceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_register);

        gName = (EditText)findViewById(R.id.register_name);
        gEmail = (EditText)findViewById(R.id.register_email);
        gPassword = (EditText)findViewById(R.id.register_password);
        gRepassword = (EditText)findViewById(R.id.register_repassword);
        gRegister = (Button)findViewById(R.id.register_btn);
        gLogin = (TextView)findViewById(R.id.register_loginBtn);

        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NAME = gName.getText().toString();
                EMAIL = gEmail.getText().toString();
                PASSWORD = gPassword.getText().toString();
                RE_PASSWORD = gRepassword.getText().toString();

                if (TextUtils.isEmpty(NAME))
                    Toast.makeText(customRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(customRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(customRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(customRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(NAME))
                    Toast.makeText(customRegisterActivity.this, "Enter a valid name.", Toast.LENGTH_SHORT).show();
                else if (!PASSWORD.equals(RE_PASSWORD))
                    Toast.makeText(customRegisterActivity.this, "Passwords does not match.", Toast.LENGTH_SHORT).show();
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
                            UID = task.getResult().getUser().getUid();
                            createUserInstance(UID,NAME,EMAIL);
                        }
                        else
                        {
                            Toast.makeText(customRegisterActivity.this, "Error Occured, Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void createUserInstance(String uid, String name, String email) {

        instanceMap.put(COMMON.USER_FULL_NAME,name);
        instanceMap.put(COMMON.USER_EMAIL_ID,email);

        gUserInstanceRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF).child(uid);
        gUserInstanceRef.setValue(instanceMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                    toHome();
            }
        });

    }

    private void toHome() {
        Intent toHome = new Intent(customRegisterActivity.this,homeActivity.class);
        startActivity(toHome);
        finish();
    }
    private void toLogin() {
        Intent toHome = new Intent(customRegisterActivity.this,loginActivity.class);
        startActivity(toHome);

    }
}