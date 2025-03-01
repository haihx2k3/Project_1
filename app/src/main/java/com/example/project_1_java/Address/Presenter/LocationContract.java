package com.example.project_1_java.Address.Presenter;

import android.content.ContentValues;

public interface LocationContract {
    interface View {
        void showLoading();
        void hideLoading();
        void displayLocation(String address);
        void showPermissionDeniedMessage();
        void showToast(String message);
        void enableAddButton(boolean enabled);
        void navigateBack();
        void checkPermission();
    }

    interface Presenter {
        void onLocationClick();
        void onAddRecord(String name, String phone, String location, String locationPlus);
        void checkFormValidity(String name, String phone, String location, String locationPlus);
        void checkLocationPermission();
        void fetchAddress(double latitude, double longitude);
        void onSetting();
        void onPermissionGranted();
        void stopLocationUpdates();

    }
}
