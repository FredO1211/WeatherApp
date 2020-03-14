package com.example.weatherapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class GetWeatherJsonData extends AsyncTask<Void,Void,Void> implements FetchData.OnDownloadComplete {
    private static final String TAG = "GetWeatherJsonData";

    private Weather weather=null;
    private String baseURL;
    private double latitude;
    private double longitude;
    private String apiId;
    private String units;
    private String location;

    private ArrayList<Weather> weathers = new ArrayList<>();

    private final OnDataAvailable callback;
    private boolean runningOnSameThread = false;

    interface OnDataAvailable{
        void onDataAvailable(ArrayList<Weather> weathers, DownloadStatus status);
    }

    public GetWeatherJsonData(OnDataAvailable callback, String baseURL, Double latitude, Double longitude, String apiId, String units ) {
        this.latitude=latitude;
        this.longitude=longitude;
        this.baseURL = baseURL;
        this.apiId = apiId;
        this.units = units;
        this.callback = callback;
    }

    public GetWeatherJsonData(OnDataAvailable callback, String baseURL, String location, String apiId, String units ) {
        this.location=location;
        this.baseURL = baseURL;
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
    protected void onPostExecute(Void aVoid) {
        Log.d(TAG, "onPostExecute: starts");

        if(callback !=null){
            callback.onDataAvailable(this.weathers,DownloadStatus.OK);
        }

        Log.d(TAG, "onPostExecute: end");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: starts");
        FetchData fetchData = new FetchData(this);
        if(location!=null){
            String destinationUri = createUri(location,apiId,units);
            fetchData.runInSameThread(destinationUri);
        }
        else{
            String destinationUri = createUri(latitude,longitude,apiId,units);
            fetchData.runInSameThread(destinationUri);
        }
        Log.d(TAG, "doInBackground: ends");
        return null;
    }

    private String createUri(String city, String apiId, String units){
        Log.d(TAG, "createUri: starts");

        Uri uri=Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("q",String.valueOf(city))
                .appendQueryParameter("appid",apiId)
                .appendQueryParameter("units",units)
                .build();

        return uri.toString();
    }

    private String createUri(double latitude, double longitude, String apiId, String units){
        Log.d(TAG, "createUri: starts");

        Uri uri=Uri.parse(baseURL).buildUpon()
                .appendQueryParameter("lat",String.valueOf(latitude))
                .appendQueryParameter("lon",String.valueOf(longitude))
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
                if(location!=null){
                    JSONArray listJsonArray = jsonObject.getJSONArray("list");
                    JSONObject cityJSONObject = jsonObject.getJSONObject("city");
                    int timezone =cityJSONObject.getInt("timezone");

                    for(int i = 0 ; i<listJsonArray.length(); i++) {
                        JSONObject mainJsonObject = listJsonArray.getJSONObject(i).getJSONObject("main");
                        JSONArray weatherJsonArray = listJsonArray.getJSONObject(i).getJSONArray("weather");


                        int temperature= (int) mainJsonObject.getDouble("temp");
                        String mainWeather = weatherJsonArray.getJSONObject(0).getString("main");
                        String city = String.valueOf(listJsonArray.getJSONObject(i).getInt("dt")-timezone);

                        Log.d(TAG, "onDownloadComplete: "+timezone);
                        this.weathers.add(new Weather(city,mainWeather,temperature));
                    }
                }
                else {
                    JSONArray weatherJsonArray = jsonObject.getJSONArray("weather");
                    JSONObject mainJsonObject = jsonObject.getJSONObject("main");

                    String mainWeather = weatherJsonArray.getJSONObject(0).getString("main");
                    int temperature = (int) mainJsonObject.getDouble("temp");
                    String cityName = jsonObject.getString("name");

                    this.weathers.add(new Weather(cityName, mainWeather, temperature));
                }
                Log.d(TAG, "onDownloadComplete: "+ weathers.get(0).toString());
            } catch (JSONException e){
                e.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data "+ e.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(runningOnSameThread && callback !=null){
            // now inform the caller thar processing is done  - possibly returning null id there
            // wan an error
            callback.onDataAvailable(this.weathers, status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }
}
