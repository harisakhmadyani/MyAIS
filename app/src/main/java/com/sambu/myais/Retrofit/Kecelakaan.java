package com.sambu.myais.Retrofit;

public class Kecelakaan {
    String Nama;
    String NIK;
    String KodeWilayahTK;
    String Waktu;
    String KodeSubPersil;
    String TipeKecelakaan;
    String Kronologi;
    String TanggalInd;
    Integer StatusVerification;
    String response;

    public Kecelakaan(String nama, String nik, String kodewilayahtk, String waktu, String kodesubpersil, String tipekecelakaan, String kronologi, String tanggalind, Integer statusVerification, String response) {
        this.Nama = nama;
        this.NIK = nik;
        this.KodeWilayahTK = kodewilayahtk;
        this.Waktu = waktu;
        this.KodeSubPersil = kodesubpersil;
        this.TipeKecelakaan = tipekecelakaan;
        this.Kronologi = kronologi;
        this.TanggalInd = tanggalind;
        this.StatusVerification = statusVerification;
        this.response = response;
    }

    public String getNama() {
        return Nama;
    }
    public String getNIK() { return NIK; }
    public String getKodeWilayahTK() {
        return KodeWilayahTK;
    }
    public String getWaktu() {
        return Waktu;
    }
    public String getKodeSubPersil() {
        return KodeSubPersil;
    }
    public String getTipeKecelakaan() {
        return TipeKecelakaan;
    }
    public String getKronologi() {
        return Kronologi;
    }
    public String getTanggalInd() { return TanggalInd;}
    public Integer getStatusVerification() { return StatusVerification;}
    public String getResponse() {
        return response;
    }
}
