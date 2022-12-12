package com.example.test.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.test.Common.Common;
import com.example.test.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep3Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    @BindView(R.id.txt_booking_location_text)
    TextView txt_booking_location_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_hospital_address_text)
    TextView txt_hospital_address_text;
    @BindView(R.id.txt_hospital_name)
    TextView txt_hospital_name;
    @BindView(R.id.txt_hospital_open_hours)
    TextView txt_hospital_open_hours;
    @BindView(R.id.txt_hospital_phone_text)
    TextView txt_hospital_phone_text;
    @BindView(R.id.txt_hospital_web)
    TextView txt_hospital_web;

    BroadcastReceiver confirmBookingReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }

        private void setData() {
            txt_booking_location_text.setText(Common.currentHospital.getName());
            txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
            .append(" at ")
            .append(simpleDateFormat.format(Common.currentDate.getTime())));

            txt_hospital_address_text.setText(Common.currentHospital.getAddress());
            txt_hospital_web.setText(Common.currentHospital.getWebsite());
            txt_hospital_open_hours.setText(Common.currentHospital.getOpenHours());
            txt_hospital_name.setText(Common.currentHospital.getName());
        }
    };



    static  BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if (instance==null)
            instance= new BookingStep3Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceive,new IntentFilter(Common.KEY_CONFIRM_BOOKING));

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceive);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_booking_step_three,container,false);
        unbinder = ButterKnife.bind(this,view);
        return  view;
    }
}
