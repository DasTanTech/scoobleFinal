package com.uk.ac.tees.w9214627.scooble.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.carActivity;
import com.uk.ac.tees.w9214627.scooble.models.singleCarModel;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;

import java.util.List;

public class section1Adapter extends RecyclerView.Adapter<section1Adapter.car_VH> {

    private List<singleCarModel> singleCarModelList;
    private Context context;

    public section1Adapter(List<singleCarModel> singleCarModelList, Context context) {
        this.singleCarModelList = singleCarModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public car_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_section1_layout,parent,false);
        return new car_VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull car_VH holder, int position) {
        final singleCarModel curItem = singleCarModelList.get(position);
        holder.VHcompany.setText(curItem.getCarCompany());
        holder.VHmodel.setText(curItem.getCarModel());
        Glide.with(context).load(curItem.getCarPicURL()).into(holder.VHpic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toCar = new Intent(context, carActivity.class);
                toCar.putExtra(COMMON.CAR_ID,curItem.getCID());
                context.startActivity(toCar);
            }
        });

    }

    @Override
    public int getItemCount() {
        return singleCarModelList.size();
    }

    public class car_VH extends RecyclerView.ViewHolder {

        private TextView VHcompany,VHmodel;
        private ImageView VHpic;

        public car_VH(@NonNull View itemView) {
            super(itemView);
            VHcompany = (TextView)itemView.findViewById(R.id.singleSection1_company);
            VHmodel = (TextView)itemView.findViewById(R.id.singleSection1_model);
            VHpic = (ImageView)itemView.findViewById(R.id.singleSection1_imageView);
        }
    }
}
