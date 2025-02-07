package com.example.project_1_java.Store.InterFace;

import com.example.project_1_java.Model.OrderClassifyModel;

import java.util.List;

public interface SendingContract {
    interface View {
        void showOrder(List<OrderClassifyModel> sending);
        void showError();
        void hideShimmer();
    }

    interface Presenter {
        void onGetData();
    }
}
