package com.example.project_1_java.Product;

import java.util.List;

public interface ProductContract {
    public interface View {
        void showProductDetails(List<String> imageProduct);
        void showSellerDetails(String avatar, String sellerId);
        void showProductsSub();
        void navigateToChat(String userName,String avatar);
    }

    public interface Presenter {
        void onViewCreated(String id,String sellerId);
        void onProductSub();
        void onUserName();
    }

}
