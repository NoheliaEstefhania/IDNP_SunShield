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

    // Notification channel ID for the foreground service
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private BroadcastReceiver uviUpdateReceiver;
    private NotificationManager notificationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Create notification channel
        createNotificationChannel();
        System.out.println("Notificacion EMPEZANDO");
        // Create an intent to open MainActivity when notification is clicked
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        Notification notification = buildNotification("My Foreground Service", "Content Text", pendingIntent);

        // Start the service in the foreground with the notification
        startForeground(1, notification);

        // Register the BroadcastReceiver to receive updates from the background service
        uviUpdateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double uviValue = intent.getDoubleExtra("UVI_VALUE", 0);

                // Update the notification with the new UV index value
                updateNotification(uviValue);
            }
        };
        IntentFilter intentFilter = new IntentFilter(BackgroundService.ACTION_UVI_UPDATE);
        registerReceiver(uviUpdateReceiver, intentFilter);
        // Return START_NOT_STICKY to ensure the service is not restarted automatically
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Method to create the notification channel for the foreground service
    private void createNotificationChannel() {
        System.out.println("METODO DE createNotificationChannel");

        notificationManager = getSystemService(NotificationManager.class);

        // Check if notification manager is available and create the notification channel
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

    // Method to build the notification
    private Notification buildNotification(String title, String contentText, PendingIntent pendingIntent) {
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.img_logo);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.img_disease01)
                .setLargeIcon(largeIcon)
                .setContentIntent(pendingIntent);

        return builder.build();
    }

    // Method to update the notification with the new UV index value
    private void updateNotification(double uviValue) {
        // Actualiza la notificación con el nuevo valor numérico
        Notification notification = buildNotification("My Foreground Service", "Valor actualizado: " + uviValue, null);
        notificationManager.notify(1, notification);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver when the service is destroyed
        unregisterReceiver(uviUpdateReceiver);
    }
}

