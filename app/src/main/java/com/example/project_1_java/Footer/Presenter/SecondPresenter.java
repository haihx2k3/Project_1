package com.example.project_1_java.Footer.Presenter;

import android.content.Context;
import android.content.Intent;

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
    private static boolean isFetched = false;
    private Context context;

    public SecondPresenter(SecondContract.View view, FirebaseUtil firebaseUtil, Context context) {
        this.view = view;
        this.firebaseUtil = firebaseUtil;
        this.userId = FirebaseUtil.currentUserId();
        this.context = context;
    }

    @Override
    public void onCreateStatus() {
        boolean loginSuccess = context.getSharedPreferences("saveLogin", Context.MODE_PRIVATE).getBoolean("hide", false);
        if (loginSuccess) {
            view.showLoggedInState();
            if (!isFetched) {
                fetchUser();
                isFetched = true;
            } else {
                loadTemporary();
            }
        } else {
            isFetched = false;
            view.showLoggedOutState();
        }
    }
    @Override
    public void loadTemporary(){
        view.showLoggedInState();
        userName = context.getSharedPreferences("temporaryData", Context.MODE_PRIVATE).getString("userName", null);
        avatarProfile = context.getSharedPreferences("temporaryData", Context.MODE_PRIVATE).getString("avatarProfile", null);
        view.displayUserInfo(userName, avatarProfile);
    }
    @Override
    public void onProfileImageClicked() {
        if (userName==null&&avatarProfile==null){
            return;
        }
        navigateToActivity(ProfileActivity.class);

    }

    @Override
    public void onLoginClicked() {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    public void onSettingsClicked() {
        if (context != null) {
            context.startActivity(new Intent(context, EditActivity.class));
        }

    }

    @Override
    public void onSellProductClicked() {
        navigateToActivity(ShopActivity.class);
    }

    @Override
    public void clearTemporaryData() {
        userName=null;
        avatarProfile = null;
        context.getSharedPreferences("temporaryData", Context.MODE_PRIVATE)
                .edit()
                .remove("userName")
                .remove("avatarProfile")
                .apply();
        view.displayUserInfo(userName,avatarProfile);
    }
    private void navigateToActivity(Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.putExtra("name", userName);
        intent.putExtra("avt", avatarProfile);
        intent.putExtra("uid", userId);
        context.startActivity(intent);
    }
    private void fetchUser() {
        firebaseUtil.getInfoUser((fetchedUserName) -> {
            String user = fetchedUserName.first;
            String avatar = fetchedUserName.second;
            this.userName = user;
            this.avatarProfile = avatar;
            view.displayUserInfo(userName, avatarProfile);
            saveTemporary();
        });
    }
    private void saveTemporary(){
        context.getSharedPreferences("temporaryData", Context.MODE_PRIVATE)
                .edit()
                .putString("userName", userName)
                .putString("avatarProfile", avatarProfile)
                .apply();
    }


}
