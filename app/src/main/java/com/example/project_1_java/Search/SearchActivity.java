package com.example.project_1_java.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Product.ProductAdapter;
import com.example.project_1_java.R;
import com.example.project_1_java.Search.Contract.SearchContract;
import com.example.project_1_java.Search.Presenter.SearchPresenter;
import com.example.project_1_java.databinding.ActivitySearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {
    private ActivitySearchBinding binding;
    private SearchContract.Presenter presenter;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new SearchPresenter(this);
        setupListener();
        setupRecycleView();
    }

    private void setupRecycleView() {
        binding.ln2.setVisibility(View.VISIBLE);
        binding.rvSearch.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        if (binding.rvSearch.getAdapter() == null) {
            binding.rvSearch.setHasFixedSize(true);
            adapter = new ProductAdapter(new ArrayList<>(), new OnClick() {
                @Override
                public void onClick(int pos) {

                }
            });
            binding.rvSearch.setAdapter(adapter);
        }
    }

    private void setupListener() {
        binding.imgSearch.setOnClickListener(v -> {
            String query = binding.edtSearch.getText().toString().trim();
            binding.edtSearch.clearFocus();
            presenter.onSearchClick(query);
        });
    }

    @Override
    public void onSearched(List<ModelProduct> modelProducts) {
        if (modelProducts != null && !modelProducts.isEmpty()) {
            adapter.updateData(modelProducts);
        }
       }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}