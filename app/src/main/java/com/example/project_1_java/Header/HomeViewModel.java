package com.example.project_1_java.Header;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<ModelProduct>> _mListProduct = new MutableLiveData<>();
    public final LiveData<List<ModelProduct>> mListProduct = _mListProduct;
    private final MutableLiveData<List<String>> _mListImgVp2LiveData = new MutableLiveData<>();
    public final LiveData<List<String>> mListImgVp2 = _mListImgVp2LiveData;
    private final FirebaseUtil firebaseUtil;
    private final ExecutorService executor;
    public HomeViewModel() {
        firebaseUtil = new FirebaseUtil();
        executor = Executors.newSingleThreadExecutor();
        fetchProducts();
        fetchAds();
    }

    private void fetchProducts() {
        executor.execute(() -> {
            firebaseUtil.getItemProduct(result -> {
                if (result != null &&! result.isEmpty()) {
                    List<ModelProduct> current = new ArrayList<>(Optional.ofNullable(_mListProduct.getValue()).orElse(new ArrayList<>()));
                    current.addAll(result);
                    _mListProduct.postValue(current);
                } else {
                    return;
                }
            }, errorMessage -> {
                _mListProduct.postValue(new ArrayList<>());
                Log.d("Error HomeViewModel","Fetch products");
            });
        });
    }
    public void searchProducts(String query) {

    }

    public void loadMoreProducts() {
        fetchProducts();
    }

    private void fetchAds() {
        executor.execute(() -> {
            firebaseUtil.getAds(result -> {
                if (result != null) {
                    _mListImgVp2LiveData.setValue(result);
                } else {
                    _mListImgVp2LiveData.setValue(new ArrayList<>());
                }
            }, errorMessage -> {
                _mListImgVp2LiveData.setValue(new ArrayList<>());
                Log.d("Error HomeViewModel","fetch Ads");
            });
        });

    }
}
