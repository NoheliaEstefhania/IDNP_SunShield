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
    public Location(double longitude, double latitude, String title){
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
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
    public double getLongitude(){
        return this.longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
    public String getTitle(){
        return this.title;
    }
}
