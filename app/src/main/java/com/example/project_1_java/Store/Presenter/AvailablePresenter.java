package com.example.project_1_java.Store.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.example.project_1_java.Model.AvailableModel;
import com.example.project_1_java.Store.Branch.AvailableProduct;
import com.example.project_1_java.Store.InterFace.AvailableContract;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AvailablePresenter implements AvailableContract.Presenter {
    private final AvailableContract.View view;
    private final List<AvailableModel> avaiModels;
    private final FirebaseUtil firebaseUtil;
    private final ExecutorService executor;
    public AvailablePresenter(AvailableContract.View view) {
        this.view = view;
        this.firebaseUtil = new FirebaseUtil();
        this.avaiModels = new ArrayList<>();
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onGetData() {
        executor.execute(() -> {
            firebaseUtil.getItemAvailable(new FirebaseUtil.ItemCallback<List<AvailableModel>>() {
                @Override
                public void onResult(List<AvailableModel> result) {
                    if (result.isEmpty()){
                        view.showError();
                        return;
                    }
                    avaiModels.addAll(result);
                    view.showProduct(avaiModels);
                    view.hideShimmer();
                }
            }, new FirebaseUtil.FailureCallback() {
                @Override
                public void onFailure(String errorMessage) {
                    Log.d("Error Available Presenter",errorMessage);
                    view.showError();
                }
            });
        });
    }

    @Override
    public void onUpdate(int pos, List<AvailableModel> models) {
        Bundle bundle = new Bundle();
        AvailableModel model = models.get(pos);
        bundle.putString("id",model.getId());
        bundle.putString("title",model.getTitle());
        bundle.putString("image",model.getUriList().get(0));
        AvailableProduct sub = new AvailableProduct();
        sub.setArguments(bundle);
        view.showInfo(sub);
    }

    @Override
    public void onDelete() {

    }
}
