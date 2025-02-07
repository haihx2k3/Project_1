package com.example.project_1_java.Card.Interface;

public interface DataListenerOrder {
    void onDataReceived(Float price,String delivery);
    void onUpdateLocation(String name, String phone, String location, String locationPlus, int id);
}
