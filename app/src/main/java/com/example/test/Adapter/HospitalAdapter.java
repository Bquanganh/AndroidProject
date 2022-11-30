package com.example.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.example.test.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    private Context context;
    private List<Hospital> hospitalList;

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
    public void onBindViewHolder(@NonNull HospitalAdapter.ViewHolder holder, int position) {
        Hospital hospital = hospitalList.get(position);
        holder.type.setText(hospital.getType());
        holder.userEmail.setText(hospital.getEmail());
        holder.userName.setText(hospital.getName());
        holder.userIdNumber.setText(hospital.getIdNumber());
        holder.userBloodGroup.setText(hospital.getBloodGroup());
        holder.userAddress.setText(hospital.getAddress());


        Glide.with(context).load(hospital.getProfilepictureurl()).error(R.mipmap.ic_launcher).into(holder.userProfileImage);
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView userProfileImage;
        public TextView type,userName,userEmail,userIdNumber,userBloodGroup,userAddress;
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
