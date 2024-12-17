package com.example.project_1_java.Header;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<ModelProduct>> _mListProduct = new MutableLiveData<>();
    public final LiveData<List<ModelProduct>> mListProduct = _mListProduct;
    private final MutableLiveData<List<String>> _mListImgVp2LiveData = new MutableLiveData<>();
    public final LiveData<List<String>> mListImgVp2 = _mListImgVp2LiveData;
    private final FirebaseUtil firebaseUtil;
    public HomeViewModel() {
        firebaseUtil = new FirebaseUtil();
        fetchProducts();
        fetchAds();
    }

    private void fetchProducts() {
        firebaseUtil.getItemProduct(result -> {
            if (result != null &&! result.isEmpty()) {
                List<ModelProduct> current = _mListProduct.getValue();
                if (current==null) current = new ArrayList<>();
                current.addAll(result);
                _mListProduct.setValue(current);
            } else {
                _mListProduct.setValue(new ArrayList<>());
            }
        }, errorMessage -> {

            _mListProduct.setValue(new ArrayList<>());
        });
    }
    public void searchProducts(String query) {

    }

    public void loadMoreProducts() {
        fetchProducts();
    }

    private void fetchAds() {
        firebaseUtil.getAds(result -> {
            if (result != null) {
                _mListImgVp2LiveData.setValue(result);
            } else {
                _mListImgVp2LiveData.setValue(new ArrayList<>());
            }
        }, errorMessage -> {
            _mListImgVp2LiveData.setValue(new ArrayList<>());
        });
    }
}
