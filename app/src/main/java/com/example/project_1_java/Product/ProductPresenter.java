package com.example.project_1_java.Product;

import android.util.Log;

import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductPresenter implements ProductContract.Presenter {
    private ProductContract.View view;
    private FirebaseUtil firebaseUtil;
    private final ExecutorService executor;
    String user,avt;

    public ProductPresenter(ProductContract.View view) {
        this.view = view;
        this.firebaseUtil = new FirebaseUtil();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onViewCreated(String id, String sellerId) {
        executor.execute(()->{
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
        });
    }

    @Override
    public void onProductSub() {
        view.showProductsSub(user,avt);
    }

    @Override
    public void onUserName() {
        view.navigateToChat(user,avt);
    }
}
