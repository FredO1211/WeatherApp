package com.example.weatherapp;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GetCurrentLocation extends AsyncTask<Void,Void,Coordinates>{
    private static final String TAG = "GetCurrentLocation";
    private FusedLocationProviderClient fusedLocationClient;
    private Coordinates coordinates;
    private Activity activity;

    private final GetWeatherJsonData.OnDataAvailable callback;

    public GetCurrentLocation(Activity activity, GetWeatherJsonData.OnDataAvailable callback) {
        this.activity = activity;
        this.callback = callback;
    }

    @Override
    protected Coordinates doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: start");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            coordinates = new Coordinates(location.getLatitude(),location.getLongitude());
                            Log.d(TAG, "onSuccess: " +coordinates.toString());
                            GetWeatherJsonData getWeatherJsonData = new GetWeatherJsonData(callback,
                                    "https://api.openweathermap.org/data/2.5/weather",
                                    location.getLatitude(),location.getLongitude(),
                                    "da04e8d884c2338e773b27e31ebd3e93",
                                    "metric");
                            getWeatherJsonData.execute();
                        }
                        else {
                            Log.d(TAG, "onSuccess: unsuccessful");
                        }
                    }
                });

        Log.d(TAG, "doInBackground: stop");

        return coordinates;
    }

}
