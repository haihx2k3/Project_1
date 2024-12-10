package com.example.project_1_java.InterFace;

public interface DataOrderBill {
   void onDataReceived(Float price);
    void onCheckIdLocation(Integer id);
    void onUpdateLocation(String Name,String Phone,String Location,String LocationPlus,Integer id);
}
