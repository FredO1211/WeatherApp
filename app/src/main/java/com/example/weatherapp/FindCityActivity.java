package com.example.weatherapp;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FindCityActivity extends AppCompatActivity implements GetWeatherJsonData.OnDataAvailable{

private TextView currentTemperatureTV;
private TextView tomorrowTemperatureTV;
private TextView dATTemperatureTV;
private TextView textViewCurrent;
private TextView textViewTomorrow;
private TextView textViewDAT;

private ImageView currentWeatherIV;
private ImageView tomorrowWeatherIV;
private ImageView dATWeatherIV;

private EditText cityEditText;

private Button searchButton;

private GetWeatherJsonData getWeatherJsonData;

private GetWeatherJsonData.OnDataAvailable callback;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_city);
        searchButton=findViewById(R.id.search_btn);

        currentTemperatureTV=findViewById(R.id.currentTemperatureTV);
        tomorrowTemperatureTV=findViewById(R.id.tomorrowTemperatureTV);
        dATTemperatureTV=findViewById(R.id.DATTemperatureTV);

        currentWeatherIV=findViewById(R.id.currentWeatherIV);
        tomorrowWeatherIV=findViewById(R.id.tomorrowWeatherIV);
        dATWeatherIV=findViewById(R.id.DATWeatherIV);

        textViewCurrent=findViewById(R.id.textView5);
        textViewTomorrow=findViewById(R.id.textView6);
        textViewDAT=findViewById(R.id.textView7);

        cityEditText=findViewById(R.id.cityEditText);

        callback=this;

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherJsonData = new GetWeatherJsonData(callback,"https://api.openweathermap.org/data/2.5/forecast",cityEditText.getText().toString(),"da04e8d884c2338e773b27e31ebd3e93","metric");
                getWeatherJsonData.execute();
                closeKeyboard();
            }
        });
    }

    public void setVisble(){
        currentTemperatureTV.setVisibility(View.VISIBLE);
        tomorrowTemperatureTV.setVisibility(View.VISIBLE);
        dATTemperatureTV.setVisibility(View.VISIBLE);
        textViewCurrent.setVisibility(View.VISIBLE);
        textViewTomorrow.setVisibility(View.VISIBLE);
        textViewDAT.setVisibility(View.VISIBLE);

        currentWeatherIV.setVisibility(View.VISIBLE);
        tomorrowWeatherIV.setVisibility(View.VISIBLE);
        dATWeatherIV.setVisibility(View.VISIBLE);
    }

    private void closeKeyboard() {
        View view =this.getCurrentFocus();
        if(view !=null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

    private void setIcons(Weather weather, TextView temperature, ImageView icon){

        temperature.setText(weather.getTemperature()+"°C");

        if(weather.getWeatherType().equals("Clouds")){
            icon.setImageResource(R.drawable.clouds);
        }
        else if(weather.getWeatherType().equals("Rain") || weather.getWeatherType().equals("Drizzle")){
            icon.setImageResource(R.drawable.rain);
        }
        else if(weather.getWeatherType().equals("Clear")){
            icon.setImageResource(R.drawable.sun);
        }
        else if(weather.getWeatherType().equals("Extreme")||weather.getWeatherType().equals("Thunderstorm")){
            icon.setImageResource(R.drawable.storm);
        }
        else if(weather.getWeatherType().equals("Snow")){
            icon.setImageResource(R.drawable.storm);
        }
        else {
            icon.setImageResource(R.drawable.sunny);
        }
    }

    @Override
    public void onDataAvailable(ArrayList<Weather> weathers, DownloadStatus status) {
        try {
            if (status == DownloadStatus.OK) {
                setIcons(weathers.get(0),currentTemperatureTV,currentWeatherIV);
                int i=0;
                if((Integer.parseInt(weathers.get(0).getCity()))%86400>=50400){
                    i=8;
                }
                while(i<weathers.size()){
                    if(((Integer.parseInt(weathers.get(i).getCity()))%86400<55799) &&((Integer.parseInt(weathers.get(i).getCity()))%86400>45000 )){
                        setIcons(weathers.get(i),tomorrowTemperatureTV,tomorrowWeatherIV);
                        setIcons(weathers.get(i+8),dATTemperatureTV,dATWeatherIV);
                        break;
                    }
                    i++;
                }
                setVisble();
            }
        }catch (Exception e){
            Context context = getApplicationContext();
            String text = "Current weather is not available now. \nPlease try again later.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context,text,duration);
            toast.show();
        }
    }
}
