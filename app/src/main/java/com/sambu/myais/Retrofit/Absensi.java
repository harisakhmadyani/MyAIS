package com.sambu.myais.Retrofit;

public class Absensi {
//    String Jabatan;
    String Tanggal;
    String Kehadiran;
    String Nik;
    String Nama;
    String KodeWilayah;
    String response;

    public Absensi(String jabatan, String jamAbsen, String kehadiran, String Nik, String nama,String kodeWilayah,String tanggal, String response) {
//        Jabatan = jabatan;
        Tanggal = tanggal;
        Kehadiran = kehadiran;
        KodeWilayah=kodeWilayah;
        this.Nik = Nik;
        Nama = nama;
        this.response = response;
    }
//
//    public String getJabatan() {
//        return Jabatan;
//    }
//
    public String getJamAbsen() {
        return Tanggal;
    }
//
    public String getKetAbsen() {
        return Kehadiran;
    }

    public String getKodeWilayah(){
        return KodeWilayah;
    }
//
    public String getNIK() {
        return Nik;
    }

    public String getNama() {
        return Nama;
    }

    public String getResponse() {
        return response;
    }
}
