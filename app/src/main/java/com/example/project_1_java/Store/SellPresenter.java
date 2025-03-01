package com.example.project_1_java.Store;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.Classify.ClassifyFragment;
import com.example.project_1_java.Store.InterFace.SellContract;

import java.util.ArrayList;
import java.util.List;

public class SellPresenter implements SellContract.Presenter{
    private final SellContract.View view;
    private final Context context;
    private  List<Uri> imageUris;
    private final SellViewModel viewModel;
    private final List<ClassifyModel> listClassify = new ArrayList<>();
    List<BranchModel> branchModels = new ArrayList<>();
    //Constructor
    public  SellPresenter(SellContract.View view ,Context context){
        this.view = view;
        this.context = context;
        this.viewModel = new ViewModelProvider((Fragment) view).get(SellViewModel.class);
        //Check completion status
        this.viewModel.getDataSaved().observe((Fragment) view, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSaved) {
                if (isSaved){
                    view.hideLoading();
                    view.showSuccessDialog();
                    view.clearInputs();
                }
            }
        });
    }
    //Upload data firebase
    @Override
    public void onUploadClicked(String title, String description, String price,String branch) {
        if (imageUris.isEmpty()||title.isEmpty()||description.isEmpty()||price.isEmpty()){
            view.showError("Vui lòng điền đầy đủ thông tin");
            return;
        }else {
            view.showLoading();
            viewModel.saveDataSell(title,description,price,imageUris,listClassify,branch);
        }

    }
    //receive data
    @Override
    public void onClassifyResult(ArrayList<ClassifyModel> result) {
        listClassify.addAll(result);
    }
    //Get data Values
    @Override
    public void onDataBranch() {
        String[] branchNames = context.getResources().getStringArray(R.array.branch_names);
        TypedArray branchImages = context.getResources().obtainTypedArray(R.array.branch_images);

        for (int i = 0; i < branchNames.length; i++){
            int imageResId = branchImages.getResourceId(i, R.drawable.phone);
            branchModels.add(new BranchModel(branchNames[i],imageResId));
        }
        branchImages.recycle();
        view.showBranch(branchModels);
    }
    //Processing image
    @Override
    public void handleImagePicked(List<Uri> uriList) {
        if (uriList!=null&&!uriList.isEmpty()){
            imageUris = uriList;
            view.showImages(uriList);
        }
    }
    //Open fragment with data
    @Override
    public void openCategory() {
        ClassifyFragment sub = new ClassifyFragment();
        Bundle bundle = new Bundle();
        if (!listClassify.isEmpty()){
            bundle.putParcelableArrayList("Category",new ArrayList<>(listClassify));
            sub.setArguments(bundle);
        }
        view.showFragment(sub);
    }

    @Override
    public void onOpenImage() {
        view.openImagePicker();
    }

    @Override
    public void onPermissionGranted() {
        view.openImagePicker();
    }

    @Override
    public void onPermissionDenied() {
        view.onSetting();
    }
}
