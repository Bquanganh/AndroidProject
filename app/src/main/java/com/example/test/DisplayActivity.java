package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.example.test.Adapter.UserAdapter;
import com.example.test.Common.Common;
import com.example.test.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.annotation.GlideModule;




import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_user_image;
    private TextView nav_user_name, nav_user_email,nav_user_bloodGroup,nav_user_type;



    private RecyclerView recycleView;
    private ProgressBar progressbar;

    private List<User> userList;
    private UserAdapter userAdapter;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Donation App");

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(DisplayActivity.this,drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        progressbar = findViewById(R.id.progressbar);

        recycleView = findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycleView.setLayoutManager(layoutManager);
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recycleView.setAdapter(userAdapter);



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if(type.equals("donor")){
                    readRecipients();
//                    readHospital();
                }
                if(type.equals("recipient")){
                    readDonors();
//
                }else{
                    readForHospital();
                }
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        nav_user_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_user_name = nav_view.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_user_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_user_bloodGroup = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodGroup);
        nav_user_type = nav_view.getHeaderView(0).findViewById(R.id.nav_user_type);


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue().toString();
                    nav_user_name.setText(name);

                    String email = snapshot.child("email").getValue().toString();
                    nav_user_email.setText(email);

                    String bloodGroup = snapshot.child("bloodGroup").getValue().toString();
                    nav_user_bloodGroup.setText(bloodGroup);

                    String type = snapshot.child("type").getValue().toString();
                    nav_user_type.setText(type);

                    if(snapshot.hasChild("profilepictureurl")){
                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_user_image);
                    }else{
                        nav_user_image.setImageResource(R.drawable.profile);
                    }

                    Menu nav_menu = nav_view.getMenu();

                    if (type.equals("donor")){
                        nav_menu.findItem(R.id.sentEmail).setTitle("Received Emails");
                        nav_menu.findItem(R.id.notifications).setVisible(true);
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void readForHospital() {
        Intent intent = new Intent(DisplayActivity.this,BookingForHospital.class);
        startActivity(intent);
    }

    private void readDonors() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = reference.orderByChild("type").equalTo("donor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);

                if (userList.isEmpty()){
                    Toast.makeText(DisplayActivity.this,"No Donors",Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readRecipients() {

        Intent intent = new Intent(DisplayActivity.this,SendEmailActivity.class);
        startActivity(intent);
    }

//    private void readHospital() {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
//        Query query = reference.orderByChild("type").equalTo("hospital");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    User user = dataSnapshot.getValue(User.class);
//                    userList.add(user);
//                }
//                userAdapter.notifyDataSetChanged();
//                progressbar.setVisibility(View.GONE);
//
//                if (userList.isEmpty()){
//                    Toast.makeText(DisplayActivity.this,"No Hospital",Toast.LENGTH_SHORT).show();
//                    progressbar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.aplus:
                Intent intent2 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent2.putExtra("group","A+");
                startActivity(intent2);
                break;

            case R.id.aminus:
                Intent intent3 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent3.putExtra("group","A-");
                startActivity(intent3);
                break;

            case R.id.abplus:
                Intent intent4 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent4.putExtra("group","AB+");
                startActivity(intent4);
                break;
            case R.id.abminus:
                Intent intent5 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent5.putExtra("group","AB-");
                startActivity(intent5);
                break;

            case R.id.bplus:
                Intent intent6 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent6.putExtra("group","B+");
                startActivity(intent6);
                break;

            case R.id.bminus:
                Intent intent7 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent7.putExtra("group","B-");
                startActivity(intent7);
                break;

            case R.id.oplus:
                Intent intent8 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent8.putExtra("group","O+");
                startActivity(intent8);
                break;

            case R.id.ominus:
                Intent intent9 = new Intent(DisplayActivity.this, CategorySelectedActivity.class);
                intent9.putExtra("group","O-");
                startActivity(intent9);
                break;
            case R.id.sentEmail:
                Intent intent11 = new Intent(DisplayActivity.this, SendEmailActivity.class);
                startActivity(intent11);
                break;


            case R.id.profile:
                Intent intent = new Intent(DisplayActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent1 = new Intent(DisplayActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.notifications:
                Intent intent13 = new Intent(DisplayActivity.this, NotificationActivity.class);
                startActivity(intent13);
                break;
            case R.id.bookingSchedule:
                Intent intent14 = new Intent(DisplayActivity.this, BookingDonationActivity.class);
                startActivity(intent14);
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



}