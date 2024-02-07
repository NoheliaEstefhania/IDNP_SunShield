package com.example.idnp_sunshield.Services;

// En tu servicio BackgroundService

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.SharePreferences.LocationPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class BackgroundService extends Service {

    private final IBinder binder = new LocalBinder();
    private Handler handler;
    private final int delay = 15000; // 15 seg de delay
    private double lastUviValue = 0;
    public static final String ACTION_UVI_UPDATE = "com.example.idnp_sunshield.ACTION_UVI_UPDATE";
    private Context mContext;

    public class LocalBinder extends Binder {
        public BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize handler to fetch UV data periodically
        handler = new Handler();
        mContext = getApplicationContext();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtener el valor actual del índice UV
                fetchUviValue();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    // Method to send UV index update broadcast
    private void sendUviUpdateBroadcast() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UVI_UPDATE);
        intent.putExtra("UVI_VALUE", lastUviValue);

        // Send the broadcast
        sendBroadcast(intent);
    }

    // Method to fetch UV index value from the API
    private void fetchUviValue() {
        // Create a Retrofit instance with the base URL and GsonConverterFactory
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the InterfaceApi using the Retrofit instance
        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);
        LocationPreferences locationPreferences = new LocationPreferences(mContext);
        System.out.println("SHAREPREFERENTS SERVICE ubicacion: " + locationPreferences.getTitle());
        // Make an asynchronous call to get UV data from OpenWeatherMap API
        System.out.println("SHAREPREFERENTS SERVICE ubicacion: " + locationPreferences.getLatitude());
        System.out.println("SHAREPREFERENTS SERVICE ubicacion: " + locationPreferences.getLongitude());

        // Make an asynchronous call to get UV data from OpenWeatherMap API
        Call<UVData> call = interfaceApi.getData(locationPreferences.getLatitude(), locationPreferences.getLongitude(), "hourly,daily", "c71298943776351e81c2f4e84456a36d");
        //Call<UVData> call = interfaceApi.getData(-16.39889, -71.535, "hourly,daily", "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<UVData>() {
            @Override
            public void onResponse(Call<UVData> call, Response<UVData> response) {
                if (response.isSuccessful()) {
                    UVData uvData = response.body();
                    current tc = uvData.getCurrent();

                    // Send UV index update broadcast
                    sendUviUpdateBroadcast();
                    // Update lastUviValue with the fetched value
                    lastUviValue = tc.getUvi();
                }
            }

            @Override
            public void onFailure(Call<UVData> call, Throwable t) {
                // Manejar el escenario de falla
            }
        });
    }

    @Override
    public void onDestroy() {
        // Stop the service and handler when the application is closed
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }


}

