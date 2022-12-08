package com.example.test.Interface;

import java.util.List;

public interface AllLocation {
    void  onAllLocationLoadSuccess(List<String> areaNameList);
    void  onAllLocationLoadFailed(String message);
}
