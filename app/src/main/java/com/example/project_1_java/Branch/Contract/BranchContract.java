package com.example.project_1_java.Branch.Contract;

import com.example.project_1_java.Model.ModelProduct;

import java.util.List;

public interface BranchContract {
    interface View{
        void showBranch(List<ModelProduct> model);
        void showProductActivity(String sellerId, String id, String title, String price,String details);
    }
    interface Presenter{
        void getBranch(String branch);
        void onDecreasing();
        void onAscending();
        void onProductClicked(int pos, ModelProduct product);

    }
}
