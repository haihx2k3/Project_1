package com.example.project_1_java.Footer.Presenter;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.example.project_1_java.Footer.EditActivity;
import com.example.project_1_java.Footer.InterFace.SecondContract;
import com.example.project_1_java.Footer.ProfileActivity;
import com.example.project_1_java.Login.LoginActivity;
import com.example.project_1_java.Store.ShopActivity;
import com.example.project_1_java.Utils.FirebaseUtil;

public class SecondPresenter implements SecondContract.Presenter {
    private final SecondContract.View view;
    private final FirebaseUtil firebaseUtil;
    private String userName;
    private String avatarProfile;
    private final String userId;
    private Context context;
    public SecondPresenter(SecondContract.View view, FirebaseUtil firebaseUtil,Context context) {
        this.view = view;
        this.firebaseUtil = firebaseUtil;
        this.userId = firebaseUtil.currentUserId();
        this.context = context;
    }
    @Override
    public void onResume() {
        Context context = ((Fragment) view).requireContext();
        boolean loginSuccess = context.getSharedPreferences("saveLogin",Context.MODE_PRIVATE).getBoolean("hide",false);
        if (loginSuccess){
            view.showLoggedInState();
            userName = null;
            avatarProfile = null;
            fetchUser();
        }
        else {
            view.showLoggedOutState();
        }
    }

    private void fetchUser() {
        if (userName == null) {
            firebaseUtil.getInfoUser((fetchedUserName) -> {
                String user = fetchedUserName.first;
                String avatar = fetchedUserName.second;
                this.userName = user;
                this.avatarProfile = avatar;
                view.displayUserInfo(userName, avatarProfile);
            });
        } else {
            view.displayUserInfo(userName, avatarProfile);
        }
    }

    @Override
    public void onProfileImageClicked() {
        navigateToActivity(ProfileActivity.class);

    }

    @Override
    public void onLoginClicked() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    public void onSettingsClicked() {
        Context context = ((Fragment) view).getContext();
        context.startActivity(new Intent(context, EditActivity.class));

    }

    @Override
    public void onSellProductClicked() {
        navigateToActivity(ShopActivity.class);
    }
    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("name", userName);
        intent.putExtra("avt", avatarProfile);
        intent.putExtra("uid", userId);
        context.startActivity(intent);
    }
}
