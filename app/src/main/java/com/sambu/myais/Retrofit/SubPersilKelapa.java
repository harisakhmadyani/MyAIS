package com.sambu.myais.Retrofit;

public class SubPersilKelapa {
    String KodeSubPersil;
    Double Latitude;
    Double Longitude;
    Integer Toleransi;
    Integer Active;
    Integer LokasiAbsensiMasuk;
    String status ;
    Integer ActiveLokasiMasuk;

    public SubPersilKelapa(String kodesubpersil, Double latitude, Double longitude, Integer toleransi, Integer active, Integer lokasiabsensimasuk, String status, Integer activelokasimasuk) {
        this.KodeSubPersil = kodesubpersil;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Toleransi = toleransi;
        this.Active = active;
        this.LokasiAbsensiMasuk = lokasiabsensimasuk;
        this.status = status;
        this.ActiveLokasiMasuk = activelokasimasuk;
    }

    public String getKodeSubPersil() {
        return KodeSubPersil;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Integer getLokasiAbsensiMasuk() {
        return LokasiAbsensiMasuk;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public Integer getToleransi() {
        return Toleransi;
    }

    public String getStatus() {
        return status;
    }

    public Integer getActive() {
        return Active;
    }
    
    public Integer getActiveLokasiMasuk() {
        return ActiveLokasiMasuk;
    }
}
