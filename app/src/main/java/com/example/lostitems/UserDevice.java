package com.example.lostitems;

public class UserDevice {
    private String uniqueID;
    private String deviceName;
    private String deviceLat;
    private String deviceLon;
    private int deviceLogo;

    public UserDevice() {
    }

    public UserDevice(String uniqueID, String deviceName, int deviceLogo, String deviceLat, String deviceLon) {
        this.uniqueID = uniqueID;
        this.deviceName = deviceName;
        this.deviceLogo = deviceLogo;
        this.deviceLat = deviceLat;
        this.deviceLon = deviceLon;
    }

    public String getDeviceLat() {
        return deviceLat;
    }

    public void setDeviceLat(String deviceLat) {
        this.deviceLat = deviceLat;
    }

    public String getDeviceLon() {
        return deviceLon;
    }

    public void setDeviceLon(String deviceLon) {
        this.deviceLon = deviceLon;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getDeviceLogo() {
        return deviceLogo;
    }

    public void setDeviceLogo(int deviceLogo) {
        this.deviceLogo = deviceLogo;
    }
}
