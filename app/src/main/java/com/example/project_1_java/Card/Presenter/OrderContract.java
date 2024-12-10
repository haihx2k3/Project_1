package com.example.project_1_java.Card.Presenter;

import androidx.fragment.app.Fragment;

import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.Model.OrderModel;

import java.util.ArrayList;

public interface OrderContract {
    interface View {
        void showTotal(String total);
        void showUserInfo(String name, String location);
        void showToast(String message);
        void navigateToMain();
        void loadFragment(Fragment fragment);
        void updateOrderList(ArrayList<OrderClassifyModel> orderClassifyModels);
    }

    interface Presenter {
        void loadOrderList(ArrayList<OrderModel> variantOrders);
        void handleBuyClick();
        void loadData();
        void saveData(String name, String phone, String location, String locationPlus, int id);
        void onAddressClick();
        void updateTotal();
    }
}
