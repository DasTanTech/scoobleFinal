package com.uk.ac.tees.w9214627.scooble.userFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.adapters.singleCarAdapter;
import com.uk.ac.tees.w9214627.scooble.models.singleCarModel;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.ArrayList;
import java.util.List;


public class savedFragment extends Fragment {

    private String UID;

    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private List<singleCarModel> singleCarModelList = new ArrayList<>();
    private singleCarAdapter adapter;

    private DatabaseReference gCarsRef;


    public savedFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        UID = firebaseHelper.gUser.getUid();

        gRCV = (RecyclerView)view.findViewById(R.id.savedFrag_RCV);
        gLLM = new LinearLayoutManager(getActivity());
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setNestedScrollingEnabled(true);
        gRCV.setHasFixedSize(true);

        adapter = new singleCarAdapter(singleCarModelList,getContext());
        gRCV.setAdapter(adapter);

        gCarsRef = firebaseHelper.gDatabase.getReference().child(COMMON.CARS_REF);

        gCarsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot car : snapshot.getChildren())
                    {
                        if (car.child(COMMON.SAVED_USERS).hasChild(UID))
                        {
                            singleCarModelList.add(new singleCarModel(
                                    String.valueOf(car.getKey()),
                                    String.valueOf(car.child(COMMON.CAR_COMPANY).getValue()),
                                    String.valueOf(car.child(COMMON.CAR_MODEL).getValue()),
                                    String.valueOf(car.child(COMMON.CAR_PICURL).getValue())
                            ));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        singleCarModelList.clear();
    }
}