package com.example.project_1_java.Store;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_1_java.Store.Adapter.BoothAdapter;
import com.example.project_1_java.databinding.FragmentBoothBinding;
import com.google.android.material.tabs.TabLayout;

public class BoothFragment extends Fragment {
    private FragmentBoothBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBoothBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        colorTabBg();
        setupListener();
        setupRecycleView();
    }

    private void setupRecycleView() {
        BoothAdapter adapter = new BoothAdapter(getChildFragmentManager(), getLifecycle());
        binding.vpSellMain.setAdapter(adapter);
        binding.tabName.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab != null) {
                    binding.vpSellMain.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No-op
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No-op
            }
        });

        binding.vpSellMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabName.selectTab(binding.tabName.getTabAt(position));
            }
        });
    }

    private void colorTabBg() {
        TabLayout tabLayout = binding.tabName;
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setOnLongClickListener(v -> true);
            }
        }
    }

    private void setupListener() {
        binding.imgBack.setOnClickListener(v->getParentFragmentManager().popBackStack());
    }
}