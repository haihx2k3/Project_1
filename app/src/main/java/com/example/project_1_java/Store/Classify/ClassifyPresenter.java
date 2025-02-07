package com.example.project_1_java.Store.Classify;

import com.example.project_1_java.Model.ClassifyModel;
import com.example.project_1_java.Store.InterFace.ClassifyContract;

import java.util.ArrayList;

public class ClassifyPresenter implements ClassifyContract.Presenter {
    private ClassifyContract.View view;
    private ArrayList<ClassifyModel> mListSub;
    private Integer selectedImagePosition = null;
    private Integer selectedUpdatePosition = null;
    public ClassifyPresenter (ClassifyContract.View view){
        this.view = view;
        mListSub = new ArrayList<>();
    }
    @Override
    public void onAddRcClassifyClicked() {
        view.loadFragment(new ClassifyInputFragment());
        view.disableNameField();
    }

    @Override
    public void onBackClicked() {
        view.closeActivityWithResult(null);
    }

    @Override
    public void onSaveClicked() {
        for (ClassifyModel model : mListSub) {
            if (model.getImg() == null || model.getImg().isEmpty()) {
                view.showError("Vui lòng điền đầy đủ thông tin");
                return;
            }
        }
        view.closeActivityWithResult(mListSub);
    }

    @Override
    public void onImageClicked(int position) {
        selectedImagePosition=position;
        view.openImagePicker();
    }

    @Override
    public void onEditClicked(int position) {
        selectedUpdatePosition=position;
        view.loadFragment(new UpdateClassifyFragment());
    }

    @Override
    public void onDeleteClicked(int position) {
        mListSub.remove(position);
    }

    @Override
    public void onPermissionGranted() {
        view.openImagePicker();
    }

    @Override
    public void onPermissionDenied() {
        view.onSetting();
    }

    @Override
    public void handleImagePicked(String imageUri) {
        if (selectedImagePosition!=null){
            mListSub.get(selectedImagePosition).setImg(imageUri);
            view.showVariants(mListSub);
        }
    }

    @Override
    public void handleDataReceived(String title, String price) {
        mListSub.add((new ClassifyModel(title,price,"")));
        view.showVariants(mListSub);
    }

    @Override
    public void handleDataUpdated(String title, String price) {
        if (selectedUpdatePosition!=null){
            ClassifyModel model = mListSub.get(selectedUpdatePosition);
            if (title != null && !title.isEmpty()) {
                model.setTitle(title);
            }
            if (price != null && !price.isEmpty()) {
                model.setPrice(price);
            }
            view.showVariants(mListSub);
        }
    }

    @Override
    public void handleDataTemporary(ArrayList<ClassifyModel> temporary) {
        mListSub = temporary;
        view.showVariants(mListSub);
    }
}
