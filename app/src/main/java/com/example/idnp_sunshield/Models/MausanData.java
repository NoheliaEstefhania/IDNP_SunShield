package com.example.idnp_sunshield.Models;

import java.util.List;

public class MausanData {

    private List<weather> weather;
    private main main;
    private current current;

    private List<daily> daily;
    private String name;

    public MausanData(List<com.example.idnp_sunshield.Models.weather> weather, com.example.idnp_sunshield.Models.main main, String name,
                      com.example.idnp_sunshield.Models.current current,List<com.example.idnp_sunshield.Models.daily> daily ) {
        this.weather = weather;
        this.main = main;
        this.name = name;
        this.current = current;
        this.daily = daily;
    }

    public List<com.example.idnp_sunshield.Models.daily> getDaily() {
        return daily;
    }

    public void setDaily(List<com.example.idnp_sunshield.Models.daily> daily) {
        this.daily = daily;
    }

    public com.example.idnp_sunshield.Models.current getCurrent() {
        return current;
    }

    public void setCurrent(com.example.idnp_sunshield.Models.current current) {
        this.current = current;
    }

    public List<com.example.idnp_sunshield.Models.weather> getWeather() {
        return weather;
    }

    public void setWeather(List<com.example.idnp_sunshield.Models.weather> weather) {
        this.weather = weather;
    }

    public com.example.idnp_sunshield.Models.main getMain() {
        return main;
    }

    public void setMain(com.example.idnp_sunshield.Models.main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
