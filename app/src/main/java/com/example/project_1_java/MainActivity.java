package com.example.project_1_java;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.project_1_java.Footer.SecondHomeFragment;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.Header.HomeFragment;
import com.example.project_1_java.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final long count = 2000;
    private long backPressed = 0;
    private LoadFragment fragmentLoader;
    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentLoader = new LoadFragment();
        if (savedInstanceState==null){
            fragmentLoader.loadFragment(this,R.id.frHome,new HomeFragment());
        }
        setupListener();
        finishApp();
    }

    private void setupListener() {
        final Fragment sub = new HomeFragment();
        final Fragment sub2 = new SecondHomeFragment();
        BottomNavigationView bottom = findViewById(R.id.bottom_navigation);
        for (int i = 0; i < bottom.getMenu().size(); i++) {
            View view = bottom.findViewById(bottom.getMenu().getItem(i).getItemId());
            if (view != null) {
                view.setOnLongClickListener(v -> true);
            }
        }
        bottom.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) {
                fragmentLoader.loadFragment(this, R.id.frHome, sub);
                return true;
            } else if (item.getItemId() == R.id.menu_proFile) {
                fragmentLoader.loadFragment(this, R.id.frHome, sub2);
                return true;
            } else {
                return true;
            }

        });
    }
    private void finishApp() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressed + count > System.currentTimeMillis()) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Nhấn lại để thoát", Toast.LENGTH_SHORT).show();
                    backPressed = System.currentTimeMillis();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}