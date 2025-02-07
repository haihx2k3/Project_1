package com.example.project_1_java.ApiLocation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimApi {
    @GET("reverse")
    Call<NominatimResponse> getAddress(
            @Query("format") String format,
            @Query("lat") double latitude,
            @Query("lon") double longitude
    );
}
