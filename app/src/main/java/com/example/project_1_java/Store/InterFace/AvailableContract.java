package com.example.project_1_java.Store.InterFace;

import androidx.fragment.app.Fragment;

import com.example.project_1_java.Model.BoothModel;
import com.example.project_1_java.Model.ClassifyModel;

import java.util.ArrayList;
import java.util.List;

public interface AvailableContract {
    interface View {
        void showProduct(List<BoothModel> available);
        void showError();
    }

    interface Presenter {
        void onGetData();
    }
}
