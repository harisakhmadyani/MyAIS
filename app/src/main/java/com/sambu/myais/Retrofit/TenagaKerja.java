package com.sambu.myais.Retrofit;

public class TenagaKerja {
    Integer RegNo;
    String NIK;
    String Nama;
    String KodeWilayah;
    String status;

    public TenagaKerja(Integer regno, String nik, String nama, String kodewilayah,String status) {
        this.RegNo = regno;
        this.NIK = nik;
        this.Nama = nama;
        this.KodeWilayah = kodewilayah;
        this.status = status;
    }

    public Integer getRegNo() {
        return RegNo;
    }

    public String getNIK() {
        return NIK;
    }

    public String getNama() {
        return Nama;
    }

    public String getKodeWilayah() {
        return KodeWilayah;
    }

    public String getStatus() {
        return status;
    }
}
