package com.example.project_1_java.ApiLocation;

import com.google.gson.annotations.SerializedName;
public class NominatimResponse {
    @SerializedName("address")
    public Address address;
    public static class Address {
        @SerializedName("road")
        public String road;

        @SerializedName("city_district")
        public String cityDistrict;

        @SerializedName("quarter")
        public String quarter;
        @SerializedName("suburb")
        public String suburb;
        @SerializedName("city")
        public String city;
    }
}

