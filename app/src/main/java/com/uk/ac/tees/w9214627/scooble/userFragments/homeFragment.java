package com.uk.ac.tees.w9214627.scooble.userFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.adapters.section1Adapter;
import com.uk.ac.tees.w9214627.scooble.adapters.singleCarAdapter;
import com.uk.ac.tees.w9214627.scooble.models.singleCarModel;
import com.uk.ac.tees.w9214627.scooble.searchActivity;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;
import com.yarolegovich.discretescrollview.DSVOrientation;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;


public class homeFragment extends Fragment {

    //section1
    private DiscreteScrollView gDSV;
    private section1Adapter adapter;
    private InfiniteScrollAdapter gDSVadapter;
    private List<singleCarModel> singleCarModelList = new ArrayList<>();

    //section2
    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private singleCarAdapter section2Adapter;

    //firebase
    private DatabaseReference gPopCarsRef;

    private EditText gSearch;



    public homeFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        gDSV = (DiscreteScrollView)view.findViewById(R.id.homeFrag_discreteScrollView);
        adapter = new section1Adapter(singleCarModelList,getContext());
        gDSVadapter = InfiniteScrollAdapter.wrap(adapter);
        gDSV.setOrientation(DSVOrientation.HORIZONTAL);
        gDSV.setAdapter(gDSVadapter);
        gDSV.setItemTransitionTimeMillis(170);
        gDSV.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());

        gRCV = (RecyclerView)view.findViewById(R.id.homeFrag_RCV);
        gLLM = new LinearLayoutManager(getActivity());
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setNestedScrollingEnabled(true);
        gRCV.setHasFixedSize(true);

        section2Adapter = new singleCarAdapter(singleCarModelList,getContext());
        gRCV.setAdapter(section2Adapter);

        gSearch = (EditText)view.findViewById(R.id.homeFrag_search);
        gSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSearch();
            }
        });

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
                    adapter.notifyDataSetChanged();
                    section2Adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void toSearch() {
        Intent toSearch = new Intent(getActivity(), searchActivity.class);
        startActivity(toSearch);
    }

    @Override
    public void onStart() {
        super.onStart();
        singleCarModelList.clear();
    }
}