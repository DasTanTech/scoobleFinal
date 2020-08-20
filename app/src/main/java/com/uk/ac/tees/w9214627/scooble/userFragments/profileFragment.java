package com.uk.ac.tees.w9214627.scooble.userFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.editProfileActivity;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import de.hdodenhof.circleimageview.CircleImageView;


public class profileFragment extends Fragment {

   private TextInputLayout gName,gEmail,gPhone,gDOB;
   private CircleImageView gPropic;
   private Button gEditProfile,gLogout;

   private DatabaseReference gUserRef;
   private String UID;

    public profileFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        UID = firebaseHelper.gAuth.getUid();

        gName = (TextInputLayout)view.findViewById(R.id.profileFrag_name);
        gEmail = (TextInputLayout)view.findViewById(R.id.profileFrag_email);
        gPhone = (TextInputLayout)view.findViewById(R.id.profileFrag_phone);
        gDOB = (TextInputLayout)view.findViewById(R.id.profileFrag_dob);
        gPropic = (CircleImageView)view.findViewById(R.id.profileFrag_imageView);
        gEditProfile = (Button) view.findViewById(R.id.profileFrag_editProfileBtn);
        gLogout = (Button) view.findViewById(R.id.profileFrag_logoutBtn);

        gUserRef = firebaseHelper.gDatabase.getReference().child(COMMON.USERS_REF).child(UID);
        gUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gName.getEditText().setText(String.valueOf(snapshot.child(COMMON.USER_FULL_NAME).getValue()));
                gEmail.getEditText().setText(String.valueOf(snapshot.child(COMMON.USER_EMAIL_ID).getValue()));
                if (!snapshot.hasChild(COMMON.USER_PHONE))
                {
                    gPhone.getEditText().setText("Not Updated");
                    gDOB.getEditText().setText("Not Updated");
                }else{
                    gPhone.getEditText().setText(String.valueOf(snapshot.child(COMMON.USER_PHONE).getValue()));
                    gDOB.getEditText().setText(String.valueOf(snapshot.child(COMMON.USER_DOB).getValue()));
                    Glide.with(getContext()).load(String.valueOf(snapshot.child(COMMON.USER_PROPICURL).getValue())).into(gPropic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditProfie();
            }
        });

        gLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseHelper.logoutUser(getActivity());
            }
        });

        return view;
    }

    private void toEditProfie() {
        Intent toEditProfile = new Intent(getActivity(), editProfileActivity.class);
        startActivity(toEditProfile);
    }
}