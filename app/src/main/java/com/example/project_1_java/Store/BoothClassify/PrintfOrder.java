package com.example.project_1_java.Store.BoothClassify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.project_1_java.databinding.FragmentPrintfOrderBinding;


public class PrintfOrder extends Fragment {
    private FragmentPrintfOrderBinding binding;
    private String idOrder,name,address,price,content;
    private int amount;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrintfOrderBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            idOrder = getArguments().getString("idOrder");
            name = getArguments().getString("info");
            address = getArguments().getString("address");
            price = getArguments().getString("price");
            content = getArguments().getString("content");
            amount = getArguments().getInt("amount");
        }
        setupView();
        setupListener();
    }

    private void setupListener() {
        binding.btnCancelPrintf.setOnClickListener(v->getParentFragmentManager().popBackStack());
    }

    private void setupView() {
        binding.txtIdPrintf.setText(idOrder);
        binding.txtInfoPrintf.setText(name);
        binding.txtAddressPrintf.setText(address);
        binding.txtPricePrintf.setText(String.format("%sđ", price));
        binding.edtContentPrintf.setText(String.format("- "+content+"(Số lượng: "+amount+")"));
    }
}