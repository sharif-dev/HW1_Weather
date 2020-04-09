package com.example.weather4cast.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    public static class LocationC{
        @SerializedName("name")
        public String cityName;
    }

    public static class ForecastC{
        @SerializedName("forecastday")
        public List<Day> days;
    }

    @SerializedName("location")
    public LocationC cityNameC;

    @SerializedName("forecast")
    public ForecastC daysC;
}
