package com.example.project_1_java.Funcion;

import android.content.Intent;

import com.example.project_1_java.Model.UserModel;

public class AndroidUtils {
    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("userName",model.getUserName());
        intent.putExtra("sellerId",model.getUser());
        intent.putExtra("avatar",model.getImgProfile());
    }
}
