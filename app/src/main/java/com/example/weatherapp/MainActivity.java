package com.example.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private TextView temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperatureTextView=findViewById(R.id.temperatureTextView);
        findWeather();
    }

    public void findWeather(){
        String url="http://api.openweathermap.org/data/2.5/weather?q=Sanok&appid=da04e8d884c2338e773b27e31ebd3e93&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("main");
                    int temp = (int) jsonObject.getDouble("temp");

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("nie dzila");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
        setTemperature(20);
    }
    public void setTemperature(int temperature){
        temperatureTextView.setText(temperature+"°C");
    }
}
