package com.example.idnp_sunshield.Fragments;

import android.content.IntentFilter;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.Services.DataUpdateReceiver;
import com.example.idnp_sunshield.Singleton.LocationSingleton;
import com.example.idnp_sunshield.Views.BarChartView;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.daily;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.databinding.FragmentForecastBinding;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forecast extends Fragment implements DataUpdateReceiver.DataUpdateListener {

    FragmentForecastBinding binding;
    private BarChartView barChartView;
    private DataUpdateReceiver dataUpdateReceiver;

    // Default constructor
    public Forecast() {
        // Required empty public constructor
    }

    // Factory method to create a new instance of the Forecast fragment
    public static Forecast newInstance(String param1, String param2) {
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
/*        // Registra el BroadcastReceiver en el método onCreate o en onResume
        dataUpdateReceiver = new DataUpdateReceiver(this);
        IntentFilter intentFilter = new IntentFilter("DataUpdate");
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(dataUpdateReceiver, intentFilter);*/

        // Registra el BroadcastReceiver en el método onCreate o en onResume
        dataUpdateReceiver = new DataUpdateReceiver(this);
        IntentFilter intentFilter = new IntentFilter("DataUpdate");
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(dataUpdateReceiver, intentFilter);
        // Fetch weather data
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
        LocationSingleton locationSingleton = LocationSingleton.getInstance();
        Location ubicacion = locationSingleton.getLocation();

        System.out.println("LOCALIZACION ubicacion: " + ubicacion.getTitle());
        // Make an asynchronous API call to get weather data
        Call<UVData> call = interfaceApi.getData(ubicacion.getLatitude(), ubicacion.getLongitude(), "hourly,minutely", "c71298943776351e81c2f4e84456a36d");

        //Call<UVData> call = interfaceApi.getData(-16.39889, -71.535, "hourly,minutely", "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<UVData>() {
            @Override
            public void onResponse(Call<UVData> call, Response<UVData> response) {
                if (response.isSuccessful()) {
                    UVData mausanData = response.body();
                    main to = mausanData.getMain();
                    current tc = mausanData.getCurrent();
                    List<daily> td = mausanData.getDaily();
                    List<daily> dataList = new ArrayList<>();
                    System.out.println("I am in the response");
                    binding.forecastTitle.setText("I am replacing");

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

    @Override
    public void onDataUpdate(double latitude, double longitude) {
        System.out.println("Respuesta desde forecast en onDataUpdate");
        binding.forecastTitle.setText("Valores recibidos : latitud: "+ latitude +" longitude: "+ longitude);
    }
}
