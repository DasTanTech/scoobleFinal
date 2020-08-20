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
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

public class agencyLoginActivity extends AppCompatActivity {

    //customAuth
    private EditText gEmail,gPassword;
    private Button gLogin;
    private String EMAIL = null , PASSWORD = null;

    //customRegister
    private TextView gRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agency_login);

        //customAuth
        gEmail = (EditText)findViewById(R.id.agencyLogin_email);
        gPassword = (EditText)findViewById(R.id.agencyLogin_password);
        gLogin = (Button)findViewById(R.id.agencyLogin_loginBtn);

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMAIL = gEmail.getText().toString();
                PASSWORD = gPassword.getText().toString();

                if (TextUtils.isEmpty(EMAIL))
                    Toast.makeText(agencyLoginActivity.this, "Enter a valid email.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(PASSWORD))
                    Toast.makeText(agencyLoginActivity.this, "Enter a valid password.", Toast.LENGTH_SHORT).show();
                else
                {
                    customAuthLogin(EMAIL,PASSWORD);
                }
            }
        });

        //customRegister
        gRegister = (TextView)findViewById(R.id.agencyLogin_register);

        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCustomRegister();
            }
        });
    }

    private void customAuthLogin(String email, String password) {
        firebaseHelper.gAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            firebaseHelper.gUser = task.getResult().getUser();
                            toHome();
                        }else
                            Toast.makeText(agencyLoginActivity.this, "Invalid password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void toHome() {
        Intent toHome = new Intent(agencyLoginActivity.this,agencyHomeActivity.class);
        startActivity(toHome);
        finish();
    }
    private void toCustomRegister() {
        Intent toCustomRegister = new Intent(agencyLoginActivity.this,agencyRegisterActivity.class);
        startActivity(toCustomRegister);
    }
}