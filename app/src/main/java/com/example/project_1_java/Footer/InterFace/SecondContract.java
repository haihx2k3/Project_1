package com.example.project_1_java.Footer.InterFace;

public interface SecondContract {
    interface View {
        void showLoggedInState();
        void showLoggedOutState();
        void displayUserInfo(String userName, String avatarProfile);
        void showChatActivity();
        void showCardActivity(boolean checkLogin);
    }

    interface Presenter {
        void onCreateStatus();
        void loadTemporary();
        void onProfileImageClicked();
        void onLoginClicked();
        void onSettingsClicked();
        void onSellProductClicked();
        void clearTemporaryData();
        void onChatClicked();
        void onCardClicked();
    }
}
