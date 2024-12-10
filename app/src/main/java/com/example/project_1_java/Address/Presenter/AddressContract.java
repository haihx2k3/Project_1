package com.example.project_1_java.Address.Presenter;

import com.example.project_1_java.Model.InfoModel;

import java.util.List;

public interface AddressContract {
    interface View {
        void showAddresses(List<InfoModel> addresses);
        void onDeleteAddressSuccess();
        void navigateToLocationFragment();
        void onSelectLocation(String name,String phone,String location,String locationPLus,Integer id);
    }

    interface Presenter {
        void loadAddresses();
        void deleteAddress(int position);
        void onRadioCheck(int position);
    }
}
