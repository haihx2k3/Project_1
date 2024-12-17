package com.example.project_1_java.Header;

import android.content.Context;
import android.content.res.TypedArray;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ImageModel;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.R;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter{
    private final HomeContract.View view;
    private final Context context;
    private List<BranchModel> branchModels;
    private HomeViewModel viewModel;
    public HomePresenter(HomeContract.View view,Context context) {
        this.view = view;
        this.context = context;
        this.viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(HomeViewModel.class);
        this.branchModels = new ArrayList<>();
        viewModel.mListImgVp2.observe(this.view.getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> imageModels) {
                view.displayImagesPager(imageModels);
            }
        });
        viewModel.mListProduct.observe(this.view.getViewLifecycleOwner(), new Observer<List<ModelProduct>>() {
            @Override
            public void onChanged(List<ModelProduct> products) {
                view.displayProducts(products);
            }
        });
    }
    @Override
    public void onSearchButtonClicked() {
        if (view != null) {
            view.showSearchActivity();
        }
    }

    @Override
    public void onChatClicked() {
        if (view != null) {
            view.showChatActivity();
        }
    }

    @Override
    public void onCardClicked() {
        boolean loginSuccess = context.getSharedPreferences("saveLogin", Context.MODE_PRIVATE).getBoolean("hide", false);
        if (view != null) {
            view.showCardActivity(loginSuccess);
        }
    }

    @Override
    public void onProductClicked(int pos, ModelProduct product) {
        if (view != null) {
            view.showProductActivity(product.getSellerId(), product.getId(), product.getTitle(), product.getPrice());
        }
    }

    @Override
    public void onDataBranch() {
        String[] branchNames = context.getResources().getStringArray(R.array.branch_names);
        TypedArray branchImages = context.getResources().obtainTypedArray(R.array.branch_images);

        for (int i = 0; i < branchNames.length; i++){
            int imageResId = branchImages.getResourceId(i, R.drawable.phone);
            branchModels.add(new BranchModel(branchNames[i],imageResId));
        }
        branchImages.recycle();
        view.displayBranch(branchModels);
    }

    @Override
    public void onDataLoad() {
        viewModel.loadMoreProducts();
    }
}
