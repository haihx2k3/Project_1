package com.example.project_1_java.Footer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Footer.InterFace.ProfileContract;
import com.example.project_1_java.Footer.Presenter.ProfilePresenter;
import com.example.project_1_java.LoadingEffectActivity;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.SellViewModel;
import com.example.project_1_java.Utils.PermissionManager;
import com.example.project_1_java.databinding.ActivityProfileBinding;

public class ProfileActivity extends LoadingEffectActivity implements ProfileContract.View {
    private ActivityProfileBinding binding;
    private ProfilePresenter presenter;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private ActivityResultLauncher<String> permissionLauncher;
    private PermissionManager permissionManager;
    private String uid,userName;
    private Uri img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SellViewModel viewModel = new ViewModelProvider(this).get(SellViewModel.class);
        presenter = new ProfilePresenter(this,viewModel,this);
        getData();
        setupProfile();
        setupListener();
        setupRsLauncher();
        permissionManager = new PermissionManager(this,permissionLauncher,pickImageLauncher);
    }

    private void setupRsLauncher() {
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result->{
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri selectedImageUri = result.getData().getData();
                if (selectedImageUri != null) {
                    img = selectedImageUri;
                    Glide.with(this).load(img).into(binding.imgProfile);
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                presenter.onPermissionGranted();
            } else {
                presenter.onPermissionDenied();
            }
        });
    }

    private void setupListener() {
        binding.edtBirthDay.setOnClickListener(v -> presenter.onDatePicker());
        binding.edtGender.setOnClickListener(v -> presenter.onGender());
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgProfile.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermission();
            } else {
                presenter.onOpenImage();
            }
        });
        binding.imgCheckEdit.setOnClickListener(v->{
            String name = binding.edtNameEdit.getText().toString().trim();
            String gender = binding.edtGender.getText().toString();
            String birthDay = binding.edtBirthDay.getText().toString();
            presenter.saveProfile(name, gender, birthDay, img);
        });
    }

    private void setupProfile() {
        Glide.with(this).load(img).placeholder(R.drawable.profile).into(binding.imgProfile);
        presenter.loadProfile(uid);
    }
    private void getData() {
        userName = getIntent().getStringExtra("name");
        setName(userName);
        uid = getIntent().getStringExtra("uid");
        String avatar = getIntent().getStringExtra("avt");
        if (avatar!=null){
            img = Uri.parse(avatar);
        }
    }

    @Override
    public void showLoading() {
        showLoading(1000,()->{});
    }

    @Override
    public void hideLoading() {
        hideLoadingEffect();
    }

    @Override
    public void setName(String name) {
        binding.edtNameEdit.setText(name);
    }

    @Override
    public void setGender(String gender) {
        binding.edtGender.setText(gender);
    }

    @Override
    public void setBirthDay(String birthDay) {
        binding.edtBirthDay.setText(birthDay);
    }

    @Override
    public void setAvatar(Uri avatarUri) {
        Glide.with(this).load(avatarUri).into(binding.imgProfile);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showGenderDialog() {

    }

    @Override
    public void openImagePicker() {
        permissionManager.openImagePicker();
    }

    @Override
    public void onSetting() {
        permissionManager.showPermissionRationaleDialog();
    }

    @Override
    public void requestPermission() {
        permissionManager.requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
    }
}