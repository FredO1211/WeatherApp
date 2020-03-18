package com.example.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements GetWeatherJsonData.OnDataAvailable {
    private static final String TAG = "MainActivity";

    private static TextView temperatureTextView;
    private static TextView cityTextView;
    private static TextView weatherTextView;
    private static ImageView backgroundImageView;
    private static CardView findCardView;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: before request");
        requestPermission();

        db=new Database(this);

        final Intent intent=new Intent(this, FindCityActivity.class);

        temperatureTextView = findViewById(R.id.temperatureTextView);
        cityTextView = findViewById(R.id.cityTextView);
        weatherTextView = findViewById(R.id.weatherTextView);
        backgroundImageView = findViewById(R.id.backgroundImageView);
        findCardView = findViewById(R.id.findCardView);

        if(db.ifRecordsExist()) {
            temperatureTextView.setText(db.getLastTemperature() + "°C");
            cityTextView.setText(db.getLastCity());
            weatherTextView.setText(db.getLastWeatherType());

            setBackgroundImageView(db.getLastWeatherType());
        }


        findCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        GetCurrentLocation getCurrentLocation = new GetCurrentLocation(this,this);
        getCurrentLocation.execute();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: starts");
        super.onResume();
        Log.d(TAG, "onResume: ends");
    }

    @Override
    public void onDataAvailable(ArrayList<Weather> weathers, DownloadStatus status){
        try {
            Weather data=weathers.get(0);
            if (status == DownloadStatus.OK) {
                Log.d(TAG, "onDataAvailable: data is " + data);
                db.addWeather(data.getTemperature(),data.getCity(),data.getWeatherType());
                cityTextView.setText(data.getCity());
                temperatureTextView.setText(data.getTemperature() + "°C");
                weatherTextView.setText(data.getWeatherType());

                setBackgroundImageView(data.getWeatherType());

            } else {
                Log.e(TAG, "onDataAvailable: failed with status" + status);
            }
        }catch (Exception e){

            Context context = getApplicationContext();
            String text = "Current weather is not available now. \nPlease try again later.";
            int duration = Toast.LENGTH_SHORT;
            Log.d(TAG, "onDataAvailable: Wrong data");
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();


        }

    }
    private void requestPermission(){
        Log.d(TAG, "requestPermission: start");
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
        Log.d(TAG, "requestPermission: ends");
    }

    private void setBackgroundImageView(String weather){
        if(weather.equals("Clouds")){
            backgroundImageView.setImageResource(R.drawable.cloudy);
        }
        else if(weather.equals("Rain") || weather.equals("Drizzle")){
            backgroundImageView.setImageResource(R.drawable.rainy);
        }
        else if(weather.equals("Clear")){
            backgroundImageView.setImageResource(R.drawable.sunny);
        }
        else if(weather.equals("Extreme")||weather.equals("Thunderstorm")){
            backgroundImageView.setImageResource(R.drawable.stormy);
        }
        else {
            backgroundImageView.setImageResource(R.drawable.sunny);
        }
    }
}
