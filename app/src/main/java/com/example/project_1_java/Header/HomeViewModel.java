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
    private final MutableLiveData<List<String>> _mListImgVp2 = new MutableLiveData<>();
    public final LiveData<List<String>> mListImgVp2 = _mListImgVp2;
    private final FirebaseUtil firebaseUtil;
    //Constructor
    public HomeViewModel() {
        firebaseUtil = new FirebaseUtil();
        fetchProducts();
        fetchAds();
    }
    //get products
    private void fetchProducts() {
        firebaseUtil.getItemProduct(result -> {
            if (result != null && !result.isEmpty()) {
                List<ModelProduct> current = _mListProduct.getValue();
                if (current == null) current = new ArrayList<>();
                current.addAll(result);
                _mListProduct.postValue(current);
            } else {
                return;
            }
        }, errorMessage -> {
            _mListProduct.postValue(new ArrayList<>());
            Log.d("Error HomeViewModel", "Fetch products");
        });
    }
    //Get 10 data at a time
    public void loadMoreProducts() {
        fetchProducts();
    }
    //Get promotion product
    private void fetchAds() {
        firebaseUtil.getAds(result -> {
            if (result != null) {
                _mListImgVp2.setValue(result);
            } else {
                _mListImgVp2.setValue(new ArrayList<>());
            }
        }, errorMessage -> {
            _mListImgVp2.setValue(new ArrayList<>());
            Log.d("Error HomeViewModel", "fetch Ads");
        });

    }
}
