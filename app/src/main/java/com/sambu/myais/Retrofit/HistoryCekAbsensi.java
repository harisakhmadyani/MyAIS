package com.sambu.myais.Retrofit;

public class HistoryCekAbsensi {
    String Nik;
    String Nama;
    String KodeWilayah;
    String KehadiranMasuk;
    String KehadiranPulang;
    String JamMasuk;
    String JamPulang;
    String KodeSubPersilMasuk;
    String KodeSubPersilPulang;
    String KetMasuk;
    String KetPulang;
    String response;

    public HistoryCekAbsensi(String Nik,String Nama , String KodeWilayah,String KehadiranMasuk,String KehadiranPulang ,String JamMasuk,String JamPulang,String KodeSubPersilMasuk,String KodeSubPersilPulang, String KetMasuk, String KetPulang, String response ){
        this.Nik = Nik;
        this.Nama = Nama;
        this.KodeWilayah = KodeWilayah;
        this.KehadiranMasuk = KehadiranMasuk;
        this.KehadiranPulang = KehadiranPulang;
        this.JamMasuk = JamMasuk;
        this.JamPulang = JamPulang;
        this.KodeSubPersilMasuk = KodeSubPersilMasuk;
        this.KodeSubPersilPulang = KodeSubPersilPulang;
        this.KetMasuk = KetMasuk;
        this.KetPulang = KetPulang;
        this.response = response ;
    }


    public String getNik() {
        return Nik;
    }


    public String getNama() {
        return this.Nama;
    }

    public String getKodeWilayah() {
        return this.KodeWilayah;
    }

    public String getKehadiranMasuk() {
        return this.KehadiranMasuk;
    }

    public String getKehadiranPulang() {
        return this.KehadiranPulang;
    }

    public String getJamMasuk() {
        return this.JamMasuk;
    }

    public String getJamPulang() {
            return this.JamPulang;
    }

    public String getKetMasuk() {
        return KetMasuk;
    }

    public String getKetPulang() {
        return KetPulang;
    }

    public String getKodeSubPersilMasuk() {
        return this.KodeSubPersilMasuk;
    }

    public String getKodeSubPersilPulang() {
        return this.KodeSubPersilPulang;
    }

    public Object getResponse(){return this.response;}


}
