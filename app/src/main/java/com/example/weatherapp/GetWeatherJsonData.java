package com.example.weatherapp;

import android.net.Uri;
import android.util.Log;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

class GetWeatherJsonData implements FetchData.OnDownloadComplete {
    private static final String TAG = "GetWeatherJsonData";

    private List<Weather> weathers=null;
    private String baseURL;
    private double latitude;
    private double longitude;
    private String apiId;
    private String units;

    private final OnDataAvailable callback;

    interface OnDataAvailable{
        void onDataAvailable(List<Weather> data, DownloadStatus status);
    }

    public GetWeatherJsonData(String baseURL, double latitude, double longitude, String apiId, String units, OnDataAvailable callback) {
        this.baseURL = baseURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.apiId = apiId;
        this.units = units;
        this.callback = callback;
    }

    void executeOnSameThread(){
        Log.d(TAG, "executeOnSameThread: starts");
        String destinationUri=createUri(latitude,longitude,apiId,units);

        FetchData fetchData = new FetchData(this);
        fetchData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    private String createUri(double latitude, double longitude, String apiId, String units){
        Log.d(TAG, "createUri: starts");
        DecimalFormat df2 = new DecimalFormat("#.##");
        df2.setRoundingMode(RoundingMode.UP);

        return Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("lat",df2.format(latitude))
                .appendQueryParameter("lon", df2.format(longitude))
                .appendQueryParameter("appid", apiId)
                .appendQueryParameter("units", units)
                .build().toString();
    }
}
