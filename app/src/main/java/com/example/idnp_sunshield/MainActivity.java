package com.example.idnp_sunshield;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.idnp_sunshield.Models.MausanData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.Models.weather;

import com.example.idnp_sunshield.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");

        String currentdate = format.format(new Date());
        //binding.date.setText(currentdate);
        fetchWeather("Delhi");
    }

    void fetchWeather (String cityname){
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
                    //binding.maxTempValue.setText("si rpta");
                    binding.maxTempValue.setText(String.valueOf(tc.getUvi()));

                    /*binding.mainTempValue.setText(String.valueOf(to.getTemp()) +"\\u2103");
                    binding.maxTempValue.setText(String.valueOf(to.getTemp_max()));
                    binding.minTempValue.setText(String.valueOf(to.getTemp_min()));

                    binding.pressureValue.setText(String.valueOf(to.getPressure()));
                    binding.cityname.setText(mausanData.getName());
                    /*
                    List<weather> description = mausanData.getWeather();

                    for (weather data: description) {
                        binding.description.setText(data.getDescription());
                    }
                    */
                }
            }

            @Override
            public void onFailure(Call<MausanData> call, Throwable t) {
                binding.maxTempValue.setText("no rpta");
            }
        });
    }
}