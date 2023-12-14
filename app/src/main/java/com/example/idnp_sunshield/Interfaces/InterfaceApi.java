package com.example.idnp_sunshield.Interfaces;

import retrofit2.Call;

import com.example.idnp_sunshield.Models.MausanData;

import retrofit2.http.GET;
import retrofit2.http.Query;
public interface InterfaceApi {

    /*
    @GET("data/2.5/weather")
    Call<MausanData> getData(
            @Query("q") String q,
            @Query("appid") String APIKEY,
            @Query("units") String units
    );
    */

    @GET("data/3.0/onecall")
    Call<MausanData> getData(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("exclude") String exclude,
            @Query("appid") String APIKEY
    );
}
