package com.example.test.Interface;

import com.example.test.Model.TimeSlot;

import java.util.List;

public interface TimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
