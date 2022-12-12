package com.example.test.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Adapter.MyTimeSlotAdapter;
import com.example.test.Common.Common;
import com.example.test.Common.SpacesItemDecoration;
import com.example.test.Interface.TimeSlotLoadListener;
import com.example.test.Model.TimeSlot;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment implements TimeSlotLoadListener {
    private RecyclerView recyclerView;
    TimeSlotLoadListener timeSlotLoadListener;
    LocalBroadcastManager localBroadcastManager;

    Calendar selected_date;
    Unbinder unbinder;
    @BindView(R.id.recycle_time_slot)
    RecyclerView recycle_time_slot;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE,0);
            loadAvailableTimeSlotOfHospital(Common.currentHospital.getId(),simpleDateFormat.format(date.getTime()));
        }

        private void loadAvailableTimeSlotOfHospital(String id, String date) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("hospitals").child(Common.currentHospital.getCity()).child("hospitals").child(id);
            ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        DataSnapshot snapshot = task.getResult();
                        if (snapshot.exists())
                        {
                            DatabaseReference bookDate = FirebaseDatabase.getInstance().getReference()
                                    .child("hospitals").child(Common.currentHospital.getCity()).child("hospitals").child(Common.currentHospital.getId())
                                    .child(date);
                            bookDate.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists())
                                    {
                                        List<TimeSlot> timeSlots = new ArrayList<>();
                                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                                        {
                                            TimeSlot timeSlot = snapshot1.getValue(TimeSlot.class);
                                            timeSlots.add(timeSlot);
                                            timeSlotLoadListener.onTimeSlotLoadSuccess(timeSlots);
                                        }
                                    }else
                                    {
                                        timeSlotLoadListener.onTimeSlotLoadEmpty();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    timeSlotLoadListener.onTimeSlotLoadFailed(e.getMessage());
                }
            });
        }
    };
    static  BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if (instance==null)
            instance= new BookingStep2Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeSlotLoadListener =this;

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot,new IntentFilter(Common.KEY_DISPLAY_TIME_SLOT));
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE,0);


    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView = inflater.inflate(R.layout.fragment_booking_step_two,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        init(itemView);
        return  itemView;
    }

    private void init(View itemView) {
        recycle_time_slot.setHasFixedSize(true);
        GridLayoutManager  gridLayoutManager = new GridLayoutManager(getActivity(),3);
        recycle_time_slot.setLayoutManager(gridLayoutManager);
        recycle_time_slot.addItemDecoration(new SpacesItemDecoration(8));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE,0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE,2);


    }

    @Override
    public void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList) {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext(),timeSlotList);
        recycle_time_slot.setAdapter(adapter);
    }

    @Override
    public void onTimeSlotLoadFailed(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSlotLoadEmpty() {
        MyTimeSlotAdapter adapter = new MyTimeSlotAdapter(getContext());
        recycle_time_slot.setAdapter(adapter);
    }
}
