package com.example.project_1_java.Store.InterFace;

import com.example.project_1_java.Model.AvailableModel;
import com.example.project_1_java.Model.OrderClassifyModel;

import java.util.List;

public interface PendingContract {
    interface View {
        void showProduct(List<OrderClassifyModel> pending);
        void showError();
        void hideShimmer();
    }

    interface Presenter {
        void onGetData();
    }
}
