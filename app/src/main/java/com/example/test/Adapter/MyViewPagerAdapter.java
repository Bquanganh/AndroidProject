package com.example.test.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.test.Fragments.BookingStep1Fragment;
import com.example.test.Fragments.BookingStep2Fragment;
import com.example.test.Fragments.BookingStep3Fragment;
import com.example.test.Fragments.BookingStep4Fragment;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return BookingStep2Fragment.getInstance();
            case 1:
                return BookingStep3Fragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
