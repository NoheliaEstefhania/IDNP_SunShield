package com.example.idnp_sunshield.Models;

public class country {

    public double lat;
    public double lot;

    public country(double lat, double lot) {
        this.lat = lat;
        this.lot = lot;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLot() {
        return lot;
    }

    public void setLot(double lot) {
        this.lot = lot;
    }
}
