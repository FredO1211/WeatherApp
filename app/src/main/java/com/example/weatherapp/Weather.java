package com.example.weatherapp;

class Weather {
    private String village;
    private String weatherType;
    private double temperature;

    public Weather(String village, String weatherType, double temperature) {
        this.village = village;
        this.weatherType = weatherType;
        this.temperature = temperature;
    }

    public String getVillage() {
        return village;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public double getTemperature() {
        return temperature;
    }
}
