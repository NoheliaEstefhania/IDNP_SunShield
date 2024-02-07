package com.example.idnp_sunshield.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Entity.DataBase;
import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.LocationsData;
import com.example.idnp_sunshield.databinding.FragmentLocationBinding;
import com.example.idnp_sunshield.SharePreferences.LocationPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Locations extends Fragment {

    FragmentLocationBinding binding;
    double latitud;
    double longitud;
    String countryLocation;
    public double getLatitud() {
        return latitud;
    }
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
    public double getLongitud() {
        return longitud;
    }
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
    public Locations() {
        // Required empty public constructor
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

        // Set button click listener to fetch
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manejar el clic del botón aquí
                countryLocation = binding.locationText.getText().toString();
                if (TextUtils.isEmpty(countryLocation)) {
                    countryLocation = "London";
                }
                System.out.println("countryLocation: " + countryLocation);
                fetchLocations(countryLocation);
            }
        });
        // Fetch weather data for default location (London) on fragment creation
        if(TextUtils.isEmpty(binding.locationText.getText().toString())){
            countryLocation = "London";
            fetchLocations(countryLocation);
        }

        return binding.getRoot();
    }


    // Method to handle data related to the selected location
    private void data(String countryLocation) {
        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_2_3).allowMainThreadQueries().build();

        double latitud = getLatitud();
        double longitud = getLongitud();

        System.out.println("Valores de latitud y longitud: " + latitud + ", " + longitud);
        List<Location> locationsList;
        locationsList = dataBase.getLocationDAO().getAllLocations();

        boolean locationAlreadyRegistered = false;

        // Check if the current location is already registered in the database
        for (Location location : locationsList) {
            System.out.println("Impresion de valor de location.getLatitude() : "+ location.getLatitude());
            System.out.println("Impresion de valor de latitud : "+ latitud);
            System.out.println("Impresion de valor de location.getLongitude() : "+ location.getLongitude());
            System.out.println("Impresion de valor de longitud : "+ longitud);

            if (location.getLatitude() == latitud && location.getLongitude() == longitud) {
                locationAlreadyRegistered = true;
                System.out.println("Ya está registrada");
                break;
            }
        }

        // If the location is not registered, add it to the database
        if (!locationAlreadyRegistered) {
            System.out.println("entre al registro");
            dataBase.getLocationDAO().addLocation(new Location(longitud, latitud, countryLocation, locationsList.isEmpty()));
        }

        locationsList = dataBase.getLocationDAO().getAllLocations();

        System.out.println("Cantidad de valores de locationsList antes: " + locationsList.size());

        for (Location location : locationsList) {
            System.out.println("Valores de locationsList: Country: "+  location.getTitle() + " Latitud=" + location.getLatitude() +
                    ", Longitud=" + location.getLongitude() + " , State: " + location.getState());
        }

        //binding.showCountry.setText(!locationsList.isEmpty() ? (locationsList.get(0).getLatitude() + locationsList.get(0).getLongitude() + "") : "");
        //binding.showCountry.setText(locationsList.get(0).getLatitude() + " "+ locationsList.get(0).getLongitude() + " ");

        // Inicia el servicio y envía datos a través de un Intent
        System.out.println("SERVICE latitude: " + locationsList.get(0).getTitle());
        System.out.println("SERVICE latitude: " + locationsList.get(0).getLatitude());
        System.out.println("SERVICE longitude: " + locationsList.get(0).getLongitude());

        /*for (int i = 0; i < locationsList.toArray().length; i++) {
            dataBase.getLocationDAO().deleteLocation(locationsList.get(i));
        }*/
        System.out.println("Cantidad de valores de locationsList después: " + locationsList.size());
    }

    // Method to fetch data using Retrofit
    public void fetchLocations(String countryLocation) {
        // Create a Retrofit instance with the base URL and GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create an instance of the InterfaceApi using the Retrofit instance
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        System.out.println("Location Messaje");
        // Make an asynchronous call to get UV data from OpenWeatherMap API
        Call<List<LocationsData>> call = interfaceApi.getLocationData( countryLocation,1, "c71298943776351e81c2f4e84456a36d");

        System.out.println("CALL :" + call.request().url());
        call.enqueue(new Callback<List<LocationsData>>() {
            @Override
            public void onResponse(Call<List<LocationsData>> call, Response<List<LocationsData>> response) {

                if (response.isSuccessful()) {
                    List<LocationsData> lodata = response.body();
                    double lon = lodata.get(0).getLon();
                    double lati = lodata.get(0).getLat();
                    System.out.println("response.body contenido: " + response.body());
                    System.out.println("Location Messaje on response");
                    setLongitud(lon);
                    setLatitud(lati);
                    System.out.println("valor setter de longitud: " + lati + " " + lon);
                    data(countryLocation);
                }
            }
            @Override
            public void onFailure(Call<List<LocationsData>> call, Throwable t) {
                binding.showCountry.setText("no answer");
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}