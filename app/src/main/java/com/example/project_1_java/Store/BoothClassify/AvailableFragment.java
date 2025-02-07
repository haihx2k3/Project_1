package com.example.project_1_java.Store.BoothClassify;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.InterFace.OnClickAvailable;
import com.example.project_1_java.Model.AvailableModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.Adapter.AvailableAdapter;
import com.example.project_1_java.Store.Branch.AvailableProduct;
import com.example.project_1_java.Store.InterFace.AvailableContract;
import com.example.project_1_java.Store.Presenter.AvailablePresenter;
import com.example.project_1_java.databinding.FragmentAvailableBinding;

import java.util.ArrayList;
import java.util.List;

public class AvailableFragment extends Fragment implements AvailableContract.View {
    private FragmentAvailableBinding binding;
    private AvailableContract.Presenter presenter;
    private AvailableAdapter adapter;
    private LoadFragment loadFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAvailableBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new AvailablePresenter(this);
        presenter.onGetData();
        loadFragment = new LoadFragment();
    }

    @Override
    public void showProduct(List<AvailableModel> available) {
        binding.rcAvailable.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AvailableAdapter(available, new OnClickAvailable() {
            @Override
            public void onDelete(int pos) {

            }

            @Override
            public void onUpdate(int pos) {
                presenter.onUpdate(pos,available);
            }
        });
        binding.rcAvailable.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL);
        binding.rcAvailable.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void showError() {
        binding.shimmerAvai.setVisibility(View.GONE);
        binding.lnWaitOder.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShimmer() {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            binding.shimmerAvai.hideShimmer();
            binding.shimmerAvai.stopShimmer();
            binding.shimmerAvai.setVisibility(View.GONE);
            binding.rcAvailable.setVisibility(View.VISIBLE);
        },3000);
    }

    @Override
    public void showInfo(Fragment sub) {
        binding.frAvailable.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this,R.id.frAvailable,sub);
    }
}