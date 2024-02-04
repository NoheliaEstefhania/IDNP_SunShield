package com.example.idnp_sunshield.Services;

// ForegroundService.java
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.idnp_sunshield.Activities.MainActivity;
import com.example.idnp_sunshield.R;

public class ForegroundService extends Service {
    /*private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private BroadcastReceiver uviUpdateReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.img_disease01)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        // Registra el BroadcastReceiver para recibir actualizaciones del servicio en segundo plano
        uviUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double uviValue = intent.getDoubleExtra("UVI_VALUE", 0);

                // Actualiza la notificación con el nuevo valor numérico
                updateNotification(uviValue);
            }
        };
        IntentFilter intentFilter = new IntentFilter(BackgroundService.ACTION_UVI_UPDATE);
        registerReceiver(uviUpdateReceiver, intentFilter);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    private void updateNotification(double uviValue) {
        // Actualiza la notificación con el nuevo valor numérico
        // ...
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Asegúrate de desregistrar el BroadcastReceiver cuando el servicio se destruya
        //unregisterReceiver(uviValueReceiver);
    }*/

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private BroadcastReceiver uviUpdateReceiver;
    private NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setSmallIcon(R.drawable.img_disease01)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        // Registra el BroadcastReceiver para recibir actualizaciones del servicio en segundo plano
        uviUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double uviValue = intent.getDoubleExtra("UVI_VALUE", 0);

                // Actualiza la notificación con el nuevo valor numérico
                updateNotification(uviValue);
            }
        };
        IntentFilter intentFilter = new IntentFilter(BackgroundService.ACTION_UVI_UPDATE);
        registerReceiver(uviUpdateReceiver, intentFilter);

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    private void updateNotification(double uviValue) {
        // Actualiza la notificación con el nuevo valor numérico
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setContentText("Valor actualizado: " + uviValue)
                .setSmallIcon(R.drawable.img_disease01)
                .build();

        notificationManager.notify(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Asegúrate de desregistrar el BroadcastReceiver cuando el servicio se destruya
        unregisterReceiver(uviUpdateReceiver);
    }
}

