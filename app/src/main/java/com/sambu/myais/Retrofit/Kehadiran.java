package com.sambu.myais.Retrofit;

public class Kehadiran {

    /* renamed from: ID */
    Integer ID;
    String Kehadiran;
    String Keterangan;
    String status;

    public Kehadiran(Integer ID, String kehadiran, String keterangan,String status) {
        this.ID = ID;
        this.Kehadiran = kehadiran;
        this.Keterangan = keterangan;
        this.status = status;
    }

    public Integer getID() {
        return this.ID;
    }

    public String getKehadiran() {
        return this.Kehadiran;
    }

    public String getKeterangan() {
        return this.Keterangan;
    }

    public  String GetStatus() { return  this.status; }
}