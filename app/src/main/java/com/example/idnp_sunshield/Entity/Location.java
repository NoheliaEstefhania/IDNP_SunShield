package com.example.idnp_sunshield.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "Location")
public class Location {
    @ColumnInfo(name = "location_id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "longitude")
    private double longitude;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "state")
    private boolean state;

    public Location(double longitude, double latitude, String title, boolean state){
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.state = state;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public double getLongitude(){
        return this.longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public String getTitle(){
        return this.title;
    }
    public boolean getState(){
        return this.state;
    }

}
