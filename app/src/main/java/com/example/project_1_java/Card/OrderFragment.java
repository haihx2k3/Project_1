package com.example.project_1_java.Card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project_1_java.Card.Adapter.OrderAdapter;
import com.example.project_1_java.Card.Interface.DataListenerOrder;
import com.example.project_1_java.Card.Presenter.OrderContract;
import com.example.project_1_java.Card.Presenter.OrderPresenter;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.MainActivity;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.FragmentOrderBinding;

import java.util.ArrayList;

public class OrderFragment extends Fragment implements OrderContract.View, DataListenerOrder {
    private FragmentOrderBinding binding;
    private ArrayList<OrderClassifyModel> mOrder;
    private OrderPresenter presenter;
    private OrderAdapter adapter;
    private LoadFragment loadFragment;
    private int position = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new OrderPresenter(this, requireActivity());
        loadFragment = new LoadFragment();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<OrderModel> result = getArguments().getParcelableArrayList("orderList");
            presenter.loadOrderList(result);
        }
        setUpRecyclerView();
        setUpListeners();
        presenter.loadData();
    }

    private void setUpListeners() {
        binding.imgBack.setOnClickListener(v -> getParentFragmentManager().beginTransaction().remove(OrderFragment.this).commit());

        binding.lnAddress.setOnClickListener(v -> presenter.onAddressClick());

        binding.btnBuy.setOnClickListener(v -> presenter.handleBuyClick());

    }

    private void setUpRecyclerView() {
        binding.rvOder.setLayoutManager(new GridLayoutManager(getContext(), 1));
        binding.rvOder.setHasFixedSize(true);
        adapter = new OrderAdapter(mOrder, pos -> {
            position = pos;
            Bundle bundle = new Bundle();
            bundle.putFloat("delivery", mOrder.get(pos).getpriceDelivery());
            DeliveryFragment fragment = new DeliveryFragment();
            fragment.setArguments(bundle);
            loadFragment(fragment);
        });
        binding.rvOder.setAdapter(adapter);
    }

    @Override
    public void onDataReceived(Float price,String delivery) {
        if (position != -1) {
            mOrder.get(position).setpriceDelivery(price);
            mOrder.get(position).setDelivery(delivery);
            presenter.updateTotal();
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onUpdateLocation(String name, String phone, String location, String locationPlus, int id) {
        binding.txtName.setText(name + " | " + phone);
        binding.txtLocation.setText(locationPlus + "," + location);
        presenter.saveData(name, phone, location, locationPlus, id);
    }

    @Override
    public void showTotal(String total) {
        binding.txtBill.setText(total);
    }

    @Override
    public void showUserInfo(String name, String location) {
        binding.txtName.setText(name);
        binding.txtLocation.setText(location);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMain() {
        Context context = getContext();
        if (context != null) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Log.e("OrderFragment", "Error navigateToMain");
        }
    }

    @Override
    public void loadFragment(Fragment fragment) {
        binding.frOrder.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this, R.id.frOrder, fragment);
    }

    @Override
    public void updateOrderList(ArrayList<OrderClassifyModel> orderClassifyModels) {
        mOrder = orderClassifyModels;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOrder.clear();
    }
}