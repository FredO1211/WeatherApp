package com.example.weatherapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class GetWeatherJsonData extends AsyncTask<String,Void,Weather> implements FetchData.OnDownloadComplete {
    private static final String TAG = "GetWeatherJsonData";

    private Weather weather=null;
    private String baseURL;
    private double latitude;
    private double longitude;
    private String apiId;
    private String units;

    private final OnDataAvailable callback;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable{
        void onDataAvailable(Weather weather, DownloadStatus status);
    }

    public GetWeatherJsonData(OnDataAvailable callback, String baseURL, double latitude, double longitude, String apiId, String units ) {
        this.baseURL = baseURL;
        this.latitude = latitude;
        this.longitude = longitude;
        this.apiId = apiId;
        this.units = units;

        this.callback = callback;
    }

    void executeOnSameThread(){
        Log.d(TAG, "executeOnSameThread: starts");
        runningOnSameThread=true;
        String destinationUri=createUri(latitude,longitude,apiId,units);

        FetchData fetchData = new FetchData(this);
        fetchData.execute(destinationUri);
        Log.d(TAG, "executeOnSameThread: ends");
    }

    @Override
    protected void onPostExecute(Weather weather) {
        Log.d(TAG, "onPostExecute: starts");

        if(callback !=null){
            callback.onDataAvailable(weather,DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: end");
    }

    @Override
    protected Weather doInBackground(String... params) {
        Log.d(TAG, "doInBackground: starts");
        String destinationUri = createUri(latitude,longitude,apiId,units);

        FetchData fetchData = new FetchData(this);
        fetchData.runInSameThread(destinationUri);
        Log.d(TAG, "doInBackground: ends");
        return weather;
    }

    private String createUri(double latitude, double longitude, String apiId, String units){
        Log.d(TAG, "createUri: starts");
        DecimalFormat df2 = new DecimalFormat("#.##");
        df2.setRoundingMode(RoundingMode.UP);

        Uri uri=Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("lat",df2.format(latitude))
                .appendQueryParameter("lon",df2.format(longitude))
                .appendQueryParameter("appid",apiId)
                .appendQueryParameter("units",units)
                .build();

        return uri.toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete: starts. Status = "+status);

        if(status == DownloadStatus.OK){

            try{
                JSONObject jsonObject = new JSONObject(data);
                JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");
                JSONObject mainJsonObject = jsonObject.getJSONObject("main");

                String mainWeather = weatherJsonArray.getJSONObject(0).getString("main");
                int temperature =(int) mainJsonObject.getDouble("temp");
                String cityName = jsonObject.getString("name");

                weather=new Weather(cityName,mainWeather,temperature);
                Log.d(TAG, "onDownloadComplete: "+ weather.toString());
            } catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data "+ e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(runningOnSameThread && callback !=null){
            // now inform the caller thar processing is done  - possibly returning null id there
            // wan an error
            callback.onDataAvailable(weather, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
