package com.example.project_1_java.Footer.InterFace;

public interface SecondContract {
    interface View {
        void showLoggedInState();
        void showLoggedOutState();
        void displayUserInfo(String userName, String avatarProfile);
    }

    interface Presenter {
        void onResume();
        void onProfileImageClicked();
        void onLoginClicked();
        void onSettingsClicked();
        void onSellProductClicked();
    }
}
