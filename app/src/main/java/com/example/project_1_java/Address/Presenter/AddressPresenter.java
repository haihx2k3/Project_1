package com.example.project_1_java.Address.Presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project_1_java.Model.InfoModel;

import java.util.ArrayList;
import java.util.List;

public class AddressPresenter implements AddressContract.Presenter {
    private final AddressContract.View view;
    private final SQLiteDatabase database;
    private final List<InfoModel> infoList = new ArrayList<>();
    public AddressPresenter (AddressContract.View view, SQLiteDatabase database){
        this.view = view;
        this.database = database;
    }
    @Override
    public void loadAddresses() {
        infoList.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM INFOR", null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow("Id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
                String locationPlus = cursor.getString(cursor.getColumnIndexOrThrow("LocationPlus"));
                infoList.add(new InfoModel(id, name, phone, location, locationPlus));
            } while (cursor.moveToNext());
        }
        cursor.close();
        view.showAddresses(infoList);
    }

    @Override
    public void deleteAddress(int position) {
        String id = infoList.get(position).getId();
        database.delete("INFOR", "id=?", new String[]{id});
        infoList.remove(position);
        view.onDeleteAddressSuccess();
    }

    @Override
    public void onRadioCheck(int position) {
        InfoModel info = infoList.get(position);
        view.onSelectLocation(info.getName(), info.getPhone(), info.getLocation(), info.getLocationPlus(), position);
    }
}
