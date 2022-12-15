package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.Adapter.MyViewPagerAdapter;
import com.example.test.Common.Common;
import com.example.test.Common.NonSwoperViewPager;
import com.example.test.Fragments.BookingStep2Fragment;
import com.example.test.Fragments.BookingStep3Fragment;
import com.example.test.Model.AllHospitals;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shuhart.stepview.StepView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


public class BookingFor2UsersActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;

    private Button btn_previous_step,btn_next_step;
    private Toolbar toolbar;
    private StepView stepView;
    private NonSwoperViewPager viewPager;
    private String hospitalId,idOfRecipient;
    private BookingDonationActivity bookingDonationActivity;

     public List<Hospital> list;
    public User user;



    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if (step ==0)
            {
                Common.currentHospital = intent.getParcelableExtra(Common.KEY_HOSPITAL);
            }else if (step ==1)
            {   Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT,-1);

            }
            Common.currentHospital = intent.getParcelableExtra(Common.KEY_HOSPITAL);
            btn_next_step.setEnabled(true);
            setupColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public String getIdOfRecipient() {
        return idOfRecipient;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_for2_users);
        Intent intent = getIntent();


        hospitalId = intent.getStringExtra("hospitalId");


//        Log.d("id",hospitalId);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.commit();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking Donation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btn_previous_step= findViewById(R.id.btn_previous_step);
        btn_next_step= findViewById(R.id.btn_next_step);
        stepView= findViewById(R.id.step_view);
        viewPager= findViewById(R.id.view_pager);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver,new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));
        setupStepView();
        setupColorButton();
        Log.d("Step", String.valueOf(Common.step));


        idOfRecipient = Common.currentRecipient;


        Log.d("Test12h38", Common.currentRecipient);







        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                stepView.go(position,true);
                if(position==0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                setupColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.step <2 || Common.step ==0){
                    Common.step++;
                    Log.d("Step", String.valueOf(Common.step));
                    if (Common.step ==0)
                    {
                        loadTimeSchedule(hospitalId);
                    }else if (Common.step ==1){

                            confirmBooking();

                    }

                    viewPager.setCurrentItem(Common.step);
        }
            }
            private void loadTimeSchedule(String hospitalID) {
                Intent i = new Intent( Common.KEY_DISPLAY_TIME_SLOT);
                localBroadcastManager.sendBroadcast(i);
            }

            private void confirmBooking() {
                Intent i = new Intent(Common.KEY_CONFIRM_BOOKING);
                localBroadcastManager.sendBroadcast(i);
            }
        });
        btn_previous_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Step", String.valueOf(Common.step));
                if(Common.step ==2 || Common.step > 0)
                {
                    Common.step --;
                    viewPager.setCurrentItem(Common.step);
                }
            }
        });

    }


    private void setupColorButton() {
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.ColorPrimary);
        }else{
            btn_next_step.setBackgroundResource(R.color.purple_200);
        }
        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.ColorPrimary);
        }else{
            btn_previous_step.setBackgroundResource(R.color.purple_200);
        }
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Time");
        stepList.add("Confirm");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
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