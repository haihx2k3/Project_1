package com.example.project_1_java.Header;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project_1_java.Chat.ResentChatActivity;
import com.example.project_1_java.Header.Adapter.BranchHeaderAdapter;
import com.example.project_1_java.Header.Adapter.ViewpagerAdapter;
import com.example.project_1_java.Card.CardActivity;
import com.example.project_1_java.Header.Adapter.ProductAdapter;
import com.example.project_1_java.InterFace.OnClick;
import com.example.project_1_java.Login.LoginActivity;
import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ImageModel;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Product.ProductInforActivity;
import com.example.project_1_java.Search.SearchActivity;
import com.example.project_1_java.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeContract.View {
    private FragmentHomeBinding binding;
    private HomePresenter presenter;
    private ViewPager2 viewPager;
    private ViewpagerAdapter adapterVp;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeViewModel viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        presenter = new HomePresenter(this, getContext(), viewModel);
        viewPager = binding.viewPager2;
        presenter.onDataBranch();
        setupListener();
    }

    private void setupListener() {
        binding.imgSearch.setOnClickListener(v->presenter.onSearchButtonClicked());
        binding.btnCard.setOnClickListener(v->presenter.onCardClicked());
        binding.btnChat.setOnClickListener(v->presenter.onChatClicked());

    }

    @Override
    public void displayImagesPager(List<String> images) {
        adapterVp = new ViewpagerAdapter(new ArrayList<>(images), viewPager);
        viewPager.setAdapter(adapterVp);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPager.removeCallbacks(runnable);
                viewPager.postDelayed(runnable, 2000);
            }
        });
    }

    @Override
    public void displayProducts(List<ModelProduct> products) {
        binding.rvPropose.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
        if (binding.rvPropose.getAdapter() == null) {
            binding.rvPropose.setHasFixedSize(true);
            binding.rvPropose.setAdapter(new ProductAdapter(products, new OnClick() {
                @Override
                public void onClick(int pos) {
                    presenter.onProductClicked(pos, products.get(pos));
                }
            }));
        }
    }

    @Override
    public void displayBranch(List<BranchModel> branch) {
        binding.rvBranch.setLayoutManager(new GridLayoutManager(requireContext(), 1, GridLayoutManager.HORIZONTAL, false));
        binding.rvBranch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalScrollRange = binding.rvBranch.computeHorizontalScrollRange() - binding.rvBranch.computeHorizontalScrollExtent();
                if (totalScrollRange > 0) {
                    int scrollOffset = binding.rvBranch.computeHorizontalScrollOffset();
                    float progress = (float) scrollOffset / totalScrollRange;
                    int linearWidth = binding.lnBranch.getWidth();
                    int indicatorWidth = binding.indicator.getWidth();
                    float translationX = progress * (linearWidth - indicatorWidth);
                    binding.indicator.setTranslationX(translationX);
                }
            }
        });
        if (binding.rvBranch.getAdapter() == null) {
            binding.rvBranch.setHasFixedSize(true);
            binding.rvBranch.setAdapter(new BranchHeaderAdapter(branch, new OnClick() {
                @Override
                public void onClick(int pos) {
                    Toast.makeText(getContext(), branch.get(pos).getTitle(), Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }

    @Override
    public void showSearchActivity() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @Override
    public void showChatActivity() {
        startActivity(new Intent(requireContext(), ResentChatActivity.class));
    }

    @Override
    public void showCardActivity(boolean checkLogin) {
        Intent intent;
        if (checkLogin) {
            intent = new Intent(getContext(), CardActivity.class);
        } else {
            intent = new Intent(getContext(), LoginActivity.class);
        }
        startActivity(intent);
    }


    @Override
    public void showProductActivity(String sellerId, String id, String title, String price) {
        Intent intent = new Intent(getContext(), ProductInforActivity.class);
        intent.putExtra("sellerId", sellerId);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("price", price);
        startActivity(intent);
    }
}