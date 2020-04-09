package com.example.weather4cast;

import android.content.Context;
import android.util.Log;

import com.example.weather4cast.model.WeatherResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileManager {
    public static WeatherResponse getWeatherResponse(Context context){
        Gson gson = new Gson();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return gson.fromJson(stringBuilder.toString(), WeatherResponse.class);
    }

    public static void StoreWeatherResponse(WeatherResponse weatherResponse, Context context){
        try {
            Gson gson = new Gson();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("WeatherResponse.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(gson.toJson(weatherResponse));
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
