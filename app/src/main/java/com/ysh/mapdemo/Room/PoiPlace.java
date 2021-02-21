package com.ysh.mapdemo.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.amap.api.maps.model.LatLng;

@Entity
public class PoiPlace {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String address;
    private double Lat;
    private double lon;
    private boolean latInvisible;

    public PoiPlace(String title, String address, double lat, double lon) {
        this.title = title;
        this.address = address;
        Lat = lat;
        this.lon = lon;
    }

    public PoiPlace() {

    }

    public boolean isLatInvisible() {
        return latInvisible;
    }

    public void setLatInvisible(boolean latInvisible) {
        this.latInvisible = latInvisible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
