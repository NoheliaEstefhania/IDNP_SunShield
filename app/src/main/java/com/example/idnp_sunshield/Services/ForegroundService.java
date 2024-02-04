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

    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private BroadcastReceiver uviUpdateReceiver;
    private NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        System.out.println("Notificacion EMPEZANDO");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        /*Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setSmallIcon(R.drawable.img_disease01)
                .setContentIntent(pendingIntent)
                .build();*/
        Notification notification = buildNotification("My Foreground Service", "Content Text", pendingIntent);

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
        System.out.println("METODO DE createNotificationChannel");

        notificationManager = getSystemService(NotificationManager.class);

        if (notificationManager == null) {
            System.out.println("Notification Manager es nulo por alguna razón");
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "ForegroundServiceChannel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(serviceChannel);
                System.out.println("Creacion NOtificaion excitosa");
            } else {
                System.out.println("NOtificaion nula por X razon");
            }
        }
        else {
            System.out.println("VERSION NO COMPATIBLE");
        }
    }
    private Notification buildNotification(String title, String contentText, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.img_disease01)
                .setContentIntent(pendingIntent);

        return builder.build();
    }
    /*private void updateNotification(double uviValue) {
        // Actualiza la notificación con el nuevo valor numérico
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Foreground Service")
                .setContentText("Valor actualizado: " + uviValue)
                .setSmallIcon(R.drawable.img_disease01)
                .build();

        notificationManager.notify(1, notification);
    }*/
    private void updateNotification(double uviValue) {
        // Actualiza la notificación con el nuevo valor numérico
        Notification notification = buildNotification("My Foreground Service", "Valor actualizado: " + uviValue, null);
        notificationManager.notify(1, notification);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Asegúrate de desregistrar el BroadcastReceiver cuando el servicio se destruya
        unregisterReceiver(uviUpdateReceiver);
    }
}

