package com.sambu.myais.Retrofit;

public class DeviceInfo {
    String API;
    String APPVersion;
    String AndroidVersion;
    String CreatedDate;
    String Device;
    String UserID;
    String Model;
    String PancangID;
    String SerialDevice;
    String lanjut;
    String status;
    String KodeWilayah;
    String GroupAccess;

    public DeviceInfo(String ID, String serialDevice, String device, String model, String API2, String androidVersion, String APPVersion2, String pancangID, String createdDate, String status2, String lanjut2, String kodeWilayah, String groupAccess) {
        this.UserID = ID;
        this.SerialDevice = serialDevice;
        this.Device = device;
        this.Model = model;
        this.API = API2;
        this.AndroidVersion = androidVersion;
        this.APPVersion = APPVersion2;
        this.PancangID = pancangID;
        this.CreatedDate = createdDate;
        this.status = status2;
        this.lanjut = lanjut2;
        this.KodeWilayah = kodeWilayah;
        this.GroupAccess = groupAccess;
    }

    public String getUserID() {
        return this.UserID;
    }

    public String getSerialDevice() {
        return this.SerialDevice;
    }

    public String getDevice() {
        return this.Device;
    }

    public String getModel() {
        return this.Model;
    }

    public String getAPI() {
        return this.API;
    }

    public String getAndroidVersion() {
        return this.AndroidVersion;
    }

    public String getAPPVersion() {
        return this.APPVersion;
    }

    public String getPancangID() {
        return this.PancangID;
    }

    public String getCreatedDate() {
        return this.CreatedDate;
    }

    public String getStatus() {
        return this.status;
    }

    public String getLanjut() {
        return this.lanjut;
    }

    public String getKodeWilayah() { return this.KodeWilayah; }

    public String getGroupAccess() { return this.GroupAccess; }
}
