package com.example.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Common.Common;
import com.example.test.Interface.RecycleItemSelected;
import com.example.test.Model.AllHospitals;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public class MyAllHospitalAdapter extends RecyclerView.Adapter<MyAllHospitalAdapter.MyViewHolder> {

    Context context;
    List<AllHospitals> allHospitalsList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyAllHospitalAdapter(Context context, List<AllHospitals> allHospitalsList) {
        this.context = context;
        this.allHospitalsList = allHospitalsList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyAllHospitalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_all_hospital,parent,false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAllHospitalAdapter.MyViewHolder holder, int position) {
        AllHospitals allHospitals = allHospitalsList.get(position);

        holder.txt_allHospital_name.setText(allHospitalsList.get(position).getName());
        holder.txt_allHospital_address.setText(allHospitalsList.get(position).getAddress());
        if(!cardViewList.contains(holder.card_hospital)){
            cardViewList.add(holder.card_hospital);
        }

        holder.setRecycleItemSelected(new RecycleItemSelected() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for (CardView cardView:cardViewList){
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
                }
                holder.card_hospital.setCardBackgroundColor(context.getResources().getColor(R.color.purple_200));

                Intent i = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                i.putExtra(Common.KEY_HOSPITAL,allHospitalsList.get(pos));
                i.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allHospitalsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_allHospital_name,txt_allHospital_address;
        CardView card_hospital;

        RecycleItemSelected recycleItemSelected;

        public void setRecycleItemSelected(RecycleItemSelected recycleItemSelected) {

            this.recycleItemSelected = recycleItemSelected;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_hospital = itemView.findViewById(R.id.card_hospital);
            txt_allHospital_name = itemView.findViewById(R.id.txt_allHospital_name);
            txt_allHospital_address = itemView.findViewById(R.id.txt_allHospital_address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recycleItemSelected.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
