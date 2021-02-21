package com.ysh.mapdemo.Room;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PoiPlaceRepository {
    private LiveData<List<PoiPlace>> allPoiPlacesLive;
    private PoiPlaceDao poiPlaceDao;
    public PoiPlaceRepository(Context context){
        PoiPlaceDatabase poiPlaceDatabase = PoiPlaceDatabase.getDatabase(context.getApplicationContext());
        poiPlaceDao = poiPlaceDatabase.getPoiPlaceDao();
        allPoiPlacesLive = poiPlaceDao.getAllPoiPlaceLive();
    }

    public LiveData<List<PoiPlace>> getAllPoiPlacesLive() {
        return allPoiPlacesLive;
    }
    static class InsertAsyncTask extends AsyncTask<PoiPlace,Void,Void> {
        private PoiPlaceDao poiPlaceDao;
        public InsertAsyncTask(PoiPlaceDao poiPlaceDao){
            this.poiPlaceDao = poiPlaceDao;
        }

        @Override
        protected Void doInBackground(PoiPlace... poiPlaces) {
            poiPlaceDao.insertPoiPlace(poiPlaces);
            return null;
        }
        //任务完成时将结果带给主线程
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
        //当进度发生更新时会呼叫
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        //在后台执行之前会呼叫
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
    static class UpdateAsyncTask extends AsyncTask<PoiPlace,Void,Void> {
        private PoiPlaceDao poiPlaceDao;

        public UpdateAsyncTask(PoiPlaceDao poiPlaceDao) {
            this.poiPlaceDao = poiPlaceDao;
        }
        @Override
        protected Void doInBackground(PoiPlace... poiPlaces) {
            poiPlaceDao.updatePoiPlace(poiPlaces);
            return null;
        }
    }
    static class DeleteAsyncTask extends AsyncTask<PoiPlace,Void,Void> {
        private PoiPlaceDao poiPlaceDao;

        public DeleteAsyncTask(PoiPlaceDao poiPlaceDao) {
            this.poiPlaceDao = poiPlaceDao;
        }
        @Override
        protected Void doInBackground(PoiPlace... poiPlaces) {
            poiPlaceDao.deletePoiPlace(poiPlaces);
            return null;
        }
    }
    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private PoiPlaceDao poiPlaceDao;

        public DeleteAllAsyncTask(PoiPlaceDao poiPlaceDao) {
            this.poiPlaceDao = poiPlaceDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            poiPlaceDao.deleteAllWords();
            return null;
        }
    }
    public void insertPoiPlaces(PoiPlace...poiPlaces){
        new InsertAsyncTask(poiPlaceDao).execute(poiPlaces);
    }
    public void updatePoiPlaces(PoiPlace...poiPlaces){
        new UpdateAsyncTask(poiPlaceDao).execute(poiPlaces);
    }
    public void deletePoiPlaces(PoiPlace...poiPlaces){
        new DeleteAsyncTask(poiPlaceDao).execute(poiPlaces);
    }
    public void DeleteALLPoiPlaces(){
        new DeleteAllAsyncTask(poiPlaceDao).execute();
    }
}
