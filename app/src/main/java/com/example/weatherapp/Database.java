package com.example.weatherapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    public Database (Context context) {
        super(context, "weather.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Last_weather(" +
                "WeatherID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Temperature INTEGER, " +
                "City TEXT, " +
                "Weather TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addWeather(int temperature, String city, String weatherType){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Temperature",temperature);
        contentValues.put("City",city);
        contentValues.put("Weather",weatherType);
        database.insert("Last_weather",null, contentValues);
        database.close();
    }

    Cursor getAllRecordsCursor(){
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM Last_weather ORDER BY WeatherID ASC;", null);
        return cursor;
    }

    public int getLastTemperature(){
        Cursor cursor = getAllRecordsCursor();
        cursor.moveToLast();
        return cursor.getInt(1);
    }

    public String getLastCity(){
        Cursor cursor = getAllRecordsCursor();
        cursor.moveToLast();
        return cursor.getString(2);
    }

    public String getLastWeatherType(){
        Cursor cursor = getAllRecordsCursor();
        cursor.moveToLast();
        return cursor.getString(3);
    }

    public boolean ifRecordsExist(){
        return getAllRecordsCursor().moveToFirst();
    }
}
