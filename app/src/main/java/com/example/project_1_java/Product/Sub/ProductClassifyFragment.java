package com.example.project_1_java.Product.Sub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Card.CardActivity;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Product.ProductClassifyAdapter;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.FragmentProductClassifyBinding;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class ProductClassifyFragment extends Fragment implements ProductClassifyConTract.View {
    private FragmentProductClassifyBinding binding;
    private String id, img, title, price, sellerId,sellerName,avt;
    private ProductClassifyPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProductClassifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new ProductClassifyPresenter(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            id = bundle.getString("id");
            title = bundle.getString("title");
            price = bundle.getString("price");
            sellerId = bundle.getString("sellerId");
            sellerName = bundle.getString("sellerName");
            avt = bundle.getString("avt");
            img = bundle.getString("img");
            presenter.loadProductDetails(id);
        }
        setupDefault();
        setupListener();
        setupRecycleView();
    }

    private void setupDefault() {
        binding.txtTittleShow.setText(title);
        binding.txtPriceShow.setText(price);
        Glide.with(requireContext()).load(img).into(binding.imageView2);
    }

    private void setupListener() {
        binding.btnAdd.setEnabled(false);
        binding.imgPlus.setOnClickListener(v -> presenter.onPlusClicked());
        binding.imgMinus.setOnClickListener(v -> presenter.onMinusClicked());
    }

    @Override
    public void showClassifyList(List<ClassifyModel> classifyList) {
        binding.rcClassifyShow.setAdapter(new ProductClassifyAdapter(classifyList, pos -> {
            ClassifyModel model = classifyList.get(pos);
            showProductDetails(model.getTitle(), model.getPrice(), model.getImg());
            enableAddButton();
        }));

    }

    @Override
    public void showProductDetails(String type, String price, String img) {
        binding.txtTittleShow.setText(type);
        binding.txtPriceShow.setText(price);
        Glide.with(requireContext()).load(img).into(binding.imageView2);
        binding.btnAdd.setOnClickListener(v -> {
            presenter.onAddToCartClicked(title,type, price,img, Integer.parseInt(binding.txtCount.getText().toString()),sellerId,sellerName,avt);
        });
    }

    @Override
    public void updateSum(int sum) {
        binding.txtCount.setText(String.valueOf(sum));
        binding.imgMinus.setEnabled(sum > 1);

    }

    @Override
    public void enableAddButton() {
        binding.btnAdd.setEnabled(true);
        binding.btnAdd.setBackgroundResource(R.drawable.bk_btnlogin);
        binding.btnAdd.setTextColor(Color.parseColor("#00CC00"));

    }

    @Override
    public void navigateToCardActivity() {
        startActivity(new Intent(requireContext(), CardActivity.class));
    }

    @Override
    public void showError(String errorMessage) {

    }
    private void setupRecycleView() {
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        binding.rcClassifyShow.setLayoutManager(layoutManager);
        binding.rcClassifyShow.setHasFixedSize(true);
    }

}