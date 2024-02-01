package com.example.idnp_sunshield.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

public class DataUpdateReceiver extends BroadcastReceiver {

    private DataUpdateListener listener;

    public DataUpdateReceiver(DataUpdateListener listener) {
        this.listener = listener;
    }

/*    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals("DataUpdate")) {
            // Obtén datos actualizados del Intent y notifica al listener
            double latitude = intent.getDoubleExtra("latitude", 0);
            double longitude = intent.getDoubleExtra("longitude", 0);
            listener.onDataUpdate(latitude, longitude);
        }

    }*/

    @Override
    public void onReceive(Context context, Intent intent) {
        double latitude = intent.getDoubleExtra("latitude", 0.0);
        double longitude = intent.getDoubleExtra("longitude", 0.0);

        if (listener != null) {
            listener.onDataUpdate(latitude, longitude);
        }
    }

    public interface DataUpdateListener {
        void onDataUpdate(double latitude, double longitude);
    }
}

