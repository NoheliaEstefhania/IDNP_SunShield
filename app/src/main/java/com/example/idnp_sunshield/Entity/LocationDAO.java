package com.example.idnp_sunshield.Entity;
import java.util.List;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDAO {
    @Insert
    public void addLocation(Location location);
    @Update
    public void updateLocation(Location location);
    @Delete
    public void deleteLocation(Location location);
    @Query("SELECT * FROM LOCATION")
    public List<Location> getAllLocations();
    @Query("SELECT * FROM LOCATION WHERE location_id == :location_id")
    public Location getLocation(int location_id);
}
