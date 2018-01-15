package com.example.elisabeth.depressionsapp.datamodel;

/**
 * Created by elisabeth on 06.01.18.
 */
public class SensorEntry {
    public int id;
    public double temperature;
    public double airquality;
    public String timestamp;

    public SensorEntry() {}

    public SensorEntry (double temperature, double airquality, String timestamp) {
        this.temperature = temperature;
        this.airquality = airquality;
        this.timestamp = timestamp;
    }

    public SensorEntry (int id, double temperature, double airquality, String timestamp) {
        this.id = id;
        this.temperature = temperature;
        this.airquality = airquality;
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

    public double getAirquality() {
        return airquality;
    }

    public void setAirquality(double airquality) {
        this.airquality = airquality;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
