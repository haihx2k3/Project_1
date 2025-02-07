package com.example.project_1_java.Store.BoothClassify;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1_java.Card.DeliveryFragment;
import com.example.project_1_java.Funcion.LoadFragment;
import com.example.project_1_java.Funcion.LoadingEffect;
import com.example.project_1_java.InterFace.OnSelect;
import com.example.project_1_java.Model.OrderClassifyModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.Adapter.SendingAdapter;
import com.example.project_1_java.Store.InterFace.SendingContract;
import com.example.project_1_java.Store.Presenter.SendingPresenter;
import com.example.project_1_java.databinding.FragmentSendingBinding;

import java.util.List;

public class SendingFragment extends Fragment implements SendingContract.View {
    private FragmentSendingBinding binding;
    private SendingContract.Presenter presenter;
    private LoadFragment loadFragment;
    private SendingAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSendingBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadFragment = new LoadFragment();
        presenter = new SendingPresenter(this);
        presenter.onGetData();
        setupView();
    }

    private void setupView() {
        binding.rcSending.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void setupListener(Fragment fragment) {
        binding.frSending.setVisibility(View.VISIBLE);
        loadFragment.loadFragment(this, R.id.frSending,fragment);
    }

    @Override
    public void showOrder(List<OrderClassifyModel> sending) {
        binding.rcSending.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SendingAdapter(sending, new OnSelect() {
            @Override
            public void onSelected(int pos) {
                OrderClassifyModel model = sending.get(pos);
                Bundle bundle = new Bundle();
                bundle.putString("idOrder", model.getIdOrder());
                bundle.putString("info", model.getInfo());
                bundle.putString("address", model.getAddress());
                bundle.putString("price", model.getTotal());
                bundle.putString("content", model.getTitle()+"\n- "+model.getType());
                bundle.putInt("amount",model.getCount());
                PrintfOrder fragment = new PrintfOrder();
                fragment.setArguments(bundle);
                setupListener(fragment);
            }
        });
        binding.rcSending.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL);
        binding.rcSending.addItemDecoration(divider);
    }

    @Override
    public void showError() {
        binding.rcSending.setVisibility(View.GONE);
        binding.lnWaitSending.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShimmer() {
        new Handler(Looper.getMainLooper()).postDelayed(()->{
            binding.shimmerSending.hideShimmer();
            binding.shimmerSending.stopShimmer();
            binding.shimmerSending.setVisibility(View.GONE);
        },3000);
    }
}