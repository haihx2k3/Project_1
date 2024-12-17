package com.example.project_1_java.Store.Presenter;

import android.util.Log;

import com.example.project_1_java.Model.BoothModel;
import com.example.project_1_java.Store.InterFace.AvailableContract;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class AvailablePresenter implements AvailableContract.Presenter {
    private AvailableContract.View view;
    private List<BoothModel> boothModels;
    private FirebaseUtil firebaseUtil;
    public AvailablePresenter(AvailableContract.View view) {
        this.view = view;
        this.firebaseUtil = new FirebaseUtil();
        this.boothModels = new ArrayList<>();
    }

    @Override
    public void onGetData() {
        firebaseUtil.getItemToBooth(new FirebaseUtil.ItemCallback<List<BoothModel>>() {
            @Override
            public void onResult(List<BoothModel> result) {
                if (result.isEmpty()){
                    view.showError();
                    return;
                }
                boothModels.addAll(result);
                view.showProduct(boothModels);
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
                view.showError();
            }
        });
    }
}
