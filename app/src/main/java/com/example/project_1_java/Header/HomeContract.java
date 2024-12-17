package com.example.project_1_java.Header;

import androidx.lifecycle.LifecycleOwner;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ImageModel;
import com.example.project_1_java.Model.ModelProduct;

import java.util.List;

public interface HomeContract {
    interface View {
        void displayImagesPager(List<String> images);
        void displayProducts(List<ModelProduct> products);
        void displayBranch(List<BranchModel> branch);
        void showSearchActivity();
        void showChatActivity();
        void showCardActivity(boolean checkLogin);
        void showProductActivity(String sellerId, String id, String title, String price);
        LifecycleOwner getViewLifecycleOwner();
        void showMoreProduct(List<ModelProduct> moreProduct);
    }

    interface Presenter {
        void onSearchButtonClicked();
        void onChatClicked();
        void onCardClicked();
        void onProductClicked(int pos, ModelProduct product);
        void onDataBranch();
        void onDataLoad();
    }
}
