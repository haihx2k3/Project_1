package com.example.project_1_java.Store.BoothClassify;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project_1_java.Funcion.LoadingEffect;
import com.example.project_1_java.InterFace.OnClickPending;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.Adapter.PendingAdapter;
import com.example.project_1_java.Store.InterFace.PendingContract;
import com.example.project_1_java.Store.Presenter.PendingPresenter;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.example.project_1_java.databinding.FragmentBoothBinding;
import com.example.project_1_java.databinding.FragmentPendingBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class PendingFragment extends Fragment implements PendingContract.View {
    private FragmentPendingBinding binding;
    private PendingContract.Presenter presenter;
    private PendingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPendingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new PendingPresenter(this);
        presenter.onGetData();
        setupView();
    }

    private void setupView() {
        binding.rcPending.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager!=null){
                        int totalItemCount = layoutManager.getItemCount();
                        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                        if (lastVisibleItemPosition >= totalItemCount - 1) {
                            LoadingEffect.showLoading(getContext(),3000,()->{
                                presenter.onGetData();
                            });
                        }

                    }
                }
            }
        });
    }

    @Override
    public void showProduct(List<OrderClassifyModel> pending) {
        binding.rcPending.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PendingAdapter(pending, new OnClickPending() {
            @Override
            public void onConfirm(int pos) {
                new FirebaseUtil().addItemSending(pending.get(pos).getSellerId(), pending.get(pos).getIdOrder(), pending.get(pos), () -> {
                    LoadingEffect.showLoading(getContext(),3000,()-> {
                        adapter.notifyDataSetChanged();
                        Snackbar.make(getView(), "Xác nhận thành công", Snackbar.LENGTH_SHORT).show();
                    });
                }, errorMessage -> {
                    Snackbar.make(getView(), "Xác nhận không thành công", Snackbar.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onCancel(int pos) {

            }
        });
        binding.rcPending.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        binding.rcPending.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void showError() {
        binding.shimmerPending.setVisibility(View.GONE);
        binding.lnWaitOder.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShimmer() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.shimmerPending.hideShimmer();
            binding.shimmerPending.stopShimmer();
            binding.shimmerPending.setVisibility(View.GONE);
            binding.lnPending.setVisibility(View.VISIBLE);
        }, 3000);
    }
}