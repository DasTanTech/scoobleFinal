package com.uk.ac.tees.w9214627.scooble.util;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.loginActivity;

import java.util.HashMap;

public class firebaseHelper {

    static GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(String.valueOf(R.string.default_web_client_id))
            .requestEmail()
            .build();

    public static final FirebaseAuth gAuth = FirebaseAuth.getInstance();
    public static FirebaseUser gUser = null;

    public static final FirebaseDatabase gDatabase = FirebaseDatabase.getInstance();
    public static final FirebaseStorage gStorage = FirebaseStorage.getInstance();

    public static void logoutUser(Activity activity) {

        GoogleSignInClient signInClient = GoogleSignIn.getClient(activity, gso);
        if (signInClient != null) {
            signInClient.signOut();
        }
        firebaseHelper.gAuth.signOut();
        Intent toLogin = new Intent(activity, loginActivity.class);
        activity.startActivity(toLogin);
        activity.finish();
    }




    private static DatabaseReference gSaveRef;
    private static HashMap<String,Object> saveData = new HashMap<>();
    public static void saveCar(String cid, String uid) {
        saveData.put(uid,"saved");
        gSaveRef = gDatabase.getReference().child(COMMON.CARS_REF).child(cid).child(COMMON.SAVED_USERS);
        gSaveRef.updateChildren(saveData);
    }

    private static DatabaseReference gDeSaveRef;
    public static void deSaveCar(String cid, String uid) {
        gDeSaveRef = gDatabase.getReference().child(COMMON.CARS_REF).child(cid).child(COMMON.SAVED_USERS).child(uid);
        gSaveRef.setValue(null);
    }
}
