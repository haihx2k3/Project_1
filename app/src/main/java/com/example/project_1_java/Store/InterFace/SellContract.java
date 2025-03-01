package com.example.project_1_java.Store.InterFace;

import android.net.Uri;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Store.Classify.ClassifyFragment;

import java.util.ArrayList;
import java.util.List;

public interface SellContract {
    interface View {
        void showImages(List<Uri> uriList);
        void showLoading();
        void hideLoading();
        void showSuccessDialog();
        void clearInputs();
        void showError(String message);
        void showBranch(List<BranchModel> branchModels);
        void openImagePicker();
        void onSetting();
        void requestPermission();
        void showFragment(ClassifyFragment fragment);
    }

    interface Presenter {
        void onOpenImage();
        void onPermissionGranted();
        void onPermissionDenied();
        void onUploadClicked(String title, String description, String price, String branch);
        void onClassifyResult(ArrayList<ClassifyModel> result);
        void onDataBranch();
        void handleImagePicked(List<Uri> uriList);
        void openCategory();
    }
}
