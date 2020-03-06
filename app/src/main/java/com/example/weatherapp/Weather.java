package com.example.weatherapp;

class Weather {
    private String city;
    private String weatherType;
    private int temperature;

    public Weather(String city, String weatherType, int temperature) {
        this.city = city;
        this.weatherType = weatherType;
        this.temperature = temperature;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", weatherType='" + weatherType + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}
