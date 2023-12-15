package com.example.idnp_sunshield.Entity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface IllnessDAO {
    @Insert
    public void addIllness(Illness illness);
    @Update
    public void updateIllness(Illness illness);
    @Delete
    public void deleteIllness(Illness illness);
    @Query("SELECT * FROM ILLNESS")
    public List<Illness> getAllIllnesses();
    @Query("SELECT * FROM ILLNESS WHERE illness_id == :illness_id")
    public Illness getIllness(int illness_id);
}
