package com.example.idnp_sunshield.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.idnp_sunshield.Entity.DataBase;
import com.example.idnp_sunshield.Entity.Illness;
import com.example.idnp_sunshield.Entity.Location;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.lat;
import com.example.idnp_sunshield.Models.lon;
import com.example.idnp_sunshield.R;
import com.example.idnp_sunshield.databinding.FragmentLocationBinding;

import java.io.ByteArrayOutputStream;
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
    boolean fetchLati = false;
    boolean fetchLont = false;

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
                fetchWeather2(countryLocation);
            }
        });
/*        if(TextUtils.isEmpty(binding.locationText.getText().toString())){
            fetchWeather("London");
            fetchWeather2("London");
        }*/

        return binding.getRoot();
    }

    private void data(){
        // List to store illnesses retrieved from the database
        List<Location> locationsList;

        // Create or open the Room database
        DataBase dataBase = Room.databaseBuilder(
                getActivity().getApplicationContext(),
                DataBase.class,
                "dbPruebas"
        ).allowMainThreadQueries().build();

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

        // Add an illness with the image to the database
        dataBase.getLocationDAO().addLocation(new Location(a, b));

        // Retrieve all illnesses from the database
        locationsList = dataBase.getLocationDAO().getAllLocations();

        System.out.println("valores de locations list: " + locationsList.toString());
        // Display the title of the first illness in the TextView2
        System.out.println("Cantidad de valores de locationList antes : " + locationsList.toArray().length);

        for (int i = 0; i < locationsList.toArray().length; i++) {
            System.out.println("valores de locations list: " + locationsList.get(i).getLongitude());
            System.out.println("valores de locations list: " + locationsList.get(i).getLatitude());

        }
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
        binding.showCountry.setText(locationsList.get(0).getLatitude() + locationsList.get(0).getLongitude() + "");
        for (int i = 0; i < locationsList.toArray().length; i++) {
            dataBase.getLocationDAO().deleteLocation(locationsList.get(i));
        }
        System.out.println("Cantidad de valores de locationList despeus: " + locationsList.toArray().length);

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
        //Call<List<CountryData>> call = interfaceApi.getCountry( "London",1, "c71298943776351e81c2f4e84456a36d");
        Call<List<lat>> call = interfaceApi.getLat( countryLocation,1, "c71298943776351e81c2f4e84456a36d");

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


                    setLatitud(lati.get(0).getLat());
                    System.out.println("valor setter de laitud: " + getLatitud());
                    fetchLati = true;
                    tryExecute();
                    // Extract main and current data from UVData
                    //main to = country.getMain();
                    // Update UI components with UV index value
                    //binding.showCountry.setText("conexion");
                    //binding.showCountry.setText(String.valueOf(tcLan));

                }
            }

            @Override
            public void onFailure(Call<List<lat>> call, Throwable t) {
                binding.showCountry.setText("no answer");
            }
        });
    }

    public void fetchWeather2(String countryLocation) {
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
        Call<List<lon>> call = interfaceApi.getLon( countryLocation,1, "c71298943776351e81c2f4e84456a36d");

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

                    setLongitud(loni.get(0).getLon());
                    System.out.println("valor setter de longitud: " + getLongitud());

                    fetchLont = true;
                    tryExecute();
                    // Extract main and current data from UVData
                    //main to = country.getMain();
                    // Update UI components with UV index value
                    //binding.showCountry.setText("conexion");
                    //binding.showCountry.setText(String.valueOf(tcLan));

                }
            }

            @Override
            public void onFailure(Call<List<lon>> call, Throwable t) {
                binding.showCountry.setText("no answer");
            }
        });

    }

    private void tryExecute(){
        if(fetchLati & fetchLont)
            data();
    }
}