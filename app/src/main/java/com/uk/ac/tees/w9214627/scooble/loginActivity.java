package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.HashMap;

public class loginActivity extends AppCompatActivity {

    //common
    private ProgressDialog gDialog;

    //customAuth
    private EditText gEmail,gPassword;
    private Button gLogin;
    private String EMAIL = null , PASSWORD = null;

    //customRegister
    private TextView gRegister;

    //google sign in
    private Button gGoogleSignIn;
    private GoogleSignInClient gGoogleSignInClient;
    private GoogleApiClient gGoogleApiClient;
    private static final int RC_SIGN_IN = 123;
    private HashMap<String,String> instanceMap = new HashMap<>();
    private DatabaseReference gUserInstanceRef;
    private String GNAME,GEMAIL;

    //agency
    private Button gAgencyLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //common
        gDialog = new ProgressDialog(this);
        gDialog.setTitle("Please wait");
        gDialog.setMessage("Signing in......");

        googleSignInInstance();

        //customAuth
        gEmail = (EditText)findViewById(R.id.login_email);
        gPassword = (EditText)findViewById(R.id.login_password);
        gLogin = (Button)findViewById(R.id.login_loginBtn);

        gLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EMAIL = gEmail.getText().toString();
                PASSWORD = gPassword.getText().toString();

                if (TextUtils.isEmpty(EMAIL))
                    Toast.makeText(loginActivity.this, "Enter a valid email.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(PASSWORD))
                    Toast.makeText(loginActivity.this, "Enter a valid password.", Toast.LENGTH_SHORT).show();
                else
                {
                    customAuthLogin(EMAIL,PASSWORD);
                }
            }
        });

        //customRegister
        gRegister = (TextView)findViewById(R.id.login_register);

        gRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCustomRegister();
            }
        });


        //google sign in
        gGoogleSignIn = (Button)findViewById(R.id.login_googleSignInBtn);

        gGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //agency
        gAgencyLoginBtn = (Button)findViewById(R.id.login_agencyBtn);

        gAgencyLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAgencyLogin();
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
                            Toast.makeText(loginActivity.this, "Invalid password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void googleSignInInstance() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    private void signIn() {
        Intent signInIntent = gGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        gDialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseHelper.gAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String UID = task.getResult().getUser().getUid();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(loginActivity.this);
                            if (acct != null) {
                                GNAME = acct.getDisplayName();
                                GEMAIL = acct.getEmail();
                            }
                            createUserInstance(GNAME,GEMAIL,UID);
                        } else {
                            gDialog.dismiss();
                            Toast.makeText(loginActivity.this, "Some Error.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }
    private void createUserInstance(String GNAME, String GEMAIL , final String uid) {
        instanceMap.put(COMMON.USER_FULL_NAME,GNAME);
        instanceMap.put(COMMON.USER_EMAIL_ID,GEMAIL);
        gUserInstanceRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF);
        gUserInstanceRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(uid))
                    toHome();
                else
                {
                    gUserInstanceRef.child(uid).setValue(instanceMap);
                    toHome();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void toHome() {
        Intent toHome = new Intent(loginActivity.this,homeActivity.class);
        startActivity(toHome);
        finish();
    }
    private void toCustomRegister() {
        Intent toCustomRegister = new Intent(loginActivity.this,customRegisterActivity.class);
        startActivity(toCustomRegister);
    }
    private void toAgencyLogin() {
        Intent toAgencyLogin = new Intent(loginActivity.this,agencyLoginActivity.class);
        startActivity(toAgencyLogin);
    }
}