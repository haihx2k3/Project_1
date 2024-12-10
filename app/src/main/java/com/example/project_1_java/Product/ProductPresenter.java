package com.example.project_1_java.Product;

import android.util.Log;

import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.List;

public class ProductPresenter implements ProductContract.Presenter {
    private ProductContract.View view;
    private FirebaseUtil firebaseUtil;
    String user,avt;

    public ProductPresenter(ProductContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewCreated(String id, String sellerId) {
        firebaseUtil = new FirebaseUtil();
        firebaseUtil.getItemProductId(id, new FirebaseUtil.ItemCallback<List<String>>() {
            @Override
            public void onResult(List<String> result) {
                view.showProductDetails(result);
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
                Log.d("Image Product Error", errorMessage);
            }
        });
        firebaseUtil.getInfoSeller(sellerId, (fetchUserName) -> {
            user = fetchUserName.first;
            avt = fetchUserName.second;
            view.showSellerDetails(avt, user);
        });
    }

    @Override
    public void onProductSub() {
        view.showProductsSub();
    }

    @Override
    public void onUserName() {
        view.navigateToChat(user,avt);
    }
}
