package com.example.project_1_java.ApiLocation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressService {
    private static final String BASE_URL = "https://nominatim.openstreetmap.org/";
    private final NominatimApi api;

    public AddressService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(NominatimApi.class);
    }
    public String getAddressFromCoordinates(double latitude, double longitude) throws Exception {
        Call<NominatimResponse> call = api.getAddress("jsonv2", latitude, longitude);
        Response<NominatimResponse> response = call.execute();

        if (response.isSuccessful() && response.body() != null) {
            NominatimResponse.Address address = response.body().address;

            List<String> addressParts = new ArrayList<>();
            addIfNotEmpty(addressParts, address.road);
            addIfNotEmpty(addressParts, address.cityDistrict);
            addIfNotEmpty(addressParts, address.quarter);
            addIfNotEmpty(addressParts, address.suburb);
            addIfNotEmpty(addressParts, address.city);

            return String.join(", ", addressParts);
        } else {
            throw new Exception("Failed to fetch address: " + response.errorBody().string());
        }
    }
    private void addIfNotEmpty(List<String> list, String value) {
        if (value != null && !value.isEmpty()) {
            list.add(value);
        }
    }
}
