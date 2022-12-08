package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.Adapter.MyViewPagerAdapter;
import com.example.test.Common.Common;
import com.example.test.Common.NonSwoperViewPager;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;


public class BookingFor2UsersActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;

    private Button btn_previous_step,btn_next_step;
    private Toolbar toolbar;
    private StepView stepView;
    private NonSwoperViewPager viewPager;

    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(Common.step ==3 || Common.step > 0)
        {
            Common.step --;
            viewPager.setCurrentItem(Common.step);
        }
    }
    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if (Common.step <3 || Common.step ==0){
            Common.step++;
            if (Common.step ==1){
                if (Common.currentHospital !=null){
                    loadTimeSchedule(Common.currentHospital.getHospitalID());
                }
            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void loadTimeSchedule(String hospitalID) {
    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_for2_users);

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

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
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
        stepList.add("Location");
        stepList.add("Hospitals");
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