package com.example.weatherapp;

public class WeatherRVModal {
    private String time;
    private String tamprature;
    private String icon;
    private String windspeed;

    public WeatherRVModal(String time, String tamprature, String icon, String windspeed) {
        this.time = time;
        this.tamprature = tamprature;
        this.icon = icon;
        this.windspeed = windspeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTamprature() {
        return tamprature;
    }

    public void setTamprature(String tamprature) {
        this.tamprature = tamprature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
}
