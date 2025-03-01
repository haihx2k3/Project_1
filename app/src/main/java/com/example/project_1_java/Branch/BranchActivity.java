package com.example.project_1_java.Branch;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project_1_java.Branch.Contract.BranchContract;
import com.example.project_1_java.Branch.Presenter.BranchPresenter;
import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Product.ProductAdapter;
import com.example.project_1_java.databinding.ActivityBranchBinding;

import java.util.List;

public class BranchActivity extends AppCompatActivity implements BranchContract.View {
    private ActivityBranchBinding binding;
    private String branch;
    private BranchContract.Presenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBranchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new BranchPresenter(this);
        branch = getIntent().getStringExtra("branch");
        presenter.getBranch(branch);
        setupListener();
    }
    private void setupListener() {
        binding.txtBranch.setText(branch);
        binding.imgBack.setOnClickListener(v->finish());
        binding.imgDecreasing.setOnClickListener(v->presenter.onDecreasing());
        binding.imgAscending.setOnClickListener(v -> presenter.onAscending());
    }

    @Override
    public void showBranch(List<ModelProduct> model) {
        if (model.isEmpty()){
            binding.btnEmptyBranch.setVisibility(View.VISIBLE);
            binding.btnEmptyBranch.setOnClickListener(v->finish());
        }else {
            binding.rvBranch.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
            if (binding.rvBranch.getAdapter() == null) {
                binding.rvBranch.setHasFixedSize(true);
                binding.rvBranch.setAdapter(new ProductAdapter(model, new OnClick() {
                    @Override
                    public void onClick(int pos) {

                    }
                }));
            }
        }
    }
}