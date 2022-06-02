package com.example.safetyapp;

import java.io.Serializable;

public class Coodinate implements Serializable {

    String longitudes,latitudes, country, town;

    public Coodinate(String longitudes, String latitudes, String country, String town) {
        this.longitudes = longitudes;
        this.latitudes = latitudes;
        this.country = country;
        this.town = town;
    }
    public Coodinate() {
        this.longitudes = "longitudes";
        this.latitudes = "latitudes";
        this.country = "country";
        this.town = "town";
    }

    public String getLongitudes() {
        return longitudes;
    }

    public void setLongitudes(String longitudes) {
        this.longitudes = longitudes;
    }

    public String getLatitudes() {
        return latitudes;
    }

    public void setLatitudes(String latitudes) {
        this.latitudes = latitudes;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }
}
