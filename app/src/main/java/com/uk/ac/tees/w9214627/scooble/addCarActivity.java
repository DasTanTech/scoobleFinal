package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.HashMap;

public class addCarActivity extends AppCompatActivity {

    private EditText gSpeed,gSeats,gEngine,gLat,gLon,gPrice;
    private TextInputLayout gCompany,gModel,gDesc,gCity;
    private ImageView gPic;
    private Button gAddCarBtn;

    private String COMPANY,MODEL,DESC,SPEED,SEATS,ENGINE,CITY,LAT,LON,PRICE,PICURL,CID,AID;
    private static final int GALLERY_PICK = 1;
    private Uri IMAGE_URL = null;

    private StorageReference gProPicRef;
    private UploadTask uploadTask;

    private DatabaseReference gNewCarRef;
    private HashMap<String,String> carDataMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        AID = firebaseHelper.gUser.getUid();

        gCompany = (TextInputLayout)findViewById(R.id.addCar_company);
        gModel = (TextInputLayout)findViewById(R.id.addCar_model);
        gDesc = (TextInputLayout)findViewById(R.id.addCar_desc);
        gSpeed = (EditText)findViewById(R.id.addCar_speed);
        gSeats = (EditText)findViewById(R.id.addCar_seats);
        gEngine = (EditText)findViewById(R.id.addCar_engine);
        gCity = (TextInputLayout)findViewById(R.id.addCar_city);
        gLat = (EditText)findViewById(R.id.addCar_lat);
        gLon = (EditText)findViewById(R.id.addCar_lng);
        gPrice = (EditText)findViewById(R.id.addCar_price);
        gPic = (ImageView)findViewById(R.id.addCar_imageView);
        gAddCarBtn = (Button)findViewById(R.id.addCar_btn);

        gPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        gAddCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                COMPANY = gCompany.getEditText().getText().toString();
                MODEL = gModel.getEditText().getText().toString();
                DESC = gDesc.getEditText().getText().toString();
                SPEED = gSpeed.getText().toString();
                SEATS  = gSeats.getText().toString();
                ENGINE = gEngine.getText().toString();
                CITY = gCity.getEditText().getText().toString();
                LAT = gLat.getText().toString();
                LON = gLon.getText().toString();
                PRICE = gPrice.getText().toString();

                if (TextUtils.isEmpty(COMPANY))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(MODEL))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(DESC))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(SPEED))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(SEATS))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(ENGINE))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(CITY))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(LAT))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(LON))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(PRICE))
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else if (IMAGE_URL == null)
                    Toast.makeText(addCarActivity.this, "Enter valid company.", Toast.LENGTH_SHORT).show();
                else
                {
                    storeImage(IMAGE_URL);
                }
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
            gPic.setImageURI(IMAGE_URL);
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
                    PICURL = String.valueOf(task.getResult());
                    addCar(COMPANY,MODEL,DESC,SPEED,SEATS,ENGINE,CITY,LAT,LON,PRICE,PICURL);
                }
            }
        });
    }

    private void addCar(String company, String model, String desc, String speed, String seats, String engine, String city, String lat, String lon, String price, String picurl) {

        carDataMap.put(COMMON.CAR_COMPANY,company);
        carDataMap.put(COMMON.CAR_MODEL,model);
        carDataMap.put(COMMON.CAR_DESC,desc);
        carDataMap.put(COMMON.CAR_SPEED,speed);
        carDataMap.put(COMMON.CAR_SEATS,seats);
        carDataMap.put(COMMON.CAR_ENGINE,engine);
        carDataMap.put(COMMON.CAR_CITY,city);
        carDataMap.put(COMMON.CAR_LAT,lat);
        carDataMap.put(COMMON.CAR_LNG,lon);
        carDataMap.put(COMMON.CAR_PRICE,price);
        carDataMap.put(COMMON.CAR_PICURL,picurl);
        carDataMap.put(COMMON.CAR_AGENCY_ID,AID);

        CID = firebaseHelper.gDatabase.getReference().child(COMMON.CARS_REF).push().getKey();

        gNewCarRef = firebaseHelper.gDatabase.getReference().child(COMMON.CARS_REF).child(CID);
        gNewCarRef.setValue(carDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    toAgencyHome();
                else
                    Toast.makeText(addCarActivity.this, "Error.", Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void toAgencyHome() {
        Intent toAgencyHome = new Intent(addCarActivity.this,agencyHomeActivity.class);
        startActivity(toAgencyHome);
        finish();
    }
}