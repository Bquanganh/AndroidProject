package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.test.Adapter.BookingAdapter;
import com.example.test.Adapter.HospitalAdapter;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingDonationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public Button accept;


    List<String> idList;
    List<Hospital> userList;
    HospitalAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_schedule);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking Schedule Hospital");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userList = new ArrayList<>();
        userAdapter = new HospitalAdapter(BookingDonationActivity.this,userList);
        recyclerView.setAdapter(userAdapter);
        idList = new ArrayList<>();
        showUsers();

    }
    private void showUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("hospitals");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    userList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Hospital hospital = dataSnapshot.getValue(Hospital.class);
                        userList.add(hospital);
                    }
                    userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}