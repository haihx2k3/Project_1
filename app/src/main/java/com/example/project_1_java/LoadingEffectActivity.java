package com.example.project_1_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

public class LoadingEffectActivity extends AppCompatActivity {
    private AlertDialog dialog;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_effect);
        handler = new Handler();
    }
    public void showLoading(long timeoutMillis , Runnable onTimeout) {
        AlertDialog.Builder build = new AlertDialog.Builder(this, R.style.Loading);
        LayoutInflater inflater = getLayoutInflater();
        build.setView(inflater.inflate(R.layout.dialog_loading, null));
        dialog = build.create();
        dialog.setCancelable(false);
        dialog.show();

        handler.postDelayed(() -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                onTimeout.run();
                Log.d("Loading Effect","time out");
            }
        }, timeoutMillis);
    }

    public void hideLoadingEffect() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        handler.removeCallbacksAndMessages(null);
    }
}