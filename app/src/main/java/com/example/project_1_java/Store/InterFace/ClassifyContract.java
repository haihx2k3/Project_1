package com.example.project_1_java.Store.InterFace;

import androidx.fragment.app.Fragment;

import com.example.project_1_java.Model.ClassifyModel;

import java.util.ArrayList;
import java.util.List;

public interface ClassifyContract {
    interface View {
        void showVariants(List<ClassifyModel> variants);
        void onSetting();
        void openImagePicker();
        void closeActivityWithResult(ArrayList<ClassifyModel> data);
        void disableNameField();
        void showError(String message);
        void loadFragment(Fragment fragment);
        void requestPermission();
    }

    interface Presenter {
        void onAddClassifyClicked();
        void onAddRcClassifyClicked();
        void onBackClicked();
        void onSaveClicked();
        void onImageClicked(int position);
        void onEditClicked(int position);
        void onPermissionGranted();
        void onPermissionDenied();
        void handleImagePicked(String imageUri);
        void handleDataReceived(String title, String price);
        void handleDataUpdated(String title, String price);
    }
}
