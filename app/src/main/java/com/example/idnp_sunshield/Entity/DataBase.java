package com.example.idnp_sunshield.Entity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Location.class, Illness.class},
        version = 1
)
public abstract class DataBase extends RoomDatabase {
    public abstract LocationDAO getLocationDAO();
    public abstract IllnessDAO getIllnessDAO();
}