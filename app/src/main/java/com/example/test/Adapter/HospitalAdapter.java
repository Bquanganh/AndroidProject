package com.example.test.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.BookingDonationActivity;
import com.example.test.BookingFor2UsersActivity;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.example.test.R;
import com.example.test.SendEmailActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    private Context context;
    private List<Hospital> hospitalList;
    ViewHolder viewHolder;

    public HospitalAdapter (Context context, List<Hospital> hospitalList){
        this.context = context;
        this.hospitalList = hospitalList;
    }

    public void setData(List<Hospital> list){
        this.hospitalList=list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HospitalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_booking_donation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Hospital hospital = hospitalList.get(position);
        holder.type.setText(hospital.getType());
        holder.userEmail.setText(hospital.getEmail());
        holder.userName.setText(hospital.getName());
        holder.userIdNumber.setText(hospital.getIdNumber());
        holder.userBloodGroup.setText(hospital.getBloodGroup());
        holder.userAddress.setText(hospital.getAddress());
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new AlertDialog.Builder(context)
//                        .setTitle("Booking").setMessage("Send mail to" + hospital.getName()+ "?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent i = new Intent(viewHolder.itemView.getContext(),BookingFor2UsersActivity.class);
//                                startActivity
//                            }
//                        });

                    Intent i = new Intent(context, BookingFor2UsersActivity.class);
                    i.putExtra("city",position);
                    i.putExtra("hospitalId",hospital.getId());
                    Bundle bundle = new Bundle();
                    bundle.putString("hospitalName",holder.userName.toString());
//                    bundle.putString("hospitalId",hospital.getId());
                    bundle.putString("hospitalAddress",holder.userAddress.toString());
                    context.startActivity(i);
            }
        });


        Glide.with(context).load(hospital.getProfilepictureurl()).error(R.mipmap.ic_launcher).into(holder.userProfileImage);
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userProfileImage;
        public TextView type,userName,userEmail,userIdNumber,userBloodGroup,userAddress,city;
        public Button bookButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            type = itemView.findViewById(R.id.type);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userAddress = itemView.findViewById(R.id.userAddress);
            userIdNumber = itemView.findViewById(R.id.userIdNumber);
            userBloodGroup = itemView.findViewById(R.id.userBloodGroup);
            bookButton = itemView.findViewById(R.id.bookButton);

        }
    }
}
