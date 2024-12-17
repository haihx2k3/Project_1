package com.example.project_1_java.Card.Presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.project_1_java.Address.AddressFragment;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderPresenter implements OrderContract.Presenter {
    private OrderContract.View view;
    private ArrayList<OrderClassifyModel> orderList;
    private SharedPreferences sharedPreferences;
    private Context context;
    private int idCheck = 0;

    public OrderPresenter(OrderContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
    }


    @Override
    public void loadOrderList(ArrayList<OrderModel> mOrder) {
        this.orderList = new ArrayList<>();
        for (OrderModel order : mOrder) {
            this.orderList.add(new OrderClassifyModel(order.getTitle(), order.getType(), order.getTotal(), order.getImg(), order.getCount(), order.getSellerId(), order.getId()));
            view.updateOrderList(orderList);
        }
        showTotal();
    }

    private void showTotal() {
       float total = 0F;
        for (OrderClassifyModel order : orderList) {
            total += FormatVND.convertStringToFloat(order.getTotal());
        }
        String formattedTotal = FormatVND.formatCurrency(String.format("%.3f", total));
        view.showTotal(formattedTotal);
    }

    @Override
    public void handleBuyClick() {
        if (idCheck < 0) {
            view.showToast("Địa chỉ nhận hàng không được để trống");
            return;
        }
        for (int i = 0; i < orderList.size(); i++) {
            FirebaseUtil util = new FirebaseUtil();
            util.addItemOder(
                    orderList.get(i).getSellerId(),  orderList.get(i).getIdOrder(),
                    orderList.get(i).getIdOrder(), orderList.get(i), new Runnable() {
                        @Override
                        public void run() {
                            view.navigateToMain();
                        }
                    }, new FirebaseUtil.FailureCallback() {
                        @Override
                        public void onFailure(String errorMessage) {
                            view.showToast("Fail");
                        }
                    });
        }
    }

    @Override
    public void loadData() {
        String name = sharedPreferences.getString("Name", "");
        String phone = sharedPreferences.getString("Phone", "");
        String location = sharedPreferences.getString("Location", "");
        String locationPlus = sharedPreferences.getString("LocationPlus", "");
        int id = sharedPreferences.getInt("id", -1);
        idCheck = id;

        if (!name.isEmpty() && !phone.isEmpty()) {
            view.showUserInfo(name + " | " + phone, locationPlus + ", " + location);
        }

        for (OrderClassifyModel order : orderList) {
            order.setInfo(name + " | " + phone);
            order.setAddress(locationPlus + ", " + location);
        }
    }
    @Override
    public void saveData(String name, String phone, String location, String locationPlus, int id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name", name);
        editor.putString("Phone", phone);
        editor.putString("Location", location);
        editor.putString("LocationPlus", locationPlus);
        editor.putInt("id", id);
        idCheck = id;
        editor.apply();
    }

    @Override
    public void onAddressClick() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", idCheck);
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(bundle);
        view.loadFragment(fragment);
    }

    @Override
    public void updateTotal() {
        showTotal();
    }
}
