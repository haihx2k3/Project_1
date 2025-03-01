package com.example.project_1_java.Branch.Contract;

import com.example.project_1_java.Model.ModelProduct;

import java.util.List;

public interface BranchContract {
    interface View{
        void showBranch(List<ModelProduct> model);
    }
    interface Presenter{
        void getBranch(String branch);
        void onDecreasing();
        void onAscending();
    }
}
