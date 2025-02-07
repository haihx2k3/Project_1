package com.example.project_1_java.Store.Presenter;

import android.util.Log;

import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.Store.InterFace.PendingContract;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class PendingPresenter implements PendingContract.Presenter {
    private final PendingContract.View view;
    private final List<OrderClassifyModel> model;
    private final FirebaseUtil firebaseUtil;
    public PendingPresenter(PendingContract.View view) {
        this.view = view;
        this.firebaseUtil = new FirebaseUtil();
        this.model = new ArrayList<>();
    }

    @Override
    public void onGetData() {
        firebaseUtil.getItemPending(new FirebaseUtil.ItemCallback<List<OrderClassifyModel>>() {
            @Override
            public void onResult(List<OrderClassifyModel> result) {
                if (result.isEmpty()){
                    view.showError();
                    return;
                }
                model.addAll(result);
                view.showProduct(model);
                view.hideShimmer();
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
                Log.d("Error Pending Presenter",errorMessage);
                view.showError();
            }
        });
    }
}
