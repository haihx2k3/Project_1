package com.example.project_1_java.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.project_1_java.R;

public class PermissionManager {
    private final Context context;
    private final Object owner;
    private final ActivityResultLauncher<String> permissionLauncher;
    private final ActivityResultLauncher<Intent> imagePickerLauncher;

    public PermissionManager(Object owner, ActivityResultLauncher<String> permissionLauncher, ActivityResultLauncher<Intent> imagePickerLauncher) {
        if (!(owner instanceof  Fragment || owner instanceof Activity)){
            throw new IllegalArgumentException("PermissionManager not found");
        }
        this.context = (owner instanceof Fragment)?((Fragment) owner).requireContext():(Activity) owner;
        this.owner = owner;
        this.permissionLauncher = permissionLauncher;
        this.imagePickerLauncher = imagePickerLauncher;
    }
    private boolean isPermissionGranted(String permission){
        return ContextCompat.checkSelfPermission(context,permission)== PackageManager.PERMISSION_GRANTED;
    }
    public void requestPermission(String permission){
        if (!isPermissionGranted(permission)){
            permissionLauncher.launch(permission);
        }
    }
    public void showPermissionRationaleDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Yêu cầu quyền truy cập ảnh")
                .setMessage("Ứng dụng cần quyền truy cập ảnh để tiếp tục hoạt động. Vui lòng cấp quyền.")
                .setPositiveButton("Đồng ý", (dialogInterface, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                })
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                ContextCompat.getColor(context, R.color.green));
    }
    public void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }
}
