package com.example.weather4cast.model;

import com.google.gson.annotations.SerializedName;

public class Day {
    public static class WeatherCondition{
        @SerializedName("text")
        public String weatherVerdict;

        @SerializedName("icon")
        public String verdictIconUrl;
    }

    @SerializedName("maxtemp_c")
    public int maximumTemp;

    @SerializedName("mintemp_c")
    public int minimumTemp;

    @SerializedName("avgtemp_c")
    public int averageTemp;

    @SerializedName("maxwind_kpg")
    public int maxWindSpeed;

    @SerializedName("avghumidity")
    public int averageHumidity;

    @SerializedName("condition")
    public WeatherCondition weatherCondition;
}
