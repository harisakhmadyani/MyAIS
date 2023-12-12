package com.sambu.myais.Retrofit;

public class TipeKecelakaan {

    /* renamed from: ID */
    Integer KecelakaanID;
    String Kecelakaan;
    String Keterangan;
    Integer Sort;
    String status;

    public TipeKecelakaan(Integer KecelakaanID, String Kecelakaan, String Keterangan, Integer Sort, String status) {
        this.KecelakaanID = KecelakaanID;
        this.Kecelakaan = Kecelakaan;
        this.Keterangan = Keterangan;
        this.Sort = Sort;
        this.status = status;
    }

    public Integer getKecelakaanID() {
        return this.KecelakaanID;
    }

    public String getKecelakaan() {
        return this.Kecelakaan;
    }

    public String getKeterangan() {
        return this.Keterangan;
    }

    public Integer getSort() {
        return this.Sort;
    }

    public  String GetStatus() { return  this.status; }
}