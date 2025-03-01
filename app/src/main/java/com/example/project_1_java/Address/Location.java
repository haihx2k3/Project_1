package com.example.project_1_java.Address;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.project_1_java.Address.Presenter.LocationContract;
import com.example.project_1_java.Address.Presenter.LocationPresenter;
import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.InterFace.OnClickLocation;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.FragmentLocationBinding;

public class Location extends Fragment implements LocationContract.View {
    private LocationPresenter presenter;
    private AlertDialog dialog;
    private OnClickLocation onClick;
    private ActivityResultLauncher<String> permissionLauncher;
    private FragmentLocationBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       binding = FragmentLocationBinding.inflate(inflater,container,false);
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new LocationPresenter(this,requireContext());
        setupListener();
        checkFormValidity();
        onPermission();
    }

    private void setupListener() {
        binding.imgBack.setOnClickListener(v->{
           requireParentFragment().getChildFragmentManager().popBackStack();
        });
        binding.edtLocation.setOnClickListener(v->presenter.onLocationClick());
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            //check empty view
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFormValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.fullName.addTextChangedListener(textWatcher);
        binding.phoneNumber.addTextChangedListener(textWatcher);
        binding.edtLocation.addTextChangedListener(textWatcher);
        binding.edtHouseNumber.addTextChangedListener(textWatcher);
        binding.btnAdd.setOnClickListener(v->{
            presenter.onAddRecord(
                    binding.fullName.getText().toString(),
                    binding.phoneNumber.getText().toString(),
                    binding.edtLocation.getText().toString(),
                    binding.edtHouseNumber.getText().toString()
            );
        });
    }
    private void checkFormValidity() {
        presenter.checkFormValidity(
                binding.fullName.getText().toString(),
                binding.phoneNumber.getText().toString(),
                binding.edtLocation.getText().toString(),
                binding.edtHouseNumber.getText().toString()
        );
    }
    //processing permission
    private void onPermission() {
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                presenter.onPermissionGranted();
            } else {
                presenter.onSetting();
            }
        });
    }
    // Check if the parent fragment implements OnClickLocation interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnClickLocation){
            onClick = (OnClickLocation) getParentFragment();
        }
    }

    @Override
    public void showLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(),R.style.Loading);
        builder.setView(R.layout.dialog_loading);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    //show location
    @Override
    public void displayLocation(String address) {
        binding.edtLocation.setText(address);
    }

    @Override
    public void showPermissionDeniedMessage() {
        Toast.makeText(requireContext(), "Quyền bị từ chối", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    //set color button
    @Override
    public void enableAddButton(boolean enabled) {
        binding.btnAdd.setEnabled(enabled);
        if (enabled) {
            binding.btnAdd.setBackgroundResource(R.drawable.bk_btnlogin);
            binding.btnAdd.setTextColor(Color.parseColor("#F16337"));
        }else {
            binding.btnAdd.setBackgroundResource(R.drawable.bk_btnlogin);
            binding.btnAdd.setTextColor(Color.parseColor("#CAC9C9"));
        }
    }

    @Override
    public void navigateBack() {
        if (onClick!=null){
            onClick.onClick();
        }
        requireParentFragment().getChildFragmentManager().popBackStack();
    }

    @Override
    public void checkPermission() {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.stopLocationUpdates();
    }
}