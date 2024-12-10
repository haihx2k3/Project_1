package com.example.project_1_java.Chat.Presenter;

public interface ChatContract {
    interface View {
        void displayUserName(String userName,String avatar);
    }

    interface Presenter {
        void setupChat( String userName,String avatar);
        void sendMessage(String message);
    }
}
