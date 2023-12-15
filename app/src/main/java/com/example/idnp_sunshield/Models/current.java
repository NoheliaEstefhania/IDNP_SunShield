package com.example.idnp_sunshield.Models;

public class current {

    // Fields representing data related to the current weather
    public double uvi;  // UV Index
    public long dt;      // dateTime

    // Constructor to initialize the object with specific values
    public current(double uvi, long dt) {
        this.uvi = uvi;
        this.dt = dt;
    }

    // Getter method for retrieving the timestamp (dt)
    public long getDt() {
        return dt;
    }

    // Setter method for updating the timestamp (dt)
    public void setDt(long dt) {
        this.dt = dt;
    }

    // Getter method for retrieving the UV Index (uvi)
    public double getUvi() {
        return uvi;
    }

    // Setter method for updating the UV Index (uvi)
    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
}
