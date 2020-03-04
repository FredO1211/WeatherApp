package com.example.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements FetchData.OnDownloadComplete {
    private static final String TAG = "MainActivity";

    public static TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView=findViewById(R.id.temperatureTextView);
        Button buttonParse = findViewById(R.id.parseButton);

        FetchData process = new FetchData(this);
        process.execute("https://api.openweathermap.org/data/2.5/weather?q=Sanok&appid=da04e8d884c2338e773b27e31ebd3e93&units=metric");
    }
    @Override
    public void onDownloadComplete(String data, DownloadStatus status){
        if(status==DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete: data is " + data);
        } else {
            Log.e(TAG, "onDownloadComplete: failed with status"+status);
        }
    }


}
