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

    public Location(double longitude, double latitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude(){
        return this.longitude;
    }
    public double getLatitude(){
        return this.latitude;
    }
}
