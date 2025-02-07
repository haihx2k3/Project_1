package com.example.project_1_java.Store;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_1_java.Model.BranchModel;
import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.R;
import com.example.project_1_java.Store.InterFace.SellContract;

import java.util.ArrayList;
import java.util.List;

public class SellPresenter implements SellContract.Presenter{
    private final SellContract.View view;
    private final Context context;
    private final List<Uri> imageUris;
    private final SellViewModel viewModel;
    private final List<ClassifyModel> listClassify = new ArrayList<>();
    List<BranchModel> branchModels = new ArrayList<>();
    private final ActivityResultLauncher<String> pickImage;
    public  SellPresenter(SellContract.View view ,Context context,List<Uri> imageUris){
        this.view = view;
        this.context = context;
        this.imageUris = imageUris;
        this.viewModel = new ViewModelProvider((Fragment) view).get(SellViewModel.class);
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
        this.pickImage =((Fragment) view).registerForActivityResult(new ActivityResultContracts.GetMultipleContents(),uri->{
            imageUris.addAll(uri);
            view.showImages();
        });
    }

    @Override
    public void onAddImageClicked() {
        pickImage.launch("image/*");
    }

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
    @Override
    public void onClassifyResult(ArrayList<ClassifyModel> result) {
        listClassify.addAll(result);
    }

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
}
