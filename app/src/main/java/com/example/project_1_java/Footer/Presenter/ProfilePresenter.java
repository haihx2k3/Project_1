package com.example.project_1_java.Footer.Presenter;

import android.app.AlertDialog;
import android.net.Uri;

import androidx.fragment.app.FragmentActivity;

import com.example.project_1_java.Footer.InterFace.ProfileContract;
import com.example.project_1_java.Store.SellViewModel;
import com.example.project_1_java.Utils.FirebaseUtil;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfilePresenter implements ProfileContract.Presenter {
    private final ProfileContract.View view;
    private final SellViewModel viewModel;
    private FragmentActivity activity;
    private ExecutorService executor;
    //Constructor
    public ProfilePresenter(ProfileContract.View view, SellViewModel viewModel,FragmentActivity activity) {
        this.activity = activity;
        this.view = view;
        this.viewModel = viewModel;
        this.executor = Executors.newSingleThreadExecutor();
    }
    //Get info user
    @Override
    public void loadProfile(String uid) {
        new FirebaseUtil().getProfile((fetch)->{
            String gender = fetch.first;
            String birth = fetch.second;
            view.setGender(gender);
            view.setBirthDay(birth);
        });
    }
    //Save data to firebase
    @Override
    public void saveProfile(String name, String gender, String birthDay, Uri img) {
        view.showLoading();
        viewModel.updateUser(name, gender, birthDay, img);
        viewModel.getDataSaved().observeForever(isSaved->{
            if (isSaved){
                view.hideLoading();
                view.finishActivity();
            }
        });
    }
    //Open Gallery
    @Override
    public void onOpenImage() {
        view.openImagePicker();
    }
    //Process when access is granted.
    @Override
    public void onPermissionGranted() {
        view.openImagePicker();
    }
    //Process when access is denied
    @Override
    public void onPermissionDenied() {
        view.onSetting();
    }
    //Open dialog date
    @Override
    public void onDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày sinh")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = format.format(calendar.getTime());
            view.setBirthDay(formattedDate);
        });
        datePicker.show(activity.getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
    }
    //Open dialog gender
    @Override
    public void onGender() {
        String[] options = {"Nam", "Nữ"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(options, (dialog, which) -> view.setGender(options[which]));
        builder.show();

    }
}
