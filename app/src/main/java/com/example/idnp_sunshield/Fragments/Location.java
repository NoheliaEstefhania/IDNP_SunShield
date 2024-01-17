package com.example.idnp_sunshield.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.lat;
import com.example.idnp_sunshield.Models.lon;
import com.example.idnp_sunshield.databinding.FragmentLocationBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Location#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Location extends Fragment {

    FragmentLocationBinding binding;

    public Location() {
        // Required empty public constructor
    }

    public static Location newInstance(String param1, String param2) {
        Location fragment = new Location();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment using the binding object
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        // Fetch weather data and update UI components
        //fetchWeather();
        fetchWeather2();

        return binding.getRoot();
    }

    public void fetchWeather() {
        // Create a Retrofit instance with the base URL and GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the InterfaceApi using the Retrofit instance
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        System.out.println("Location Messaje");
        // Make an asynchronous call to get UV data from OpenWeatherMap API
        //Call<List<CountryData>> call = interfaceApi.getCountry( "London",1, "c71298943776351e81c2f4e84456a36d");
        Call<List<lat>> call = interfaceApi.getLat( "London",1, "c71298943776351e81c2f4e84456a36d");

        System.out.println("CALL :" + call.request().url());
        call.enqueue(new Callback<List<lat>>() {
            @Override
            public void onResponse(Call<List<lat>> call, Response<List<lat>> response) {
                if (response.isSuccessful()) {
                    // Retrieve UVData from the response
                    //country country = response.body();
                    List<lat> lati = response.body();
                    System.out.println("response.body contenido: " + response.body());
                    System.out.println("Location Messaje on response");
                    System.out.println("Lat[0] valor : " + lati.get(0));                    System.out.println("Lat[0] valor : " + lati.get(0));
                    System.out.println("Lat[0] valor : " + lati.get(0).getLat());


                    // Extract main and current data from UVData
                    //main to = country.getMain();
                    // Update UI components with UV index value
                    binding.showCountry.setText("conexion");
                    //binding.showCountry.setText(String.valueOf(tcLan));

                }
            }

            @Override
            public void onFailure(Call<List<lat>> call, Throwable t) {
                binding.showCountry.setText("no answer");
            }
        });
    }

    public void fetchWeather2() {
        // Create a Retrofit instance with the base URL and GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the InterfaceApi using the Retrofit instance
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        System.out.println("Location Messaje");
        // Make an asynchronous call to get UV data from OpenWeatherMap API
        //Call<List<CountryData>> call = interfaceApi.getCountry( "London",1, "c71298943776351e81c2f4e84456a36d");
        Call<List<lon>> call = interfaceApi.getLon( "London",1, "c71298943776351e81c2f4e84456a36d");

        System.out.println("CALL :" + call.request().url());
        call.enqueue(new Callback<List<lon>>() {
            @Override
            public void onResponse(Call<List<lon>> call, Response<List<lon>> response) {
                if (response.isSuccessful()) {
                    // Retrieve UVData from the response
                    //country country = response.body();
                    List<lon> loni = response.body();
                    System.out.println("response.body contenido: " + response.body());
                    System.out.println("Location Messaje on response");
                    System.out.println("Lat[0] valor : " + loni.get(0));
                    System.out.println("Lat[0] valor : " + loni.get(0));
                    System.out.println("Lat[0] valor : " + loni.get(0).getLon());


                    // Extract main and current data from UVData
                    //main to = country.getMain();
                    // Update UI components with UV index value
                    binding.showCountry.setText("conexion");
                    //binding.showCountry.setText(String.valueOf(tcLan));

                }
            }

            @Override
            public void onFailure(Call<List<lon>> call, Throwable t) {
                binding.showCountry.setText("no answer");
            }
        });
    }
}