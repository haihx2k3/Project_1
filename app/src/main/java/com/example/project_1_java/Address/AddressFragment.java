package com.example.project_1_java.Address;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_1_java.Address.Adapter.AddressAdapter;
import com.example.project_1_java.Address.Presenter.AddressContract;
import com.example.project_1_java.Address.Presenter.AddressPresenter;
import com.example.project_1_java.Card.Interface.DataListenerOrder;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.InterFace.OnClickLocation;
import com.example.project_1_java.InterFace.OnSelectAddress;
import com.example.project_1_java.Model.InfoModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Utils.copyDbHelper;
import com.example.project_1_java.databinding.FragmentAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class AddressFragment extends Fragment implements AddressContract.View, OnClickLocation {
    private FragmentAddressBinding binding;
    private AddressPresenter presenter;
    private AddressAdapter adapter;
    private LoadFragment loadFragment;
    private copyDbHelper db;
    private DataListenerOrder onData;
    private Integer id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = new copyDbHelper(requireContext());
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFragment = new LoadFragment();
        SQLiteDatabase database = db.openDatabase();
        presenter = new AddressPresenter(this, database);
        id = getArguments().getInt("id",0);
        setupListener();
        setupRecycleView();
        presenter.loadAddresses();
    }

    private void setupRecycleView() {
        adapter = new AddressAdapter(new ArrayList<>(), new OnSelectAddress() {
            @Override
            public void onDelete(int pos) {
                presenter.deleteAddress(pos);
            }

            @Override
            public void onRadioCheck(int pos) {
                presenter.onRadioCheck(pos);
            }
        },id);
        binding.rcAddress.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        binding.rcAddress.setAdapter(adapter);
    }

    private void setupListener() {
        binding.imgBack.setOnClickListener(v -> requireParentFragment().getChildFragmentManager().popBackStack());
        binding.btnAddNew.setOnClickListener(v -> {
            binding.frAddress.setVisibility(View.VISIBLE);
            navigateToLocationFragment();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof DataListenerOrder){
            onData = (DataListenerOrder) getParentFragment();
        }
    }

    @Override
    public void showAddresses(List<InfoModel> addresses) {
        adapter.updateData(addresses);
    }

    @Override
    public void onDeleteAddressSuccess() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void navigateToLocationFragment() {
        final Fragment sub = new Location();
        binding.frAddress.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this, R.id.frAddress, sub);
    }

    @Override
    public void onSelectLocation(String name, String phone, String location, String locationPLus, Integer id) {
        onData.onUpdateLocation(name,phone,location,locationPLus,id);
        requireParentFragment().getChildFragmentManager().popBackStack();
    }

    @Override
    public void onClick() {
        presenter.loadAddresses();
    }
}