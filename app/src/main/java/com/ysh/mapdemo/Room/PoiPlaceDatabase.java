package com.ysh.mapdemo.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {PoiPlace.class},version = 3,exportSchema = false)
public abstract class PoiPlaceDatabase extends RoomDatabase{
    private static PoiPlaceDatabase INSTANCE;
    //synchronized 如果有多个客户端同时申请这个INSTANCE 就不会同时生成INSTANCE 产生排队机制
    static synchronized PoiPlaceDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),PoiPlaceDatabase.class,"poiPlace_database")
                    .build();
        }
        return INSTANCE;
    }
    public abstract PoiPlaceDao getPoiPlaceDao();

}
