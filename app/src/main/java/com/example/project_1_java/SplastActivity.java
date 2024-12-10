package com.example.project_1_java;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_1_java.databinding.ActivitySplastBinding;

public class SplastActivity extends AppCompatActivity {
    private ActivitySplastBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main = new Intent(SplastActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        },2000);
    }
}