package com.example.idnp_sunshield.Fragments;

import static android.icu.text.MessagePattern.Part.Type.ARG_NAME;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.current;

import com.example.idnp_sunshield.SharePreferences.LocationPreferences;
import com.example.idnp_sunshield.databinding.FragmentUVIndexBinding;

import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UV_index extends Fragment {
    FragmentUVIndexBinding binding;

    public UV_index() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the binding object for the UVIndex fragment
        binding = FragmentUVIndexBinding.inflate(getLayoutInflater());
    }

    // Broadcast Receiver to handle UV index updates
    private BroadcastReceiver uviUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("com.example.idnp_sunshield.ACTION_UVI_UPDATE")) {
                double uviValue = intent.getDoubleExtra("UVI_VALUE", 0.0);
                System.out.println("SERVICE BACKGROUND Índice UV actualizado: " + uviValue);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment using the binding object
        binding = FragmentUVIndexBinding.inflate(inflater, container, false);
        // Register the Broadcast Receiver
        System.out.println("ANTES DE ON RECEIVE BROADCAST");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.idnp_sunshield.ACTION_UVI_UPDATE");
        requireActivity().registerReceiver(uviUpdateReceiver, intentFilter);

        // Fetch and update UI components
        fetchUVIndex();
        return binding.getRoot();
    }

    // Unregister the Broadcast Receiver when the view is destroyed
    @Override
    public void onDestroyView() {
        requireActivity().unregisterReceiver(uviUpdateReceiver);
        super.onDestroyView();
    }

    // Fetches weather data using Retrofit and OpenWeatherMap API
    public void fetchUVIndex() {
        // Create a Retrofit instance with the base URL and GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Update UI components with UV index value
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        Context context = requireContext();
        LocationPreferences locationPreferences = new LocationPreferences(requireContext());
        System.out.println("SHAREPREFERENTS ubicacion: " + locationPreferences.getTitle());
        // Make an asynchronous call to get UV data from OpenWeatherMap API
        System.out.println("SHAREPREFERENTS ubicacion: " + locationPreferences.getLatitude());
        System.out.println("SHAREPREFERENTS ubicacion: " + locationPreferences.getLongitude());

        Call<UVData> call = interfaceApi.getData(locationPreferences.getLatitude(), locationPreferences.getLongitude(), "hourly,daily", "c71298943776351e81c2f4e84456a36d");
        //Call<UVData> call = interfaceApi.getData(-16.39889, -71.535, "hourly,daily", "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<UVData>() {
            @Override
            public void onResponse(Call<UVData> call, Response<UVData> response) {
                if (response.isSuccessful()) {
                    // Retrieve UVData from the response
                    UVData uvData = response.body();

                    current tc = uvData.getCurrent();

                    // Update UI components with UV index value
                    binding.uvIndexNumber.setText(String.valueOf(tc.getUvi()));

                    // UV index recommendations based on the UV index value
                    HashMap<String, String> recommendations = new HashMap<>();
                    recommendations.put("Low", "On sunny days, it's recommended to wear sunglasses. If your skin is sensitive, don't forget the SPF 30+ sunscreen. Pay attention to surfaces that reflect UV rays.");
                    recommendations.put("Moderate", "It's better to seek shade around noon. Don't forget your protective clothing, hat, and sunglasses. Remember to reapply the SPF 30+ sunscreen every two hours.");
                    recommendations.put("High", "Try to limit your sun exposure between 10 a.m. and 4 p.m. Seek shade, wear protective clothing, use a hat and sunglasses. Don't forget to reapply sunscreen every two hours.");
                    recommendations.put("Very High", "It's advisable to minimize sun exposure between 10 a.m. and 4 p.m. Seek shade, wear protective clothing, use a hat and sunglasses. Remember to reapply sunscreen every two hours.");
                    recommendations.put("Extreme", "Avoid sun exposure between 10 a.m. and 4 p.m. Seek shade, wear protective clothing, use a hat and sunglasses. Remember to reapply sunscreen every two hours.");

                    // Determine UV index grade
                    String grade;
                    if (tc.getUvi() <= 2) grade = "Low";
                    else if (3 <= tc.getUvi() && tc.getUvi() <= 5) grade = "Moderate";
                    else if (6 <= tc.getUvi() && tc.getUvi() <= 7) grade = "High";
                    else if (8 <= tc.getUvi() && tc.getUvi() <= 10) grade = "Very High";
                    else grade = "Extreme";

                    // Set UV index grade, recommendation, and date
                    binding.uvGrade.setText(grade);
                    binding.uvRecomendation.setText(recommendations.get(grade));
                    binding.uvDate.setText(date(tc.getDt()));
                }
            }

            @Override
            public void onFailure(Call<UVData> call, Throwable t) {
                // Handle the failure scenario
                binding.uvIndexNumber.setText("no answer");
            }
        });
    }
    // Format timestamp to a readable date
    private String date(long timestamp) {
        java.util.Date time = new java.util.Date((long) timestamp * 1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, MMMM dd, yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("America/Lima"));
        return sdf.format(time);
    }




}