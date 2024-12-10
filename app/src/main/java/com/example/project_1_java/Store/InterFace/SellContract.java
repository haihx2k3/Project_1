package com.example.project_1_java.Store.InterFace;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ClassifyModel;

import java.util.ArrayList;
import java.util.List;

public interface SellContract {
    interface View {
        void showImages();
        void showLoading();
        void hideLoading();
        void showSuccessDialog();
        void clearInputs();
        void showError(String message);
        void showBranch(List<BranchModel> branchModels);
    }

    interface Presenter {
        void onAddImageClicked();
        void onUploadClicked(String title, String description, String price,String branch);
        void onClassifyResult(ArrayList<ClassifyModel> result);
        void onDataBranch();
    }
}
