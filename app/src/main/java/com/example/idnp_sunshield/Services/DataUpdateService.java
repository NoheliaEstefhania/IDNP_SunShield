package com.example.idnp_sunshield.Services;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DataUpdateService extends IntentService {

    public DataUpdateService() {
        super("DataUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            // Obtén datos del intent (puedes enviar datos extras según sea necesario)
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);

            // Realiza cualquier procesamiento necesario con los datos

            // Envía datos actualizados a través de un Intent local
            Intent dataIntent = new Intent("DataUpdate");
            dataIntent.putExtra("latitude", latitude);
            dataIntent.putExtra("longitude", longitude);
            LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
        }
    }
}

