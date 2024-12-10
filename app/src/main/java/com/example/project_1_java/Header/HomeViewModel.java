package com.example.project_1_java.Header;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_1_java.Model.ImageModel;
import com.example.project_1_java.Model.ModelProduct;
import com.example.project_1_java.Utils.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    // Show Product
    private MutableLiveData<List<ModelProduct>> _mListProduct = new MutableLiveData<>();
    public LiveData<List<ModelProduct>> mListProduct = _mListProduct;

    // ViewPager 2
    private MutableLiveData<List<String>> _mListImgVp2LiveData = new MutableLiveData<>();
    public LiveData<List<String>> mListImgVp2 = _mListImgVp2LiveData;

    private FirebaseUtil firebaseUtil;
    public  HomeViewModel() {
        firebaseUtil = new FirebaseUtil();
        initData();
        initDataVp2();
    }

    private void initData() {
        firebaseUtil.getItemProduct(new FirebaseUtil.ItemCallback<List<ModelProduct>>() {
            @Override
            public void onResult(List<ModelProduct> result) {
                _mListProduct.setValue(result);
            }
        }, new FirebaseUtil.FailureCallback() {
            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    public void searchProducts(String query) {

    }

    private void initDataVp2() {
      firebaseUtil.getAds(new FirebaseUtil.ItemCallback<List<String>>() {
          @Override
          public void onResult(List<String> result) {
            _mListImgVp2LiveData.setValue(result);
          }
      }, new FirebaseUtil.FailureCallback() {
          @Override
          public void onFailure(String errorMessage) {

          }
      });
    }
}
