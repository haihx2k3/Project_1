package com.example.project_1_java.Address.Presenter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.core.content.ContextCompat;

import com.example.project_1_java.ApiLocation.AddressService;
import com.example.project_1_java.R;
import com.example.project_1_java.Utils.copyDbHelper;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.concurrent.Executors;

public class LocationPresenter implements LocationContract.Presenter{
    private final Context context;
    private LocationContract.View view;
    private final copyDbHelper db;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private MyLocationNewOverlay locationOverlay;
    private AddressService service;
    public LocationPresenter(LocationContract.View view,Context context) {
        this.view = view;
        this.context = context;
        this.db = new copyDbHelper(context);
        this.service = new AddressService();
    }

    @Override
    public void onLocationClick() {
        if (isGPSEnabled()){
            checkLocationPermission();
        }else {
            promptEnableGPS();
        }
    }

    @Override
    public void onAddRecord(String name, String phone, String location, String locationPlus) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", name);
        contentValues.put("Phone", phone);
        contentValues.put("Location", location);
        contentValues.put("LocationPlus", locationPlus);
        SQLiteDatabase database = db.openDatabase();
        long newRowId = database.insert("INFOR", null, contentValues);
        if (newRowId != -1) {
            view.navigateBack();
        }
       database.close();
    }

    @Override
    public void checkFormValidity(String name, String phone, String location, String locationPlus) {
        boolean allFieldsFilled = !name.isEmpty() && !phone.isEmpty() && !location.isEmpty() && !locationPlus.isEmpty();
        String sanitizedPhone = phone.replace(" ", "");
        boolean isPhoneNumberValid = sanitizedPhone.matches("^(\\+84|0)[3|5|7|8|9]\\d{8}$");
        view.enableAddButton(allFieldsFilled && isPhoneNumberValid);
    }

    @Override
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            view.showLoading();
            getLocation();
        } else {
            view.checkPermission();
        }
    }

    @Override
    public void fetchAddress(double latitude, double longitude) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                String address = service.getAddressFromCoordinates(latitude, longitude);
                mainHandler.post(()->{
                    view.displayLocation(address);
                    view.hideLoading();
                });
            } catch (Exception e) {
               mainHandler.post(()->{
                   view.showToast("Failed to fetch address");
                   view.hideLoading();
               });
            }
        });
    }

    @Override
    public void onSetting() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Yêu cầu quyền truy cập vị tró")
                .setMessage("Ứng dụng cần quyền truy cập vị trí để tiếp tục hoạt động. Vui lòng cấp quyền.")
                .setPositiveButton("Đồng ý", (dialogInterface, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                })
                .create();

        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.green));
    }

    @Override
    public void onPermissionGranted() {
       onLocationClick();
    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    private void getLocation() {
        locationOverlay = new MyLocationNewOverlay(new MapView(context));
        locationOverlay.enableMyLocation();
        locationOverlay.runOnFirstFix(() -> {
            if (locationOverlay.getMyLocation() != null) {
                double latitude = locationOverlay.getMyLocation().getLatitude();
                double longitude = locationOverlay.getMyLocation().getLongitude();
                fetchAddress(latitude, longitude);
            }
        });
    }

    private void promptEnableGPS() {
        new AlertDialog.Builder(context)
                .setMessage("Để trải nghiệm tốt hơn bạn có muốn bật vị trí không")
                .setPositiveButton("Có", (dialog, which) -> context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("Không", null)
                .show();
    }
    public void stopLocationUpdates() {
        if (locationOverlay != null) {
            locationOverlay.disableMyLocation();
        }
    }
}
