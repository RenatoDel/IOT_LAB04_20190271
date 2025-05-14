package com.example.lab04_20190271.models;

import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("region")
    private String region;

    @SerializedName("country")
    private String country;

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lon")
    private double longitude;

    @SerializedName("url")
    private String url;

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getUrl() {
        return url;
    }

    // Representación de string para debugging
    @Override
    public String toString() {
        return name + ", " + region + ", " + country;
    }
}