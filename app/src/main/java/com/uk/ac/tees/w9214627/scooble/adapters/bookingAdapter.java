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
import com.google.android.material.textfield.TextInputLayout;
import com.uk.ac.tees.w9214627.scooble.R;
import com.uk.ac.tees.w9214627.scooble.bookingConfirmationActivity;
import com.uk.ac.tees.w9214627.scooble.bookingUserActivity;
import com.uk.ac.tees.w9214627.scooble.models.bookingModel;
import com.uk.ac.tees.w9214627.scooble.util.COMMON;

import java.util.List;

public class bookingAdapter extends RecyclerView.Adapter<bookingAdapter.booking_VH> {

    private List<bookingModel> bookingModelList;
    private Context context;
    private int userCheck;

    public bookingAdapter(List<bookingModel> bookingModelList, Context context,int userCheck) {
        this.bookingModelList = bookingModelList;
        this.context = context;
        this.userCheck = userCheck;
    }

    @NonNull
    @Override
    public booking_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_booking_layout,parent,false);
        return new booking_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull booking_VH holder, int position) {
        final bookingModel curBooking = bookingModelList.get(position);
        holder.cCompany.setText(curBooking.getCAR_COMPANY());
        holder.cModel.setText(curBooking.getCAR_MODEL());
        Glide.with(context).load(curBooking.getCAR_PICURL()).into(holder.cPic);
        holder.cPPlace.getEditText().setText(curBooking.getCAR_PICKPLACE());
        String[] datee = curBooking.getCAR_PICKDATE().split("-");
        String DATE = datee[0]+" "+ COMMON.getMonth(datee[1]);
        holder.cPDate.getEditText().setText(DATE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userCheck == 0)
                {
                    Intent toBookingConfirmation = new Intent(context, bookingConfirmationActivity.class);
                    toBookingConfirmation.putExtra(COMMON.BOOKING_ID,curBooking.getBID());
                    toBookingConfirmation.putExtra(COMMON.AGENCY_ID,curBooking.getUID());
                    context.startActivity(toBookingConfirmation);
                }
                else if (userCheck == 1)
                {
                    Intent toBookingConfirmationAgency = new Intent(context, bookingUserActivity.class);
                    toBookingConfirmationAgency.putExtra(COMMON.BOOKING_ID,curBooking.getBID());
                    toBookingConfirmationAgency.putExtra(COMMON.USER_ID,curBooking.getAID());
                    context.startActivity(toBookingConfirmationAgency);
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingModelList.size();
    }

    public class booking_VH extends RecyclerView.ViewHolder {

        private TextView cCompany,cModel;
        private ImageView cPic;
        private TextInputLayout cPDate,cPPlace;

        public booking_VH(@NonNull View itemView) {
            super(itemView);
            cCompany = (TextView)itemView.findViewById(R.id.singleBookingLayout_company);
            cModel = (TextView)itemView.findViewById(R.id.singleBookingLayout_model);
            cPic = (ImageView)itemView.findViewById(R.id.singleBookingLayout_imageView);
            cPPlace = (TextInputLayout)itemView.findViewById(R.id.singleBookingLayout_city);
            cPDate = (TextInputLayout)itemView.findViewById(R.id.singleBookingLayout_date);
        }
    }
}
