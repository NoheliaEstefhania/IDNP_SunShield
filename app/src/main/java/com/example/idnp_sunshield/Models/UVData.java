package com.example.idnp_sunshield.Models;

import java.util.List;

public class UVData {
    // Fields representing weather data
    private List<weather> weather;               // List of weather conditions
    private main main;                           // Main weather data
    private current current;                     // Current weather data
    private List<daily> daily;                   // List of daily weather data - many days
    private String name;                         // Location name

    // Constructor to initialize the object with specific values
    public UVData(List<weather> weather, main main, String name, current current, List<daily> daily) {
        this.weather = weather;
        this.main = main;
        this.name = name;
        this.current = current;
        this.daily = daily;
    }

    // Getter and Setter methods for each field
    // (Getters are used to retrieve the values, and Setters are used to update the values)

    public List<daily> getDaily() {
        return daily;
    }

    public void setDaily(List<daily> daily) {
        this.daily = daily;
    }

    public current getCurrent() {
        return current;
    }

    public void setCurrent(current current) {
        this.current = current;
    }

    public List<weather> getWeather() {
        return weather;
    }

    public void setWeather(List<weather> weather) {
        this.weather = weather;
    }

    public main getMain() {
        return main;
    }

    public void setMain(main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
