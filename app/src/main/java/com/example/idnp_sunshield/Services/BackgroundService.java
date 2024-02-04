package com.example.idnp_sunshield.Services;

// En tu servicio BackgroundService

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.current;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class BackgroundService extends Service {

    private final IBinder binder = new LocalBinder();
    private Handler handler;
    private final int delay = 15000; // 1 minuto (ajusta según sea necesario)
    private double lastUviValue = 0;
    private int notificationCount = 0; // Nuevo contador
    public static final String ACTION_UVI_UPDATE = "com.example.idnp_sunshield.ACTION_UVI_UPDATE";


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

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtener el valor actual del índice UV
                fetchUviValue();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void sendUviUpdateBroadcast() {
        Intent intent = new Intent();
        intent.setAction(ACTION_UVI_UPDATE);
        intent.putExtra("UVI_VALUE", lastUviValue);

        // Envía la transmisión (Broadcast)
        sendBroadcast(intent);
    }

    private void fetchUviValue() {
        // Realiza la llamada a la API para obtener el valor actual del índice UV
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfaceApi interfaceApi = retrofit.create(InterfaceApi.class);

        Call<UVData> call = interfaceApi.getData(-16.39889, -71.535, "hourly,daily", "c71298943776351e81c2f4e84456a36d");
        call.enqueue(new Callback<UVData>() {
            @Override
            public void onResponse(Call<UVData> call, Response<UVData> response) {
                if (response.isSuccessful()) {
                    UVData uvData = response.body();
                    current tc = uvData.getCurrent();

                    sendUviUpdateBroadcast();
                    // Actualizar lastUviValue con el valor obtenido
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
        // Detén el servicio y el handler cuando la aplicación se cierra
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }


}

