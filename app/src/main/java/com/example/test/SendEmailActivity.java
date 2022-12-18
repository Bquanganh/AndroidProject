package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.test.Adapter.BookingAdapter;
import com.example.test.Adapter.RequestDonationAdapter;
import com.example.test.Adapter.UserAdapter;
import com.example.test.Model.BookingInformation;
import com.example.test.Model.RequestDonation;
import com.example.test.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SendEmailActivity extends AppCompatActivity {
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private TextView emptyView;
    public Button accept;



    List<String> idList;
    List<RequestDonation> requestDonationList;


    RequestDonationAdapter requestDonationAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("People sent Emails");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = findViewById(R.id.recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        requestDonationList = new ArrayList<>();
        requestDonationAdapter = new RequestDonationAdapter(SendEmailActivity.this,requestDonationList);
        recyclerView.setAdapter(requestDonationAdapter);

        emptyView = findViewById(R.id.empty_view);
        
        idList = new ArrayList<>();
        getIdOfUsers();





    }



    private void getIdOfUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("emails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                if (snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                        idList.add(dataSnapshot.getKey());
                    }
                }else
                {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }




                showSchedule();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showSchedule() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("emails").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RequestDonation bookingInformation = dataSnapshot.getValue(RequestDonation.class);

                    for (String id : idList){
                        if (bookingInformation.getCustomerName().equals(id)){
                            requestDonationList.add(bookingInformation);
                        }

                    }

                }
                requestDonationAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
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