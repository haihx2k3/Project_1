package com.example.project_1_java.Store.BoothClassify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.InterFace.OnClickBooth;
import com.example.project_1_java.Model.BoothModel;
import com.example.project_1_java.Store.Adapter.AvailableAdapter;
import com.example.project_1_java.Store.InterFace.AvailableContract;
import com.example.project_1_java.Store.Presenter.AvailablePresenter;
import com.example.project_1_java.databinding.FragmentAvailableBinding;

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
    public void showProduct(List<BoothModel> available) {
        binding.rcAvailable.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AvailableAdapter(available, new OnClickBooth() {
            @Override
            public void onDelete(int pos) {

            }

            @Override
            public void onUpdate(int pos) {

            }
        });
        binding.rcAvailable.setAdapter(adapter);
    }

    @Override
    public void showError() {
        binding.lnWaitOder.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        
    }

}