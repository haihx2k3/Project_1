package com.example.project_1_java.Store.Presenter;

import android.util.Log;

import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.Store.InterFace.PendingContract;
import com.example.project_1_java.Store.InterFace.SendingContract;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class SendingPresenter implements SendingContract.Presenter {
    private final SendingContract.View view;
    private final List<OrderClassifyModel> model;
    private final FirebaseUtil firebaseUtil;

    public SendingPresenter(SendingContract.View view) {
        this.view = view;
        model = new ArrayList<>();
        firebaseUtil = new FirebaseUtil();
    }

    @Override
    public void onGetData() {
        firebaseUtil.getItemSending(new FirebaseUtil.ItemCallback<List<OrderClassifyModel>>() {
            @Override
            public void onResult(List<OrderClassifyModel> result) {
                if (result.isEmpty()){
                    view.showError();
                    return;
                }
                model.addAll(result);
                view.showOrder(model);
                view.hideShimmer();
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
                Log.d("Error Sending Presenter",errorMessage);
                view.showError();
            }
        });
    }
}
