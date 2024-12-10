package com.example.project_1_java.Card.Presenter;

import android.util.Log;

import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.j256.ormlite.stmt.query.In;

import java.util.ArrayList;
import java.util.List;

public class CardPresenter implements CardContract.Presenter {
    private CardContract.View view;
    private List<OrderModel> orderList = new ArrayList<>();
    private List<OrderModel> order = new ArrayList<>();
    private FirebaseUtil util = new FirebaseUtil();
    private FormatVND fmt = new FormatVND();
    private Float totalAll = 0F;
    private int totalProduct = 0;

    public CardPresenter(CardContract.View view) {
        this.view = view;
    }


    @Override
    public void fetchOrders() {
        FirebaseAuth currentUser = FirebaseAuth.getInstance();
        if (currentUser.getCurrentUser() != null) {
            util.getItemToCard(new FirebaseUtil.ItemCallback<List<OrderModel>>() {
                @Override
                public void onResult(List<OrderModel> result) {
                    orderList.clear();
                    orderList.addAll(reverseList(result));
                    view.displayOrderList(orderList);
                    view.hideLoading();
                    checkOrderState();

                }
            }, errorMessage -> view.hideLoading());
        } else {
            view.userEmpty();
        }
    }

    @Override
    public void onCheckOrder(int pos) {
        if (pos >= 0 && pos < orderList.size()) {
            handleCheckChange(pos, true);
        }

    }

    @Override
    public void onUncheckOrder(int pos) {
        if (pos >= 0 && pos < orderList.size()) {
            handleCheckChange(pos, false);
        }
    }

    @Override
    public void onPlus(int pos) {
        updateTotal(pos, true);
    }

    @Override
    public void onMinus(int pos) {
        updateTotal(pos, false);
    }

    @Override
    public void onBuyButtonClick() {
        if (!order.isEmpty()) {
            view.navigateToOrderBill(new ArrayList<>(order));
        }
    }

    @Override
    public void onCheckedCount(int pos) {
        totalProduct = pos;
        checkOrderState();
        view.updateUI(totalAll, pos);
    }

    private void checkOrderState() {
        if (orderList.isEmpty()) {
            view.displayEmptyOrderView();
        } else {
            view.displayOrderView();
        }
    }

    private List<OrderModel> reverseList(List<OrderModel> list) {
        List<OrderModel> reversedList = new ArrayList<>(list);
        java.util.Collections.reverse(reversedList);
        return reversedList;
    }

    public void handleCheckChange(int pos, boolean isAdding) {
        if (pos != -1 && pos >= 0 && pos < orderList.size()) {
            Float value = orderList.get(pos).getTotal();
            if (value != null) {
                totalAll = isAdding ? totalAll + value : totalAll - value;
                totalProduct = isAdding ? totalProduct + 1 : totalProduct - 1;
            }
            if (isAdding) {
                order.add(orderList.get(pos));
            } else {
                order.remove(orderList.get(pos));
            }
        } else if (!isAdding) {
            totalAll = 0F;
            totalProduct = 0;
        }
        view.updateUI(totalAll, totalProduct);
    }

    private void updateTotal(int pos, boolean isAdding) {
        String price = orderList.get(pos).getPrice();
        Float value = fmt.convertStringToFloat(price);
        if (value != null) {
            totalAll = isAdding ? totalAll + value : totalAll - value;
        }
        view.updateUI(totalAll, totalProduct);
    }

}
