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
import com.example.idnp_sunshield.Singleton.LocationSingleton;
import com.example.idnp_sunshield.databinding.FragmentLocationBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Locations#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    public static Locations newInstance(String param1, String param2) {
        Locations fragment = new Locations();
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
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Manejar el clic del botón aquí
                countryLocation = binding.locationText.getText().toString();
                if (TextUtils.isEmpty(countryLocation)) {
                    // Manejar el caso donde el texto está vacío
                    countryLocation = "London"; // O proporciona un valor predeterminado
                }
                System.out.println("countryLocation: " + countryLocation);
                fetchWeather(countryLocation);
            }
        });
        if(TextUtils.isEmpty(binding.locationText.getText().toString())){
            countryLocation = "London";
            fetchWeather(countryLocation);
        }

        return binding.getRoot();
    }

/*    private void data(){
        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_1_2).allowMainThreadQueries().build();

        // Dummy data: byte array representing an image (replace with actual image data)

        System.out.println("DATAAAAAAA");
        // Load the image into a Bitmap

        // Convert the Bitmap to a byte array
        double a = getLatitud();
        double b = getLongitud();

        System.out.println("valor de a: " + a);
        System.out.println("valor de b: " + b);
        System.out.println("valor setter de laitud en data : " + getLatitud());
        System.out.println("valor setter de longitud en data : " + getLongitud());

        // Retrieve all illnesses from the database
        // List to store illnesses retrieved from the database
        List<Location> locationsList;
        locationsList = dataBase.getLocationDAO().getAllLocations();

        //System.out.println(dataBase.getLocationDAO().getLocation(locationsList.get(locationsList.toArray().length -1).getId()).getClass().getName());
        for (int i = 0; i < locationsList.toArray().length; i++) {
            System.out.println("Buscanodo si ya esta registrado el valor");
            System.out.println("valor de getLocation x cosas: " + dataBase.getLocationDAO().getLocation(locationsList.get(i).getId()));
            System.out.println("valores usando Locaitons y getLocal:" + dataBase.getLocationDAO().getLocation(i));
            System.out.println("valor de locationList: " + locationsList.get(i));
            System.out.println("valores de if: " + (dataBase.getLocationDAO().getLocation(i) == locationsList.get(i)));

            if (dataBase.getLocationDAO().getLocation(locationsList.get(i).getId()) != null)
                System.out.println("Ya esta registrado");
            else
                dataBase.getLocationDAO().addLocation(new Location(a, b));
        }
        System.out.println("valores de locations list: " + locationsList.toString());
        // Display the title of the first illness in the TextView2
        System.out.println("Cantidad de valores de locationList antes : " + locationsList.toArray().length);

        for (int i = 0; i < locationsList.toArray().length; i++) {
            System.out.println("valores de locations list: " + locationsList.get(i).getLongitude());
            System.out.println("valores de locations list: " + locationsList.get(i).getLatitude());

        }

        binding.showCountry.setText(locationsList.get(0).getLatitude() + locationsList.get(0).getLongitude() + "");
        for (int i = 0; i < locationsList.toArray().length; i++) {
            dataBase.getLocationDAO().deleteLocation(locationsList.get(i));
        }
        System.out.println("Cantidad de valores de locationList despeus: " + locationsList.toArray().length);

    }*/

    private void data(String countryLocation) {
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).addMigrations(DataBase.MIGRATION_1_2).allowMainThreadQueries().build();

        double latitud = getLatitud();
        double longitud = getLongitud();

        System.out.println("Valores de latitud y longitud: " + latitud + ", " + longitud);
        List<Location> locationsList;
        locationsList = dataBase.getLocationDAO().getAllLocations();

        boolean locationAlreadyRegistered = false;

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

        if (!locationAlreadyRegistered) {
            System.out.println("entre al registro");
            dataBase.getLocationDAO().addLocation(new Location(longitud, latitud, countryLocation));

        }

        locationsList = dataBase.getLocationDAO().getAllLocations();

        System.out.println("Cantidad de valores de locationsList antes: " + locationsList.size());

        for (Location location : locationsList) {
            System.out.println("Valores de locationsList: Country: "+  location.getTitle() + " Latitud=" + location.getLatitude() +
                    ", Longitud=" + location.getLongitude());
        }

        //binding.showCountry.setText(!locationsList.isEmpty() ? (locationsList.get(0).getLatitude() + locationsList.get(0).getLongitude() + "") : "");
        binding.showCountry.setText(locationsList.get(0).getLatitude() + " "+ locationsList.get(0).getLongitude() + " ");

        // Inicia el servicio y envía datos a través de un Intent
        System.out.println("SERVICE latitude: " + locationsList.get(0).getLatitude());
        System.out.println("SERVICE longitude: " + locationsList.get(0).getLongitude());

        LocationSingleton locationSingleton = LocationSingleton.getInstance();
        Location nuevaUbicacion = new Location(locationsList.get(0).getLongitude(), locationsList.get(0).getLatitude(), locationsList.get(0).getTitle());
        locationSingleton.setLocation(nuevaUbicacion);

        for (int i = 0; i < locationsList.toArray().length; i++) {
            dataBase.getLocationDAO().deleteLocation(locationsList.get(i));
        }
        System.out.println("Cantidad de valores de locationsList después: " + locationsList.size());
    }

    public void fetchWeather(String countryLocation) {
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