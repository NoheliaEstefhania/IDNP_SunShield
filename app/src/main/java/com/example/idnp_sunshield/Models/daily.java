package com.example.idnp_sunshield.Models;

public class daily {

    public double uvi;
    public long dt;

    public daily(double uvi, long dt) {
        this.uvi = uvi;
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public double getUvi() {
        return uvi;
    }

    public void setUvi(double uvi) {
        this.uvi = uvi;
    }
}
