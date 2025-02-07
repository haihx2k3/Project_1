package com.example.project_1_java.Footer.InterFace;

import android.net.Uri;

public interface ProfileContract {
    interface View {
        void showLoading();
        void hideLoading();
        void setName(String name);
        void setGender(String gender);
        void setBirthDay(String birthDay);
        void setAvatar(Uri avatarUri);
        void finishActivity();
        void openImagePicker();
        void onSetting();
        void requestPermission();
    }

    interface Presenter {
        void loadProfile(String uid);
        void saveProfile(String name, String gender, String birthDay, Uri img);
        void onOpenImage();
        void onPermissionGranted();
        void onPermissionDenied();
        void onDatePicker();
        void onGender();
    }
}
