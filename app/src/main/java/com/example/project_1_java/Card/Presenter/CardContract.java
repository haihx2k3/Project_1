package com.example.project_1_java.Card.Presenter;

import com.example.project_1_java.Model.OrderModel;

import java.util.List;

public interface CardContract {
    interface View {
        void showLoading();
        void showError();

        void hideLoading();

        void displayOrderList(List<OrderModel> orders);

        void updateUI(Float total, int totalProduct);
        void navigateToOrderBill(List<OrderModel> order);

        void displayEmptyOrderView();

        void displayOrderView();
        void userEmpty();
    }

    interface Presenter {
        void fetchOrders();

        void onCheckOrder(int pos);

        void onUncheckOrder(int pos);

        void onPlus(int pos);

        void onMinus(int pos);

        void onBuyButtonClick();
        void onCheckedCount (int pos);
    }

}
