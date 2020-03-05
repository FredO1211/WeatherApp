package com.example.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements GetWeatherJsonData.OnDataAvailable {
    private static final String TAG = "MainActivity";

    private static TextView temperatureTextView;
    private static TextView cityTextView;
    private static TextView weatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView = findViewById(R.id.temperatureTextView);
        cityTextView = findViewById(R.id.cityTextView);
        weatherTextView = findViewById(R.id.weatherTextView);

        Log.d(TAG, "onCreate: end");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        GetWeatherJsonData getWeatherJsonData = new GetWeatherJsonData(this,"https://api.openweathermap.org/data/2.5/weather",49.30,22.41,"da04e8d884c2338e773b27e31ebd3e93","metric");
        getWeatherJsonData.execute();
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(Weather data, DownloadStatus status){
        if(status==DownloadStatus.OK){
            Log.d(TAG, "onDataAvailable: data is " + data);
            cityTextView.setText(data.getCity());
            temperatureTextView.setText(data.getTemperature()+"°C");
            weatherTextView.setText(data.getWeatherType());
        } else {
            Log.e(TAG, "onDataAvailable: failed with status"+status);
        }

    }

}
