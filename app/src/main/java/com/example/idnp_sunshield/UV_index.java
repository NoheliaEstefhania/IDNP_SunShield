package com.example.idnp_sunshield;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.idnp_sunshield.Models.MausanData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.databinding.ActivityMainBinding;
import com.example.idnp_sunshield.databinding.FragmentUVIndexBinding;

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
    public static UV_index newInstance(String param1, String param2) {
        UV_index fragment = new UV_index();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentUVIndexBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        fetchWeather();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUVIndexBinding.inflate(inflater, container, false);
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

        Call<MausanData> call = interfaceApi.getData(-16.39889,-71.535,"hourly,daily","c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<MausanData>() {
            @Override
            public void onResponse(Call<MausanData> call, Response<MausanData> response) {
                if (response.isSuccessful()){
                    MausanData mausanData = response.body();
                    main to = mausanData.getMain();
                    current tc = mausanData.getCurrent();
                    System.out.println("Estoy en la respuesta");
                    binding.uvTitle.setText("si rpta");
                    //binding.uvIndexNumber.setText(String.valueOf(tc.getUvi()));
                    binding.uvIndexNumber.setText(String.valueOf(tc.getUvi()));

                    /*binding.mainTempValue.setText(String.valueOf(to.getTemp()) +"\\u2103");
                    binding.maxTempValue.setText(String.valueOf(to.getTemp_max()));
                    binding.minTempValue.setText(String.valueOf(to.getTemp_min()));

                    binding.pressureValue.setText(String.valueOf(to.getPressure()));
                    binding.cityname.setText(mausanData.getName());
                    /*
                    List<weather> description = mausanData.getWeather();

                    for (weather data: description) {
                        binding.description.setText(data.getDescription());
                    }*/

                }
            }

            @Override
            public void onFailure(Call<MausanData> call, Throwable t) {
                binding.uvIndexNumber.setText("no rpta");
            }
        });
    }
}