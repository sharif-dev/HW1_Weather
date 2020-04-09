package com.example.weather4cast.networking;

import com.android.volley.VolleyError;
import com.example.weather4cast.model.WeatherResponse;

public class WeatherAPI {
    public interface ICallback {
        void onWeatherResponse(WeatherResponse ws);

        void onWeatherError(VolleyError error);
    }

    private static String urlTemplate =
            "http://api.weatherapi.com/v1/forecast.json?q=%s,%s51.67917&key=401aacef9f19431795c94313200804&days=7";

    public static void getWeatherFromApi(String lat, String lon, ICallback callback) {
        new Thread(() -> {
            VSReqQ.getInstance().add(new GsonRequest<>(String.format(urlTemplate, lat, lon),
                    WeatherResponse.class, callback::onWeatherResponse, callback::onWeatherError));
        }).start();
    }
}
