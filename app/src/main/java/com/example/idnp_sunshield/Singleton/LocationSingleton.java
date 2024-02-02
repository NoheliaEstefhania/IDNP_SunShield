package com.example.idnp_sunshield.Singleton;

import com.example.idnp_sunshield.Entity.Location;

public class LocationSingleton {
    private static LocationSingleton instance;

    // Atributos de la ubicación que desees almacenar
    private Location location;

    private LocationSingleton() {
        // Constructor privado para evitar la creación de instancias directas
    }

    public static LocationSingleton getInstance() {
        if (instance == null) {
            instance = new LocationSingleton();
        }
        return instance;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

