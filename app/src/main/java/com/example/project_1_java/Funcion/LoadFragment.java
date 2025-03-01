package com.example.project_1_java.Funcion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class LoadFragment {
    public void loadFragment(AppCompatActivity activity, int containerId, Fragment fragment) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void loadFragment(Fragment fragment, int containerId, Fragment targetFragment) {
        FragmentTransaction transaction = fragment.getChildFragmentManager().beginTransaction();
        transaction.replace(containerId, targetFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
