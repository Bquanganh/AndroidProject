package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.example.test.Adapter.BookingAdapter;
import com.example.test.Adapter.HospitalAdapter;
import com.example.test.Adapter.MyViewPagerAdapter;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class BookingDonationActivity extends AppCompatActivity {

    private StepView stepView;
    private ViewPager view_pager;
    public Button btn_previous_step,btn_next_step;


    List<String> idList;
    List<Hospital> userList;
    HospitalAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_for2_users);

        stepView = findViewById(R.id.step_view);
        view_pager =findViewById(R.id.view_pager);
        btn_previous_step =findViewById(R.id.btn_previous_step);
        btn_next_step = findViewById(R.id.btn_next_step);


        setupStepView();
        setColorButton();

        //View
        view_pager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    btn_previous_step.setEnabled(false);
                }else{
                    btn_previous_step.setEnabled(true);
                }
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled()){
            btn_next_step.setBackgroundResource(R.color.ColorPrimary);
        }else{
            btn_next_step.setBackgroundResource(R.color.white);
        }

        if(btn_previous_step.isEnabled()){
            btn_previous_step.setBackgroundResource(R.color.ColorPrimary);
        }else{
            btn_previous_step.setBackgroundResource(R.color.white);
        }

    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Location");
        stepList.add("Hospital");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
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