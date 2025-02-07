package com.example.project_1_java.Store.Classify;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.InterFace.OnClickClassify;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Model.OrderModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.Adapter.ClassifyAdapter;
import com.example.project_1_java.Store.InterFace.ClassifyContract;
import com.example.project_1_java.Store.InterFace.ClassifyData;
import com.example.project_1_java.Store.InterFace.ClassifyReceived;
import com.example.project_1_java.Utils.PermissionManager;
import com.example.project_1_java.databinding.FragmentClassifyBinding;

import java.util.ArrayList;
import java.util.List;

public class ClassifyFragment extends Fragment implements ClassifyData, ClassifyContract.View {
    private FragmentClassifyBinding binding;
    private LoadFragment loadFragment;

    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private PermissionManager permissionManager;
    private ClassifyReceived dataSending;
    private ClassifyAdapter adapter;
    private ClassifyPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClassifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFragment = new LoadFragment();
        presenter = new ClassifyPresenter(this);
        setupListener();
        setupRecycleView();
        setupRsLauncher();
        permissionManager = new PermissionManager(this, permissionLauncher, pickImageLauncher);
        getReceive();
    }

    private void setupListener() {
        binding.btnAddRcClassify.setOnClickListener(view -> {
            binding.scClassify.setVisibility(View.VISIBLE);
            presenter.onAddRcClassifyClicked();
        });
        binding.imgBack.setOnClickListener(view -> getParentFragmentManager().popBackStack());
        binding.btnSave.setOnClickListener(view -> presenter.onSaveClicked());
    }
    private void setupRecycleView() {
        adapter = new ClassifyAdapter(new ArrayList<>(), new OnClickClassify() {
            @Override
            public void onSelectImage(int pos) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                } else {
                    presenter.onImageClicked(pos);
                }
            }

            @Override
            public void onUpdate(int pos) {
                presenter.onEditClicked(pos);
            }

            @Override
            public void onDelete(int pos) {
                presenter.onDeleteClicked(pos);
            }
        });
        binding.rcClassify.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
        binding.rcClassify.setAdapter(adapter);

    }
    private void getReceive() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.scClassify.setVisibility(View.VISIBLE);
            ArrayList<ClassifyModel> result = getArguments().getParcelableArrayList("Category");
            presenter.handleDataTemporary(result);
        }
    }
    private void setupRsLauncher() {
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            presenter.handleImagePicked(selectedImageUri.toString());
                        }
                    }
                }
        );
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                presenter.onPermissionGranted();
            } else {
                presenter.onPermissionDenied();
            }
        });
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof ClassifyReceived) {
            dataSending = (ClassifyReceived) getParentFragment();
        }
    }

    @Override
    public void showVariants(List<ClassifyModel> variants) {
        if (variants != null && !variants.isEmpty()) {
            adapter.updateData(variants);
        }
    }

    @Override
    public void onSetting() {
        permissionManager.showPermissionRationaleDialog();
    }


    @Override
    public void openImagePicker() {
        permissionManager.openImagePicker();
    }

    @Override
    public void closeActivityWithResult(ArrayList<ClassifyModel> data) {
        if (dataSending != null) {
            dataSending.onDataSent(data);
            getParentFragmentManager().popBackStack();
        }
    }

    @Override
    public void disableNameField() {
        binding.edtAddName.setEnabled(false);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadFragment(Fragment fragment) {
        binding.frClassify.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this, R.id.frClassify, fragment);
    }

    @Override
    public void requestPermission() {
        permissionManager.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onDataReceived(String title, String price) {
        binding.frClassify.setVisibility(View.GONE);
        presenter.handleDataReceived(title, price);
    }

    @Override
    public void onDataUpdated(String title, String price) {
        binding.frClassify.setVisibility(View.GONE);
        presenter.handleDataUpdated(title, price);
    }
}
