package com.uk.ac.tees.w9214627.scooble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.adapters.singleCarAdapter;
import com.uk.ac.tees.w9214627.scooble.models.singleCarModel;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    //section2
    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private singleCarAdapter section2Adapter;
    private List<singleCarModel> singleCarModelList = new ArrayList<>();

    //firebase
    private DatabaseReference gPopCarsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        gRCV = (RecyclerView)findViewById(R.id.search_RCV);
        gLLM = new LinearLayoutManager(this);
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setNestedScrollingEnabled(true);
        gRCV.setHasFixedSize(true);

        section2Adapter = new singleCarAdapter(singleCarModelList,this);
        gRCV.setAdapter(section2Adapter);

        gPopCarsRef = firebaseHelper.gDatabase.getReference().child(COMMON.CARS_REF);
        gPopCarsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot car : snapshot.getChildren())
                    {

                        singleCarModelList.add(new singleCarModel(
                                String.valueOf(car.getKey()),
                                String.valueOf(car.child(COMMON.CAR_COMPANY).getValue()),
                                String.valueOf(car.child(COMMON.CAR_MODEL).getValue()),
                                String.valueOf(car.child(COMMON.CAR_PICURL).getValue())
                        ));
                    }
                    section2Adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}