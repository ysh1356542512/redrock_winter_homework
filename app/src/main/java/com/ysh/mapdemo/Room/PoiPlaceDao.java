package com.ysh.mapdemo.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PoiPlaceDao {
    @Insert
    void insertPoiPlace(PoiPlace...poiPlaces);
    @Update
    void updatePoiPlace(PoiPlace...poiPlaces);
    @Delete
    void deletePoiPlace(PoiPlace...poiPlaces);
    @Query("DELETE FROM POIPLACE")
    void deleteAllWords();
    @Query("SELECT * FROM POIPLACE ORDER BY ID DESC")
    LiveData<List<PoiPlace>> getAllPoiPlaceLive();
}
