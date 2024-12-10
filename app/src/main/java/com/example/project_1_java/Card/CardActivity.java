package com.example.project_1_java.Card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project_1_java.Card.Adapter.CardAdapter;
import com.example.project_1_java.Card.Presenter.CardContract;
import com.example.project_1_java.Card.Presenter.CardPresenter;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.InterFace.OnCheck;
import com.example.project_1_java.LoadingEffectActivity;
import com.example.project_1_java.Login.LoginActivity;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.ActivityCardBinding;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends LoadingEffectActivity implements CardContract.View {
    private CardPresenter presenter;
    private CardAdapter adapter;
    private FormatVND fmt;
    private LoadFragment loadFragment;
    private ActivityCardBinding binding;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new CardPresenter(this);
        fmt = new FormatVND();
        loadFragment = new LoadFragment();
        count = 0;
        setupRecycleView();
        setupListener();
        presenter.fetchOrders();
        showLoading();
    }

    private void setupListener() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.ckOder.setOnClickListener(v -> {
            if (binding.ckOder.isChecked()) {
                adapter.setAllCheckBoxes(true);
            } else {
                adapter.resetAllCheckBoxes();
                presenter.handleCheckChange(-1, false);
            }
        });
         binding.btnBuy.setOnClickListener(v -> presenter.onBuyButtonClick());

    }

    private void setupRecycleView() {
        binding.rvOder.setLayoutManager(new GridLayoutManager(this, 1));
        binding.rvOder.setHasFixedSize(true);
        adapter = new CardAdapter(new ArrayList<>(), new OnCheck() {
            @Override
            public void onCheck(int pos) {
                presenter.onCheckOrder(pos);

            }
            @Override
            public void onUnCheck(int pos) {
                presenter.onUncheckOrder(pos);
                binding.ckOder.setChecked(false);
            }
            @Override
            public void onCheckPlus(int pos) {
                presenter.onPlus(pos);
            }
            @Override
            public void onCheckMinus(int pos) {
                presenter.onMinus(pos);
            }
            @Override
            public void updateCheckedCount(int pos) {
                presenter.onCheckedCount(pos);
            }
        });
        binding.rvOder.setAdapter(adapter);

    }

    @Override
    public void showLoading() {
        showLoading(10000, () -> {
            displayEmptyOrderView();
            displayOrderView();
        });
    }

    @Override
    public void showError() {
        Log.d("Card Activity","Server time out");
    }

    @Override
    public void hideLoading() {
        hideLoadingEffect();
    }

    @Override
    public void displayOrderList(List<OrderModel> orders) {
        adapter.setOrderList(orders);
    }

    @Override
    public void updateUI(Float total, int totalProduct) {
        binding.txtBill.setText(fmt.formatCurrency(String.format("%.3f", total)));
        binding.btnBuy.setText("Mua hàng (" + totalProduct + ")");
        count = totalProduct;

    }

    @Override
    public void navigateToOrderBill(List<OrderModel> order) {
        binding.frCard.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        final Fragment sub = new OrderFragment();
        bundle.putParcelableArrayList("orderList", new ArrayList<>(order));
        sub.setArguments(bundle);
        loadFragment.loadFragment(this, R.id.frCard, sub);
    }

    @Override
    public void displayEmptyOrderView() {
        binding.lnWaitOder.setVisibility(View.VISIBLE);
        binding.bottomOrder.setVisibility(View.GONE);
    }

    @Override
    public void displayOrderView() {
        binding.lnWaitOder.setVisibility(View.GONE);
        binding.bottomOrder.setVisibility(View.VISIBLE);

    }

    @Override
    public void userEmpty() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}