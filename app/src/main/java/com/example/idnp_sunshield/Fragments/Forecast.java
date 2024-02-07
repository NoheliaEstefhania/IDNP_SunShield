package com.example.idnp_sunshield.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.idnp_sunshield.SharePreferences.LocationPreferences;
import com.example.idnp_sunshield.Views.BarChartView;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.daily;
import com.example.idnp_sunshield.databinding.FragmentForecastBinding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forecast extends Fragment  {

    FragmentForecastBinding binding;
    private BarChartView barChartView;
    // Default constructor
    public Forecast() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of the Forecast fragment
    public static Forecast newInstance() {
        Forecast fragment = new Forecast();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using data binding
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        barChartView = binding.barChartView;
        // Fetch weather data
        binding.forecastTitle.setText("Y-axis: UV Index\n" +
                "X-axis: Days of the week\n" +
                "Values: Expected UV radiation level for each day");
        fetchWeather();
        return binding.getRoot();
    }

    // Method to fetch weather data using Retrofit
    public void fetchWeather() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("retrofit: " + retrofit);
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Context context = requireContext();

        LocationPreferences locationPreferences = new LocationPreferences(requireContext());
        System.out.println("SHAREPREFERENTS ubicacion: " + locationPreferences.getTitle());
        // Make an asynchronous call to get UV data from OpenWeatherMap API
        System.out.println("SHAREPREFERENTS ubicacion: " + locationPreferences.getLatitude());
        System.out.println("SHAREPREFERENTS ubicacion: " + locationPreferences.getLongitude());

        // Make an asynchronous API call to get weather data
        Call<UVData> call = interfaceApi.getData(locationPreferences.getLatitude(), locationPreferences.getLongitude(), "hourly,minutely", "c71298943776351e81c2f4e84456a36d");

        //Call<UVData> call = interfaceApi.getData(-16.39889, -71.535, "hourly,minutely", "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<UVData>() {
            @Override
            public void onResponse(Call<UVData> call, Response<UVData> response) {
                if (response.isSuccessful()) {
                    UVData mausanData = response.body();
                    List<daily> td = mausanData.getDaily();
                    List<daily> dataList = new ArrayList<>();
                    System.out.println("I am in the response");


                    for (daily daily : td) {
                        System.out.println("dt: " + date(daily.getDt()));
                        System.out.println("uv: " + daily.getUvi());
                        dataList.add(new daily(daily.getUvi(), daily.getDt()));
                        // Process each Daily object here...
                    }
                    barChartView.setdailyList(dataList);

                }
            }

            @Override
            public void onFailure(Call<UVData> call, Throwable t) {
                System.out.println("Not connected");
            }
        });
    }

    // Method to format the date based on timestamp
    private String date(long timestamp) {
        java.util.Date time = new java.util.Date((long) timestamp * 1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        String formattedDate = sdf.format(time);
        System.out.println("date: " + formattedDate);
        return formattedDate;
    }
}
