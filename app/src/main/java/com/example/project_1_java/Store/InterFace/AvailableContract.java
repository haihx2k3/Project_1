package com.example.project_1_java.Store.InterFace;

import androidx.fragment.app.Fragment;

import com.example.project_1_java.Model.AvailableModel;

import java.util.List;

public interface AvailableContract {
    interface View {
        void showProduct(List<AvailableModel> available);
        void showError();
        void hideShimmer();
        void showInfo(Fragment sub);
    }

    interface Presenter {
        void onGetData();
        void onUpdate(int pos,List<AvailableModel> models);
        void onDelete();
    }
}
