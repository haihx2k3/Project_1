package com.example.project_1_java.Card;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_1_java.Card.Interface.DataListenerOrder;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.FragmentDeliveryBinding;
import com.example.project_1_java.databinding.FragmentOrderBinding;

import java.util.Calendar;

public class DeliveryFragment extends Fragment {
    private FragmentDeliveryBinding binding;
    private Float selectDelivery = 0F;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDeliveryBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Float delivery = getArguments() != null ? getArguments().getFloat("delivery", 18.300F) : null;
        if (delivery != null) {
            selectDelivery = delivery;
        }
        choice();
        selected();
        setupDateDelivery();
    }

    private void setupDateDelivery() {
        String fastDeliveryStart = dateAfterDays(2);
        String fastDeliveryEnd = dateAfterDays(3);
        String slowDeliveryStart = dateAfterDays(5);
        String slowDeliveryEnd = dateAfterDays(7);

        binding.txtDeliveryFast.setText("Đảm bảo nhận hàng từ " + fastDeliveryStart + " - " + fastDeliveryEnd);
        binding.txtDeliveryLow.setText("Đảm bảo nhận hàng từ " + slowDeliveryStart + " - " + slowDeliveryEnd);

        binding.txtVoucherFast.setText("Nhận Voucher trị giá đ15.000 nếu đơn hàng được giao đến bạn sau ngày " + fastDeliveryEnd + " " + Calendar.getInstance().get(Calendar.YEAR) + ".");
        binding.txtVoucherLow.setText("Nhận Voucher trị giá đ15.000 nếu đơn hàng được giao đến bạn sau ngày " + slowDeliveryEnd + " " + Calendar.getInstance().get(Calendar.YEAR) + ".");
    }

    private void selected() {
        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataListenerOrder activity = (DataListenerOrder) getParentFragment();
                if (activity != null) {
                    activity.onDataReceived(selectDelivery);
                }
                getParentFragmentManager().popBackStack();
            }
        });
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().remove(DeliveryFragment.this).commit();
            }
        });
    }

    private void choice() {
        updateUI();

        binding.shippingMethod1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDelivery = 18.300F;
                updateUI();
            }
        });
        binding.shippingMethod2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDelivery = 14.850F;
                updateUI();
            }
        });
    }
    private void updateUI() {
        if (selectDelivery == 18.300F) {
            binding.imgCheck.setVisibility(View.VISIBLE);
            binding.imgCheck2.setVisibility(View.GONE);
        } else if (selectDelivery == 14.850F) {
            binding.imgCheck2.setVisibility(View.VISIBLE);
            binding.imgCheck.setVisibility(View.GONE);
        }
    }
    private String dateAfterDays(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        return day + " Tháng " + month;
    }

}