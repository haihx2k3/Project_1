package com.example.project_1_java.Search.Presenter;

import android.app.Activity;
import android.util.Log;

import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Search.Contract.SearchContract;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View view;
    private FirebaseUtil firebaseUtil;
    private ExecutorService executor;

    public SearchPresenter(SearchContract.View view) {
        this.view = view;
        this.firebaseUtil = new FirebaseUtil();
        this.executor = Executors.newSingleThreadExecutor();
    }
    @Override
    public void onSearchClick(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }
        executor.execute(()->{
            firebaseUtil.searchProduct(query, new FirebaseUtil.ItemCallback<List<ModelProduct>>() {
                @Override
                public void onResult(List<ModelProduct> result) {
                    ((Activity) view).runOnUiThread(() -> view.onSearched(result));
                }
            }, new FirebaseUtil.FailureCallback() {
                @Override
                public void onFailure(String errorMessage) {
                    Log.e("Search Presenter",errorMessage);
                }
            });
        });
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
    }
}
