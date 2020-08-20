package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
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

import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class editProfileActivity extends AppCompatActivity {

    private TextInputLayout gName,gEmail,gPhone,gDOB;
    private CircleImageView gPropic;
    private Button gUpdatebtn;
    private String NAME,EMAIL,PHONE,DOB,PROPIC_URL= null;
    private static final int GALLERY_PICK = 1;
    private Uri IMAGE_URL = null;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private HashMap<String,Object> userDataMap = new HashMap<>();

    private StorageReference gProPicRef;
    private UploadTask uploadTask;

    private DatabaseReference gUserRef;
    private String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        UID = firebaseHelper.gAuth.getUid();

        gName = (TextInputLayout)findViewById(R.id.editProfileFrag_name);
        gEmail = (TextInputLayout)findViewById(R.id.editProfileFrag_email);
        gPhone = (TextInputLayout)findViewById(R.id.editProfileFrag_phone);
        gDOB = (TextInputLayout)findViewById(R.id.editProfileFrag_dob);
        gPropic = (CircleImageView)findViewById(R.id.editProfileFrag_imageView);
        gUpdatebtn = (Button)findViewById(R.id.editProfileFrag_updateBtn);

        loadProfileData(UID);

        gDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        editProfileActivity.this,
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
                DOB = dayOfMonth+"-"+month+"-"+year;
                gDOB.getEditText().setText(dayOfMonth+"-"+month+"-"+year);
            }
        };



        gPropic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        gUpdatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NAME = gName.getEditText().getText().toString();
                EMAIL = gEmail.getEditText().getText().toString();
                PHONE = gPhone.getEditText().getText().toString();

                if (TextUtils.isEmpty(NAME))
                    Toast.makeText(editProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(EMAIL))
                    Toast.makeText(editProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(PHONE))
                    Toast.makeText(editProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(DOB))
                    Toast.makeText(editProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else if (IMAGE_URL == null)
                    Toast.makeText(editProfileActivity.this, "Enter valid name.", Toast.LENGTH_SHORT).show();
                else {
                        storeImage(IMAGE_URL);
                }
            }
        });

    }

    private void loadProfileData(String uid){
        gUserRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF).child(UID);
        gUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NAME = String.valueOf(snapshot.child(COMMON.USER_FULL_NAME).getValue());
                gName.getEditText().setText(NAME);
                EMAIL = String.valueOf(snapshot.child(COMMON.USER_EMAIL_ID).getValue());
                gEmail.getEditText().setText(EMAIL);
                if (snapshot.hasChild(COMMON.USER_PHONE))
                {
                    PHONE = String.valueOf(snapshot.child(COMMON.USER_PHONE).getValue());
                    gPhone.getEditText().setText(PHONE);
                    DOB = String.valueOf(snapshot.child(COMMON.USER_DOB).getValue());
                    gDOB.getEditText().setText(DOB);
                    PROPIC_URL = String.valueOf(snapshot.child(COMMON.USER_PROPICURL).getValue());
                    Glide.with(editProfileActivity.this).load(PROPIC_URL).into(gPropic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallery() {
        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, GALLERY_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            IMAGE_URL = data.getData();
            gPropic.setImageURI(IMAGE_URL);
        }
    }

    private void storeImage(Uri image_url) {
        gProPicRef = firebaseHelper.gStorage.getReference().child("US_S" + image_url.getLastPathSegment() + ".jpg");
        uploadTask = gProPicRef.putFile(image_url);
        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return gProPicRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    PROPIC_URL = String.valueOf(task.getResult());
                    update(NAME,EMAIL,PHONE,DOB,PROPIC_URL);
                }
            }
        });
    }

    private void update(String name, String email, String phone, String dob, String propic_url) {

        userDataMap.put(COMMON.USER_FULL_NAME,name);
        userDataMap.put(COMMON.USER_EMAIL_ID,email);
        userDataMap.put(COMMON.USER_PHONE,phone);
        userDataMap.put(COMMON.USER_DOB,dob);
        userDataMap.put(COMMON.USER_PROPICURL,propic_url);

        gUserRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF).child(UID);
        gUserRef.updateChildren(userDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    toHome();
                else
                    Toast.makeText(editProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toHome() {
        Intent toHome = new Intent(editProfileActivity.this,homeActivity.class);
        startActivity(toHome);
        finish();
    }
}