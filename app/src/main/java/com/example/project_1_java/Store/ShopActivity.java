package com.example.project_1_java.Store;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.ActivitySellBinding;

public class ShopActivity extends AppCompatActivity {
    private ActivitySellBinding binding;
    private LoadFragment loadFragment;
    private String userName,uid,avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySellBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadFragment = new LoadFragment();
        setupListener();
        getDataFromFooter();
    }

    private void getDataFromFooter() {
        userName = getIntent().getStringExtra("name");
        binding.txtUserNameShop.setText(userName);
        uid = getIntent().getStringExtra("uid");
        String avatar = getIntent().getStringExtra("avt");
        Glide.with(this).load(avatar).placeholder(R.drawable.profile).into(binding.cirImgShop);
    }

    private void setupListener() {
        binding.imgBack.setOnClickListener(view -> finish());
        binding.lnMyProduct.setOnClickListener(v-> {
            Fragment sub = new BoothFragment();
            Bundle bundle = new Bundle();
            bundle.putString("uid",uid);
            sub.setArguments(bundle);
            load(sub);
        });
        binding.lnSell.setOnClickListener(v->load(new SellFragment()));
    }
    private void load(Fragment fr){
        binding.frSellMain.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this,R.id.frSellMain,fr);
    }

}