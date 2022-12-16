package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Adapter.DonorAdapter;
import com.example.test.Adapter.HospitalAdapter;
import com.example.test.Adapter.UserAdapter;
import com.example.test.Common.Common;
import com.example.test.Model.AllHospitals;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingForHospital extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public Button accept;
    public String idOfRecipient;
    List<String> idList;
    List<User> userList;
    DonorAdapter userAdapter;

    public String getIdOfRecipient() {
        return idOfRecipient;
    }

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
        userAdapter = new DonorAdapter(BookingForHospital.this,userList);
        recyclerView.setAdapter(userAdapter);
        idList = new ArrayList<>();
        Intent intent = getIntent();
        idOfRecipient=intent.getStringExtra("idOfRecipient");

        Common.currentRecipient = idOfRecipient;



        showUsers();
    }
    private void showUsers() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<AllHospitals> list = new ArrayList<>();
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);

                }


                userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
