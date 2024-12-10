package com.example.project_1_java.Footer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.project_1_java.R;
import com.example.project_1_java.databinding.ActivityEditBinding;
import com.google.firebase.auth.FirebaseAuth;

public class EditActivity extends AppCompatActivity {
    private ActivityEditBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListener();
        auth = FirebaseAuth.getInstance();
    }

    private void setupListener() {
        binding.btnLogOut.setOnClickListener(view -> {
            SharedPreferences sharedLogin = this.getSharedPreferences("saveLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedLogin.edit();
            editor.remove("hide");
            editor.apply();
            auth.signOut();
            finish();
        });
    }
}