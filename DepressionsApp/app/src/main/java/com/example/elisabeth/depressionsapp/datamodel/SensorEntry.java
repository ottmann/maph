package com.example.elisabeth.depressionsapp.datamodel;

/**
 * Created by elisabeth on 06.01.18.
 */
public class SensorEntry {
    public int id;
    public double temperature;
    public int airquality;
    public int lightvalue;
    public String timestamp;

    public SensorEntry() {}

    public SensorEntry (double temperature, int airquality,int lightvalue, String timestamp) {
        this.temperature = temperature;
        this.airquality = airquality;
        this.lightvalue=lightvalue;
        this.timestamp = timestamp;
    }

    public SensorEntry (int id, double temperature, int airquality,int lightvalue, String timestamp) {
        this.id = id;
        this.temperature = temperature;
        this.airquality = airquality;
        this.lightvalue=lightvalue;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getAirquality() {
        return airquality;
    }

    public void setAirquality(int airquality) {
        this.airquality = airquality;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getLightValue(){return lightvalue;}

    public void setLightValue(int lightvalue) {this.lightvalue=lightvalue;}
}
