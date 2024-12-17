package com.example.project_1_java.Store.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.project_1_java.Store.BoothClassify.AvailableFragment;
import com.example.project_1_java.Store.BoothClassify.BreachFragment;
import com.example.project_1_java.Store.BoothClassify.PendingFragment;

public class BoothAdapter extends FragmentStateAdapter {
    public BoothAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AvailableFragment();
            case 1:
                return new PendingFragment();
            default:
                return new BreachFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
