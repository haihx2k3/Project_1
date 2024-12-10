package com.example.project_1_java.Product;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.project_1_java.Chat.ChatActivity;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.Header.Adapter.ViewpagerAdapter;
import com.example.project_1_java.Product.Sub.ProductClassifyFragment;
import com.example.project_1_java.R;
import com.example.project_1_java.databinding.ActivityProductInforBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductInforActivity extends AppCompatActivity implements ProductContract.View {
    private ViewPager2 viewPager2;
    private ViewpagerAdapter adapter;
    private ProductPresenter presenter;
    private LoadFragment loadFragment;
    private String id, title, price, sellerId, img;
    private ActivityProductInforBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductInforBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");
        price = getIntent().getStringExtra("price");
        sellerId = getIntent().getStringExtra("sellerId");
        presenter = new ProductPresenter(this);
        loadFragment = new LoadFragment();
        setupEvent();
        setupListener();
        onBackPress();
    }

    private void setupEvent() {
        binding.txtTittle.setText(title);
        binding.txtPrice.setText(price);
        presenter.onViewCreated(id, sellerId);
    }

    private void setupListener() {
        binding.overlay.setOnClickListener(v -> overlay());
        binding.btnBack.setOnClickListener(v -> finish());
        binding.imgCancel.setOnClickListener(v ->onCancel());
        binding.btnOderProduct.setOnClickListener(v -> presenter.onProductSub());
        binding.btnChatShop.setOnClickListener(v->presenter.onUserName());
    }

    private void overlay() {
        binding.overlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    onCancel();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void showProductDetails(List<String> imageProduct) {
        img = imageProduct.get(0);
        viewPager2 = binding.viewpagerProduct;
        adapter = new ViewpagerAdapter((ArrayList<String>) imageProduct, viewPager2);
        viewPager2.setAdapter(adapter);
        viewPager2.setOffscreenPageLimit(1);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public void showSellerDetails(String avatar, String userName) {
        Glide.with(this).load(avatar).placeholder(R.drawable.profile).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                if (userName!=null&&!userName.isEmpty()){
                    binding.username.setText(userName);
                    binding.frloading.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (userName!=null&&!userName.isEmpty()){
                    binding.username.setText(userName);
                    binding.frloading.setVisibility(View.GONE);
                }
                return false;
            }
        }).into(binding.imgProfileProduct);
    }

    @Override
    public void showProductsSub() {
        binding.frShowProduct.setVisibility(View.VISIBLE);
        binding.overlay.setVisibility(View.VISIBLE);
        Bundle bundle = new Bundle();
        final Fragment sub = new ProductClassifyFragment();
        bundle.putString("id", id);
        bundle.putString("title", title);
        bundle.putString("price", price);
        bundle.putString("sellerId", sellerId);
        bundle.putString("img", img);
        sub.setArguments(bundle);
        loadFragment.loadFragment(this, R.id.frShowProduct, sub);
    }

    @Override
    public void navigateToChat(String userName,String avatarChat) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("sellerId",sellerId);
        intent.putExtra("userName",userName);
        intent.putExtra("avatar",avatarChat);
        startActivity(intent);
    }
    private void onBackPress() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.frShowProduct.getVisibility() == View.VISIBLE) {
                    onCancel();
                } else {
                    finish();
                }
            }
        });
    }
    private void onCancel() {
        binding.frShowProduct.setVisibility(View.GONE);
        binding.overlay.setVisibility(View.GONE);
    }
}