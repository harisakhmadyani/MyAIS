package com.sambu.myais.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseAccess {
    private static DataBaseAccess instance;

    private SQLiteDatabase sqLiteDatabase;
    private final SQLiteOpenHelper openHelper;

    private DataBaseAccess(Context context) {
        this.openHelper = new DataBaseOpenHelper(context);
    }

    public static DataBaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DataBaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.sqLiteDatabase = this.openHelper.getWritableDatabase();
    }

    public void close() {
        SQLiteDatabase sQLiteDatabase = this.sqLiteDatabase;
        if (sQLiteDatabase != null) {
            sQLiteDatabase.close();
        }
    }

    public Integer Delete(String table, String where, String value) {
        return this.sqLiteDatabase.delete(table, where, new String[]{value});
    }

    public Cursor DeleteAll(String table) {
        return this.sqLiteDatabase.rawQuery("DELETE FROM " + table, (String[]) null);
    }

    public Cursor DeleteWhere(String table, String where) {
        return this.sqLiteDatabase.rawQuery("DELETE FROM " + table + " WHERE " + where, (String[]) null);
    }

    public Cursor Get(String table) {
        return this.sqLiteDatabase.rawQuery("SELECT * FROM " + table, (String[]) null);
    }

    public Cursor GetCustom(String query) {
        return this.sqLiteDatabase.rawQuery(query, (String[]) null);
    }

    public Cursor GetLast(String table) {
        return this.sqLiteDatabase.rawQuery("SELECT * FROM " + table + " ORDER BY ID DESC LIMIT 1", (String[]) null);
    }

    public Cursor Where(String table, String where) {
        return this.sqLiteDatabase.rawQuery("SELECT * FROM " + table + " WHERE " + where, (String[]) null);
    }

    public Cursor Order(String table, String order) {
        return this.sqLiteDatabase.rawQuery("SELECT * FROM " + table + " ORDER BY " + order, (String[]) null);
    }

    public boolean insertPancang(int IDPancang, String Pancang, double Latitude, double Longitude, int Toleransi) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("IDPancang", IDPancang);
        contentValues.put("Pancang", Pancang);
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        contentValues.put("Toleransi", Toleransi);
        return this.sqLiteDatabase.insert("PancangTable", (String) null, contentValues) != -1;
    }

    public boolean insertUser(String UserID, String KodeWilayah) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", UserID);
        contentValues.put("KodeWilayah", KodeWilayah);
        return this.sqLiteDatabase.insert("User", (String) null, contentValues) != -1;
    }

    public boolean updatePancang(int IDPancang, double Latitude, double Longitude, int Toleransi) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Latitude", Latitude);
        contentValues.put("Longitude", Longitude);
        contentValues.put("Toleransi", Toleransi);
        return ((long) this.sqLiteDatabase.update("PancangTable", contentValues, "IDPancang=?", new String[]{String.valueOf(IDPancang)})) != -1;
    }

    public boolean insertPersonel(int ID, String IDPersonil, String nama, int NIK, String jabatan, String status, String IM, String jamMasuk, String jamPulang) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", ID);
        contentValues.put("PersonelID", IDPersonil);
        contentValues.put("Nama", nama);
        contentValues.put("Nik", NIK);
        contentValues.put("Jabatan", jabatan);
        contentValues.put("Status", status);
        contentValues.put("IM", IM);
        contentValues.put("JamMasuk", jamMasuk);
        contentValues.put("JamPulang", jamPulang);
        return this.sqLiteDatabase.insert("PersonelTable", (String) null, contentValues) != -1;
    }

//    public boolean insertAbsensi(String UserID, int PancangID, String Kehadiran, double Latitude, double Longitude, byte[] Foto, String CreatedDate, boolean Terkirim, String AppVersion) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("UserID", UserID);
//        contentValues.put("PancangID", PancangID);
//        contentValues.put("Kehadiran", Kehadiran);
//        contentValues.put("Latitude", Latitude);
//        contentValues.put("Longitude", Longitude);
//        contentValues.put("Foto", Foto);
//        contentValues.put("CreatedDate", CreatedDate);
//        contentValues.put("Terkirim", Terkirim);
//        contentValues.put("AppVersion", AppVersion);
//        return this.sqLiteDatabase.insert("AbsensiTable", (String) null, contentValues) != -1;
//    }


    public boolean updateAbsensiFalse() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Terkirim", false);
        return ((long) this.sqLiteDatabase.update("AbsensiTable", contentValues, (String) null, new String[0])) != -1;
    }

    public boolean insertKehadiran(Integer ID, String Nama, String ShortName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", ID);
        contentValues.put("Nama", Nama);
        contentValues.put("ShortName", ShortName);
        return this.sqLiteDatabase.insert("Kehadiran", (String) null, contentValues) != -1;
    }

    public boolean insertTenagaKerja(Integer regno, String nik, String nama, String kodewilayah) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("RegNo", regno);
        contentValues.put("NIK", nik);
        contentValues.put("Nama", nama);
        contentValues.put("KodeWilayah", kodewilayah);
        return this.sqLiteDatabase.insert("TenagaKerja", (String) null, contentValues) != -1;
    }

    public boolean insertSubPersilKelapa(String kodesubpersil, Double latitude, Double longitude, Integer toleransi, Integer lokasiabsensimasuk) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("KodeSubPersil", kodesubpersil);
        contentValues.put("Latitude", latitude);
        contentValues.put("Longitude", longitude);
        contentValues.put("Toleransi", toleransi);
        contentValues.put("Active", 0);
        contentValues.put("LokasiAbsensiMasuk", lokasiabsensimasuk);
        contentValues.put("ActiveLokasiMasuk", 0);
        return this.sqLiteDatabase.insert("SubPersilKelapa", (String) null, contentValues) != -1;
    }

    public boolean insertUrl(String APIName, String APIUrl) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("APIName", APIName);
        contentValues.put("APIUrl", APIUrl);
        return this.sqLiteDatabase.insert("APIUrl", (String) null, contentValues) != -1;
    }

    public boolean inactiveSubPersilKelapa() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Active", 0);
        return (this.sqLiteDatabase.update("SubPersilKelapa", contentValues, (String) null, new String[0])) != -1;
    }

    public boolean activeSubPersilKelapa(String kodesubpersil) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Active", 1);
        return (this.sqLiteDatabase.update("SubPersilKelapa", contentValues, "KodeSubPersil=?", new String[]{kodesubpersil})) != -1;
    }

    public boolean inactiveSubPersilMasuk() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ActiveLokasiMasuk", 0);
        return (this.sqLiteDatabase.update("SubPersilKelapa", contentValues, (String) null, new String[0])) != -1;
    }

    public boolean activeSubPersilMasuk(String kodesubpersil) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ActiveLokasiMasuk", 1);
        return (this.sqLiteDatabase.update("SubPersilKelapa", contentValues, "KodeSubPersil=?", new String[]{kodesubpersil})) != -1;
    }

    public boolean insertAbsensi(String userid, Integer regno, String kehadiran, String kodeabsen, String tanggal, String kodesubpersil, double latitude, double longitude, byte[] foto, boolean terkirim, String serialdevice, String appversion, String keterangan) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", userid);
        contentValues.put("RegNo", regno);
        contentValues.put("Kehadiran", kehadiran);
        contentValues.put("KodeAbsen", kodeabsen);
        contentValues.put("Tanggal", tanggal);
        contentValues.put("KodeSubPersil", kodesubpersil);
        contentValues.put("Latitude", latitude);
        contentValues.put("Longitude", longitude);
        contentValues.put("Foto", foto);
        contentValues.put("Terkirim", terkirim);
        contentValues.put("SerialDevice", serialdevice);
        contentValues.put("AppVersion", appversion);
        contentValues.put("Keterangan", keterangan);
        return this.sqLiteDatabase.insert("Absensi", (String) null, contentValues) != -1;
    }

    public boolean updateAbsensi(String ID) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Terkirim", true);
        return (this.sqLiteDatabase.update("Absensi", contentValues, "ID=?", new String[]{ID})) != -1;
    }
}
