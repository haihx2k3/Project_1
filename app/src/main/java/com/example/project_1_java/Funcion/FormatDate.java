package com.example.project_1_java.Funcion;

import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatDate {
    @SuppressLint("SimpleDateFormat")
    public static String timeStampToString(Object timestamp){
        if (timestamp instanceof Timestamp) {
            return new SimpleDateFormat("HH:mm").format(((Timestamp) timestamp).toDate());
        } else {
            return "00:00";
        }
    }
    @SuppressLint("SimpleDateFormat")
    public static String timeStampToDate(Object timestamp){
        if (timestamp instanceof Timestamp) {
            return new SimpleDateFormat("EEE, dd MMMM yyyy 'lúc' HH:mm", new Locale("vi", "VN")).format(((Timestamp) timestamp).toDate());
        } else {
            return "Th 4, 13 Tháng 8 Năm 2003 lúc 00:00";
        }
    }
}
