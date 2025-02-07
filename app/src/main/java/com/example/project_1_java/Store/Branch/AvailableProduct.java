package com.example.project_1_java.Store.Branch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.project_1_java.InterFace.OnClickAvailable;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Store.Adapter.AvailablePrAdapter;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.example.project_1_java.databinding.FragmentAvailableProductBinding;

import java.util.ArrayList;
import java.util.List;

public class AvailableProduct extends Fragment {
    private FragmentAvailableProductBinding binding;
    private String id,title, image;

    private FirebaseUtil firebaseUtil;
    private AvailablePrAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAvailableProductBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            id =  getArguments().getString("id");
            title = getArguments().getString("title");
            image = getArguments().getString("image");
        }
        firebaseUtil = new FirebaseUtil();
        setupView();
        getData();
        setupRecycleView();
        setupListener();
    }

    private void setupListener() {
        binding.imgCancelAvailable.setOnClickListener(v->getParentFragmentManager().popBackStack());
    }

    private void setupView() {
        binding.txtTitleAvailable.setText(title);
        Glide.with(binding.imgAvailable).load(image).into(binding.imgAvailable);
    }

    private void getData() {
        firebaseUtil.getCategory(id, new FirebaseUtil.ItemCallback<List<ClassifyModel>>() {
            @Override
            public void onResult(List<ClassifyModel> result) {
                adapter.updateData(result);
                adapter.notifyDataSetChanged();
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
                Log.d("Error Available PR",errorMessage);
            }
        });
    }

    private void setupRecycleView() {
        binding.rcPrAvailable.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AvailablePrAdapter(new ArrayList<>(), new OnClickAvailable() {
            @Override
            public void onDelete(int pos) {

            }

            @Override
            public void onUpdate(int pos) {

            }
        });
        binding.rcPrAvailable.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL);
        binding.rcPrAvailable.addItemDecoration(dividerItemDecoration);
    }

}