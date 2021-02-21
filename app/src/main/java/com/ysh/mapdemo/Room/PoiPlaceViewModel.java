package com.ysh.mapdemo.Room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;

import java.util.List;

public class PoiPlaceViewModel extends AndroidViewModel {
    private PoiPlaceDao poiPlaceDao;
    private PoiPlaceRepository poiPlaceRepository;
    public PoiPlaceViewModel(@NonNull Application application) {
        super(application);
        poiPlaceRepository = new PoiPlaceRepository(application);

    }

    public LiveData<List<PoiPlace>> getAllPoiPlaceLive() {
        return poiPlaceRepository.getAllPoiPlacesLive();
    }

    public void insertPoiPlaces(PoiPlace...poiPlaces){
        poiPlaceRepository.insertPoiPlaces(poiPlaces);
    }
    public void updatePoiPlaces(PoiPlace...poiPlaces){
        poiPlaceRepository.updatePoiPlaces(poiPlaces);
    }
    public void deletePoiPlaces(PoiPlace...poiPlaces){
        poiPlaceRepository.deletePoiPlaces(poiPlaces);
    }
    public void DeleteALLPoiPlaces(){
        poiPlaceRepository.DeleteALLPoiPlaces();
    }

}
