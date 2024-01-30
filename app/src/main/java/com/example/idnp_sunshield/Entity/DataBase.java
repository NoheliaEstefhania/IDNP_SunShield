package com.example.idnp_sunshield.Entity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

// Define a Room database
@Database(
        entities = {Location.class, Illness.class}, // Specify the entities (tables) in the database
        version = 2 // Set the version number of the database schema
)
public abstract class DataBase extends RoomDatabase {
    // Declare abstract methods to provide DAO (Data Access Object) instances
    public abstract LocationDAO getLocationDAO(); // Get the DAO for Location entity
    public abstract IllnessDAO getIllnessDAO(); // Get the DAO for Illness entity

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Puedes ejecutar las sentencias SQL necesarias para modificar la estructura de la base de datos
            // En este ejemplo, agregamos una nueva columna llamada 'title' a la tabla 'Location'
            database.execSQL("ALTER TABLE Location ADD COLUMN title TEXT");
        }
    };
}
