package com.example.project_1_java.Product.Sub;

import com.example.project_1_java.Model.ClassifyModel;

import java.util.List;

public interface ProductClassifyConTract {
    interface View {
        void showClassifyList(List<ClassifyModel> classifyList);

        void showProductDetails(String title, String price, String img);

        void updateSum(int sum);

        void enableAddButton();

        void navigateToCardActivity();

        void showError(String errorMessage);
    }

    interface Presenter {
        void loadProductDetails(String id);

        void onAddToCartClicked(String title, String type, String price, String img, int count, String sellerId);

        void onPlusClicked();

        void onMinusClicked();
    }
}
