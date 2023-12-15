package com.example.idnp_sunshield.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Views.BarChartView;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.MausanData;
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

public class Forecast extends Fragment {

    FragmentForecastBinding binding;
    private BarChartView barChartView;

    public Forecast() {
        // Required empty public constructor
    }

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
        // Inflate the layout for this fragment
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        barChartView = binding.barChartView;

        fetchWeather();
        return binding.getRoot();
    }

    public void fetchWeather (){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        System.out.println("retrofit: " + retrofit);
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);

        Call<MausanData> call = interfaceApi.getData(-16.39889,-71.535,"hourly,minutely","c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<MausanData>() {
            @Override
            public void onResponse(Call<MausanData> call, Response<MausanData> response) {
                if (response.isSuccessful()){
                    MausanData mausanData = response.body();
                    main to = mausanData.getMain();
                    current tc = mausanData.getCurrent();
                    List<daily> td = mausanData.getDaily();
                    List<daily> dataList = new ArrayList<>();
                    System.out.println("Estoy en la respuesta");
                    binding.forecastTitle.setText("Estoy Reemplazando");
                    for (daily daily : td) {
                        System.out.println("dt: " + date(daily.getDt()));
                        System.out.println("uv: " + daily.getUvi());
                        dataList.add(new daily(daily.getUvi() , daily.getDt()));
                        // Procesa cada objeto Daily aquí...
                    }
                    barChartView.setdailyList(dataList);
                    /**/
                }
            }

            @Override
            public void onFailure(Call<MausanData> call, Throwable t) {
                System.out.println("No conectado");
            }
        });
    }

    private String date(long timestamp){
        //long timestamp = 1684929490L;
        java.util.Date time=new java.util.Date((long)timestamp*1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        String formattedDate = sdf.format(time);
        System.out.println("supuesta fecha: " + formattedDate);
        return formattedDate;
    }
}