package com.sambu.myais.Retrofit;

public class Personel {

    int ID;
    String IDPersonil;
    String IM;
    String Jabatan;
    String JamMasuk;
    String JamPulang;
    int NIK;
    String Nama;
    String Status;

    public Personel(int ID, String IDPersonil2, String nama, int NIK2, String jabatan, String status, String IM, String jamMasuk, String jamPulang) {
        this.ID = ID;
        this.IDPersonil = IDPersonil2;
        this.Nama = nama;
        this.NIK = NIK2;
        this.Jabatan = jabatan;
        this.Status = status;
        this.IM = IM;
        this.JamMasuk = jamMasuk;
        this.JamPulang = jamPulang;
    }

    public int getID() {
        return this.ID;
    }

    public String getIDPersonil() {
        return this.IDPersonil;
    }

    public String getNama() {
        return this.Nama;
    }

    public int getNIK() {
        return this.NIK;
    }

    public String getJabatan() {
        return this.Jabatan;
    }

    public String getStatus() {
        return this.Status;
    }

    public String getIM() {
        return this.IM;
    }

    public String getJamMasuk() {
        return this.JamMasuk;
    }

    public String getJamPulang() {
        return this.JamPulang;
    }
}
