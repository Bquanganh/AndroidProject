package com.example.test.Common;

import com.example.test.Model.AllHospitals;
import com.example.test.Model.Hospital;
import com.example.test.Model.TimeSlot;
import com.example.test.Model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_HOSPITAL = "HOSPITAL_SAVE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final int TIME_SLOT_TOTAL = 17;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static Hospital currentHospital;
    public static int step = 0;
    public static int currentTimeSlot=-1;
    public static Calendar currentDate = Calendar.getInstance();
    public static User currentUser;
    public static SimpleDateFormat simpleFormat = new SimpleDateFormat("dd_MM_yyyy");

    public static String convertTimeSLotToString(int slot) {
        switch (slot)
        {
            case  0:
                return "7:00-7:30";
            case 1:
                return "7:30-8:00";
            case 2:
                return "8:00-8:30";
            case 3:
                return "8:30-9:00";
            case  4:
                return "9:00-9:30";
            case 5:
                return "9:30-10:00";
            case  6:
                return "10:00-10:30";
            case 7:
                return "10:30-11:00";
            case  8:
                return "11:30-12:00";
            case 9:
                return "13:00-13:30";
            case 10:
                return "13:30-14:00";
            case 11:
                return "14:00-14:30";
            case  12:
                return "14:30-15:00";
            case 13:
                return "15:00-15:30";
            case 14:
                return "15:30-16:00";
            case  15:
                return "16:00-16:30";
            case 16:
                return "16:30-17:00";
            default:
                return "Closed";

        }
    }
}
