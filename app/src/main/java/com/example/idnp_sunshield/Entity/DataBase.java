package com.example.idnp_sunshield.Entity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// Define a Room database
@Database(
        entities = {Location.class, Illness.class}, // Specify the entities (tables) in the database
        version = 1 // Set the version number of the database schema
)
public abstract class DataBase extends RoomDatabase {
    // Declare abstract methods to provide DAO (Data Access Object) instances
    public abstract LocationDAO getLocationDAO(); // Get the DAO for Location entity
    public abstract IllnessDAO getIllnessDAO(); // Get the DAO for Illness entity
}
