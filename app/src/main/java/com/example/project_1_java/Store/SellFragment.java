package com.example.project_1_java.Store;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Funcion.FormatVND;
import com.example.project_1_java.Funcion.HideKeyboard;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.Adapter.BranchStoreAdapter;
import com.example.project_1_java.Store.Adapter.SellAdapter;
import com.example.project_1_java.Store.Classify.ClassifyFragment;
import com.example.project_1_java.Store.InterFace.ClassifyReceived;
import com.example.project_1_java.Store.InterFace.SellContract;
import com.example.project_1_java.Utils.PermissionManager;
import com.example.project_1_java.databinding.FragmentSellBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SellFragment extends Fragment implements SellContract.View, ClassifyReceived {
    private AlertDialog dialog;
    private SellContract.Presenter presenter;
    private SellAdapter adapter;
    private HideKeyboard hide;
    private String branch;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private PermissionManager permissionManager;
    private FragmentSellBinding binding;
    private LoadFragment loadFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSellBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hide = new HideKeyboard(requireActivity());
        presenter = new SellPresenter(this, getContext());
        loadFragment = new LoadFragment();
        setupView();
        setupRsLauncher();
        setupRecycleView();
        permissionManager = new PermissionManager(this,permissionLauncher,pickImageLauncher);
    }

    private void setupRecycleView() {
        adapter = new SellAdapter(new ArrayList<>());
        binding.rcSell.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false));
        binding.rcSell.setAdapter(adapter);
    }

    private void setupRsLauncher() {
        //Set avatar for UI
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result->{
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                if (selectedImageUri != null) {
                    List<Uri> uriList = new ArrayList<>();
                    uriList.add(selectedImageUri);
                    presenter.handleImagePicked(uriList);
                }
            }
        });
        //Check permission conditions
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                presenter.onPermissionGranted();
            } else {
                presenter.onPermissionDenied();
            }
        });
    }
    private void setupView() {
        //Format price
        binding.edtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().isEmpty()) return;
                binding.edtPrice.removeTextChangedListener(this);
                String originalString = s.toString();
                String formatString = FormatVND.formatCurrency(originalString);
                binding.edtPrice.setText(formatString);
                binding.edtPrice.setSelection(formatString.length());
                binding.edtPrice.addTextChangedListener(this);
            }
        });
        binding.btnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.edtTittle.getText().toString();
                String describe = binding.edtDescribe.getText().toString();
                String price = binding.edtPrice.getText().toString();
                presenter.onUploadClicked(title, describe, price,branch);
                hide.hide();
            }
        });

        binding.btnClassify.setOnClickListener(v -> {
            new HideKeyboard(requireActivity()).hide();
            presenter.openCategory();
        });

        binding.btnAddImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                presenter.onOpenImage();
            }
        });
        binding.imgBack.setOnClickListener(v-> getParentFragmentManager().popBackStack());
        presenter.onDataBranch();
    }
    //Update recycle view
    @Override
    public void showImages(List<Uri> uriList) {
        adapter.updateData(uriList);
    }
    //Effect
    @Override
    public void showLoading() {
        AlertDialog.Builder build = new AlertDialog.Builder(getContext(), R.style.Loading);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
        build.setView(view);
        dialog = build.create();
        dialog.setCancelable(false);
        dialog.show();
    }
    //hide effect
    @Override
    public void hideLoading() {
        dialog.dismiss();
    }
    //completion notice
    @Override
    public void showSuccessDialog() {
        AlertDialog.Builder build = new AlertDialog.Builder(getContext(), R.style.ThemeCustomDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bg_addsell, null);
        build.setView(view);
        dialog = build.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 2000);

    }

    @Override
    public void clearInputs() {
        binding.edtTittle.setText("");
        binding.edtDescribe.setText("");
        binding.edtPrice.setText("");
        binding.edtTittle.requestFocus();
        adapter.clearData();

    }

    @Override
    public void showError(String message) {
        Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();

    }
    //Processing branch
    @Override
    public void showBranch(List<BranchModel> branchModels) {
        binding.spnBranch.setAdapter(new BranchStoreAdapter(requireActivity(),branchModels));
        binding.spnBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BranchModel selectedBranch = branchModels.get(i);
                branch = selectedBranch.getTitle();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void openImagePicker() {
        permissionManager.openImagePicker();
    }
    //open settings on android
    @Override
    public void onSetting() {
        permissionManager.showPermissionRationaleDialog();
    }

    @Override
    public void requestPermission() {
        permissionManager.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void showFragment(ClassifyFragment fragment) {
        binding.frSell.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this,R.id.frSell,fragment);
    }

    @Override
    public void onDataSent(ArrayList<ClassifyModel> data) {
        presenter.onClassifyResult(data);
    }
}