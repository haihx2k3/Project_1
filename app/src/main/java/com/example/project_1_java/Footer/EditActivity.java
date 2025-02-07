package com.example.project_1_java.Footer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_1_java.Funcion.LoadingEffect;
import com.example.project_1_java.InterFace.OnLogOut;
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
            this.getSharedPreferences("temporaryData", Context.MODE_PRIVATE)
                    .edit()
                    .remove("userName")
                    .remove("avatarProfile")
                    .apply();
            this.getSharedPreferences("saveLogin", Context.MODE_PRIVATE)
                    .edit()
                    .remove("hide")
                    .apply();
            auth.signOut();
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_OK, resultIntent);
            LoadingEffect.showLoading(this,3000, this::finish);

        });
    }
}