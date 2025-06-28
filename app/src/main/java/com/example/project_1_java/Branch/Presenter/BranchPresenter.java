package com.example.project_1_java.Branch.Presenter;

import android.app.Activity;

import com.example.project_1_java.Branch.Contract.BranchContract;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Funcion.Sort;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BranchPresenter implements BranchContract.Presenter {
    private final BranchContract.View view;
    private final ExecutorService executors;
    private final FirebaseUtil firebaseUtil;
    private List<ModelProduct> mList;
    private Sort sortBranch;


    public BranchPresenter(BranchContract.View view) {
        this.view = view;
        this.executors = Executors.newSingleThreadExecutor();
        this.firebaseUtil = new FirebaseUtil();
        this.mList = new ArrayList<>();
        this.sortBranch = new Sort();
    }

    @Override
    public void getBranch(String branch) {
      firebaseUtil.getBranchProduct(branch,result -> {
          mList = result;
          view.showBranch(result);
      },errorMessage -> {});
    }

    @Override
    public void onDecreasing() {
        executors.execute(()->{
            sortBranch.mergeSort(mList, 0, mList.size() - 1,false);
            ((Activity) view).runOnUiThread(() -> view.showBranch(mList));
        });
    }

    @Override
    public void onAscending() {
        executors.execute(()->{
            sortBranch.mergeSort(mList, 0, mList.size() - 1,true);
            ((Activity) view).runOnUiThread(() -> view.showBranch(mList));
        });
    }

    @Override
    public void onProductClicked(int pos, ModelProduct product) {
        if (view != null) {
            view.showProductActivity(product.getSellerId(), product.getId(), product.getTitle(), product.getPrice(), product.getDetail());
        }
    }
}
