package com.example.idnp_sunshield.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.country;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.databinding.FragmentLocationBinding;
import com.example.idnp_sunshield.databinding.FragmentUVIndexBinding;

import java.util.HashMap;

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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Location.
     */
    // TODO: Rename and change types and number of parameters
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
        fetchWeather();
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

        // Make an asynchronous call to get UV data from OpenWeatherMap API
        Call<country> call = interfaceApi.getContry( "London",5, "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<country>() {
            @Override
            public void onResponse(Call<country> call, Response<country> response) {
                if (response.isSuccessful()) {
                    // Retrieve UVData from the response
                    country country = response.body();
                    // Extract main and current data from UVData
                    //main to = country.getMain();
                    double tcLan = country.getLat();
                    double tcLot = country.getLot();
                    // Update UI components with UV index value
                    binding.showCountry.setText(String.valueOf(tcLan));

                }
            }

            @Override
            public void onFailure(Call<country> call, Throwable t) {
                // Handle the failure scenario
                binding.showCountry.setText("no answer");
            }
        });
    }
}