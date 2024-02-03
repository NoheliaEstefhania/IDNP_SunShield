package com.example.idnp_sunshield.SharePreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class LocationPreferences {

    private static final String PREF_NAME = "LocationPreferences";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_TITLE = "title";

    private final SharedPreferences sharedPreferences;

    public LocationPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLocation(double longitude, double latitude, String title) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_LONGITUDE, Double.doubleToRawLongBits(longitude));
        editor.putLong(KEY_LATITUDE, Double.doubleToRawLongBits(latitude));
        editor.putString(KEY_TITLE, title);
        editor.apply();
    }

    public double getLongitude() {
        return Double.longBitsToDouble(sharedPreferences.getLong(KEY_LONGITUDE, 0));
    }

    public double getLatitude() {
        return Double.longBitsToDouble(sharedPreferences.getLong(KEY_LATITUDE, 0));
    }

    public String getTitle() {
        return sharedPreferences.getString(KEY_TITLE, "");
    }

    public boolean hasLocationData() {
        // Verificar si hay datos en SharedPreferences, por ejemplo, si existe la clave "latitude"
        return sharedPreferences.contains(KEY_LATITUDE);
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}

