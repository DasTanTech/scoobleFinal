package com.uk.ac.tees.w9214627.scooble.agencyFragments;

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
import com.google.firebase.database.ValueEventListener;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.adapters.bookingAdapter;
import com.uk.ac.tees.w9214627.scooble.models.bookingModel;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;
import com.uk.ac.tees.w9214627.scooble.util.firebaseHelper;

import java.util.ArrayList;
import java.util.List;


public class agencyBookingsFragment extends Fragment {

    private String AID;

    private RecyclerView gRCV;
    private LinearLayoutManager gLLM;
    private List<bookingModel> bookingModelList = new ArrayList<>();
    private bookingAdapter adapter;

    private DatabaseReference gBookingsRef;

    public agencyBookingsFragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agency_bookings, container, false);

        AID = firebaseHelper.gUser.getUid();


        gRCV = (RecyclerView)view.findViewById(R.id.agencyBookingsFrag_RCV);
        gLLM = new LinearLayoutManager(getActivity());
        gLLM.setOrientation(LinearLayoutManager.VERTICAL);
        gRCV.setLayoutManager(gLLM);
        gRCV.setNestedScrollingEnabled(true);
        gRCV.setHasFixedSize(true);

        adapter = new bookingAdapter(bookingModelList,getContext(),1);
        gRCV.setAdapter(adapter);

        gBookingsRef = firebaseHelper.gDatabase.getReference().child(COMMON.AGENCY_REF).child(AID).child(COMMON.BOOKINGS);

        gBookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot booking : snapshot.getChildren())
                    {
                        bookingModelList.add(new bookingModel(
                                booking.getKey(),
                                String.valueOf(booking.child(COMMON.CAR_COMPANY).getValue()),
                                String.valueOf(booking.child(COMMON.CAR_MODEL).getValue()),
                                String.valueOf(booking.child(COMMON.CAR_PICURL).getValue()),
                                String.valueOf(booking.child(COMMON.CAR_CITY).getValue()),
                                String.valueOf(booking.child(COMMON.BOOKING_DATE).getValue()),
                                String.valueOf(booking.child(COMMON.AGENCY_ID).getValue()),
                                String.valueOf(booking.child(COMMON.USER_ID).getValue())
                        ));
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
        bookingModelList.clear();
    }

}