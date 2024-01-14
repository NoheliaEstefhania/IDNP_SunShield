package com.example.idnp_sunshield.Interfaces;

import retrofit2.Call;

import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.country;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceApi {
    // Define an endpoint for getting weather data using the OpenWeatherMap API
    @GET("data/3.0/onecall")
    Call<UVData> getData(
            // Define parameters for the API call
            @Query("lat") double lat,         // Latitude
            @Query("lon") double lon,         // Longitude
            @Query("exclude") String exclude, // Parts of the response to exclude
            @Query("appid") String APIKEY      // API key for authentication
    );

    @GET("geo/1.0/direct")
    Call<country> getContry(
            // Define parameters for the API call
            @Query("q") String country,         // Latitude
            @Query("limit") double limit,
            @Query("appid") String APIKEY      // API key for authenticatio
    );
}
