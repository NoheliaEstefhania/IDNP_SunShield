package com.example.idnp_sunshield.Models;

public class main {

    // Fields representing main weather data
    public double temp;         // Current temperature
    public double feels_like;   // "Feels like" temperature
    public double temp_max;     // Maximum temperature
    public double temp_min;     // Minimum temperature
    public int pressure;        // Atmospheric pressure
    public int humidity;        // Humidity percentage

    // Additional field for UV Index
    public double uvi;          // UV Index

    // Constructor to initialize the object with specific values
    public main(double temp, double feels_like, double temp_max, double temp_min, int pressure, int humidity, double uvi) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.pressure = pressure;
        this.humidity = humidity;
        this.uvi = uvi;
    }

    // Getter and Setter methods for each field
    // (Getters are used to retrieve the values, and Setters are used to update the values)

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double uvi) {
        this.uvi = uvi;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
