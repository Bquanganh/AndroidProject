package com.example.test.Interface;

import com.example.test.Model.AllHospitals;

import java.util.List;

public interface HospitalsLoadListener {
    void  onHospitalsLoadSuccess(List<AllHospitals> hospitalsList);
    void  onHospitalsLoadFailed(String message);
}
