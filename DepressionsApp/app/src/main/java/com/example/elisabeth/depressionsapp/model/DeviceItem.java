package com.example.elisabeth.depressionsapp.model;

/**
 * Created by elisabeth on 17.12.17.
 */
public class DeviceItem {

    private String deviceName;
    private String address;
    private boolean connected;

    public String getDeviceName() {
        return deviceName;
    }

    public boolean getConnected() {
        return connected;
    }

    public String getAddress() {
        return address;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceItem(String name, String address, String connected){
        this.deviceName = name;
        this.address = address;
        if (connected == "true") {
            this.connected = true;
        }
        else {
            this.connected = false;
        }
    }
}
