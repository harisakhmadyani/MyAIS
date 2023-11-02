package com.sambu.myais.Retrofit;

public class Pancang {
    String IDPancang;
    Double Latitude;
    Double Longitude;
    String Pancang;
    Integer Toleransi;

    public Pancang(String IDPancang2, String pancang, Double latitude, Double longitude, Integer toleransi) {
        this.IDPancang = IDPancang2;
        this.Pancang = pancang;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Toleransi = toleransi;
    }

    public String getIDPancang() {
        return this.IDPancang;
    }

    public String getPancang() {
        return this.Pancang;
    }

    public Double getLatitude() {
        return this.Latitude;
    }

    public Double getLongitude() {
        return this.Longitude;
    }

    public Integer getToleransi() {
        return this.Toleransi;
    }
}
