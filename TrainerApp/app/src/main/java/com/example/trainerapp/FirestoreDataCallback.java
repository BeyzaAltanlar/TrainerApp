package com.example.trainerapp;

import java.util.List;
import java.util.Map;

public interface FirestoreDataCallback {
    void onDataReceived(Map<String, Object> dataList);
    void onError(Exception e);
}


