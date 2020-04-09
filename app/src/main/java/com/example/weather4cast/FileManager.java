package com.example.weather4cast;

import android.content.Context;
import android.util.Log;

import com.example.weather4cast.model.WeatherResponse;
import com.example.weather4cast.networking.WeatherAPI;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileManager {
    public interface IGetWeatherResponseCB{
        void onSuccess(WeatherResponse ws);
        void onError();
    }
    public static void getWeatherResponse(Context context,IGetWeatherResponseCB callback){
        new Thread(() -> {
            Gson gson = new Gson();
            StringBuilder stringBuilder = new StringBuilder();

            try(InputStream inputStream = context.openFileInput("WeatherResponse.txt");) {
                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append("\n").append(receiveString);
                    }
                }
            }
            catch (FileNotFoundException e) {
                Log.e("tag", "File not found: " + e.toString());
                callback.onError();
                return;
            } catch (IOException e) {
                Log.e("tag", "Can not read file: " + e.toString());
                callback.onError();
                return;
            }

            callback.onSuccess(gson.fromJson(stringBuilder.toString(), WeatherResponse.class));
        }).start();
    }


    public static void StoreWeatherResponse(WeatherResponse weatherResponse, Context context){
        new Thread(() -> {
            try {
                Gson gson = new Gson();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("WeatherResponse.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(gson.toJson(weatherResponse));
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
