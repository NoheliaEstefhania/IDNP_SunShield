package com.example.idnp_sunshield.Services;

// En tu servicio BackgroundService

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.idnp_sunshield.Activities.MainActivity;
import com.example.idnp_sunshield.Interfaces.InterfaceApi;
import com.example.idnp_sunshield.Models.UVData;
import com.example.idnp_sunshield.Models.current;
import com.example.idnp_sunshield.Models.main;
import com.example.idnp_sunshield.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
public class BackgroundService extends Service {

    private Handler handler;
    //private final int delay = 60000; // 1 minuto (ajusta según sea necesario)
    private final int delay = 15000; // 1 minuto (ajusta según sea necesario)

    private double lastUviValue = 0;
    private int notificationCount = 0; // Nuevo contador

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForegroundService();

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtener el valor actual del índice UV
                fetchUviValue();

                // Realizar la lógica de servicio según el valor actualizado
                if (lastUviValue > 0.1) {
                    notificationCount++; // Incrementa el contador

                    showNotification("¡Alerta de UV!", "El índice UV Protégete del sol. " + lastUviValue + " - Noti: " + notificationCount);
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Nombre del canal",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startForegroundService() {
        Notification notification = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Tu servicio está en segundo plano")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(1, notification);
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

    /*private void showNotification(String title, String message) {
        Notification notification = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }*/
    private void showNotification(String title, String message) {
        Intent launchAppIntent = new Intent(this, MainActivity.class);  // Reemplaza con la actividad principal de tu aplicación
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action launchAppAction = new NotificationCompat.Action(
                R.drawable.ic_launcher_foreground,
                "Abrir App",
                pendingIntent
        );

        // Agrega una imagen a la notificación (reemplaza "R.drawable.notification_image" con tu propia imagen)
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.img_disease01);

        Notification notification = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeIcon)  // Agrega la imagen grande a la notificación
                .addAction(launchAppAction)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
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
