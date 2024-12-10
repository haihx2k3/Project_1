package com.example.project_1_java.Footer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Footer.InterFace.SecondContract;
import com.example.project_1_java.Footer.Presenter.SecondPresenter;
import com.example.project_1_java.R;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.example.project_1_java.databinding.FragmentSecondHomeBinding;

public class SecondHomeFragment extends Fragment implements SecondContract.View {
    private FragmentSecondHomeBinding binding;
    private SecondPresenter presenter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSecondHomeBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new SecondPresenter(this, new FirebaseUtil(),getContext());
        setupListener();
    }

    private void setupListener() {
        binding.profileImage.setOnClickListener(v -> presenter.onProfileImageClicked());
        binding.btnLogin.setOnClickListener(v -> presenter.onLoginClicked());
        binding.ibtnSetiing.setOnClickListener(v -> presenter.onSettingsClicked());
        binding.btnSellPr.setOnClickListener(v -> presenter.onSellProductClicked());
        binding.btnHistory.setOnClickListener(v->getActivity().recreate());
    }

    @Override
    public void showLoggedInState() {
        binding.btnLogin.setVisibility(View.GONE);
        binding.btnSignUp.setVisibility(View.GONE);
        binding.frLoadingPf.setVisibility(View.VISIBLE);
        binding.btnSellPr.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoggedOutState() {
        binding.txtId.setVisibility(View.GONE);
        binding.btnLogin.setVisibility(View.VISIBLE);
        binding.btnSignUp.setVisibility(View.VISIBLE);
        binding.btnSellPr.setVisibility(View.GONE);
    }

    @Override
    public void displayUserInfo(String userName, String avatarProfile) {
       if (isAdded()){
           binding.txtId.setText(userName);
           Glide.with(requireContext()).load(avatarProfile).placeholder(R.drawable.profile).into(binding.profileImage);
           binding.frLoadingPf.setVisibility(View.GONE);
           binding.txtId.setVisibility(View.VISIBLE);

       }
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }
}