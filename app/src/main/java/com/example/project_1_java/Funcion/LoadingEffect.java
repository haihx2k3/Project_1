package com.example.project_1_java.Funcion;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import com.example.project_1_java.R;

public class LoadingEffect {
    public static void showLoading(Context context, long timeoutMillis, Runnable onTimeout) {
        AlertDialog dialog;
        Handler handler;
        handler = new Handler();
        AlertDialog.Builder build = new AlertDialog.Builder(context, R.style.Loading);
        LayoutInflater inflater = LayoutInflater.from(context);
        build.setView(inflater.inflate(R.layout.dialog_loading, null));
        dialog = build.create();
        dialog.setCancelable(false);
        dialog.show();

        handler.postDelayed(() -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
                onTimeout.run();
                Log.d("Loading Effect", "time out");
            }
        }, timeoutMillis);
    }
}
