package com.example.project_1_java.Search.Contract;

import com.example.project_1_java.Model.ModelProduct;

import java.util.List;

public interface SearchContract {
    interface View {
        void onSearched(List<ModelProduct> modelProducts);
    }
    interface Presenter{
        void onSearchClick(String query);
        void onDestroy();
    }
}
