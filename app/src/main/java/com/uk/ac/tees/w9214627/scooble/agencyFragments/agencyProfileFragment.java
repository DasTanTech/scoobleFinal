package com.uk.ac.tees.w9214627.scooble.agencyFragments;

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
import com.uk.ac.tees.w9214627.scooble.agencyEditProfileActivity;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import de.hdodenhof.circleimageview.CircleImageView;


public class agencyProfileFragment extends Fragment {

    private TextInputLayout gName,gEmail,gPhone;
    private Button gEditProfile,gLogout;

    private DatabaseReference gAgencyRef;
    private String AID;

    public agencyProfileFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agency_profile, container, false);

        AID = firebaseHelper.gAuth.getUid();

        gName = (TextInputLayout)view.findViewById(R.id.agencyProfileFrag_name);
        gEmail = (TextInputLayout)view.findViewById(R.id.agencyProfileFrag_email);
        gPhone = (TextInputLayout)view.findViewById(R.id.agencyProfileFrag_phone);
        gEditProfile = (Button) view.findViewById(R.id.agencyProfileFrag_editProfileBtn);
        gLogout = (Button) view.findViewById(R.id.agencyProfileFrag_logoutBtn);

        gEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toEditProfile();
            }
        });

        gAgencyRef = firebaseHelper.gDatabase.getReference().child(COMMON.AGENCY_REF).child(AID);
        gAgencyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gName.getEditText().setText(String.valueOf(snapshot.child(COMMON.AGENCY_NAME).getValue()));
                gEmail.getEditText().setText(String.valueOf(snapshot.child(COMMON.AGENCY_EMAIL).getValue()));
                gPhone.getEditText().setText(String.valueOf(snapshot.child(COMMON.AGENCY_PHONE).getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    private void toEditProfile() {
        Intent toEditProfile = new Intent(getActivity(), agencyEditProfileActivity.class);
        startActivity(toEditProfile);
    }
}