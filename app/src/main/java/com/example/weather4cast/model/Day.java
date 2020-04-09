package com.example.weather4cast.model;

import com.google.gson.annotations.SerializedName;

public class Day {
    public static class WeatherCondition{
        @SerializedName("text")
        public String weatherVerdict;

        @SerializedName("icon")
        public String verdictIconUrl;
    }

    public static class DayDetail{
        @SerializedName("maxtemp_c")
        public double maximumTemp;

        @SerializedName("mintemp_c")
        public double minimumTemp;

        @SerializedName("avgtemp_c")
        public double averageTemp;

        @SerializedName("maxwind_kpg")
        public double maxWindSpeed;

        @SerializedName("avghumidity")
        public double averageHumidity;

        @SerializedName("condition")
        public WeatherCondition weatherCondition;
    }

    @SerializedName("date")
    public String date;

    @SerializedName("day")
    public DayDetail dayDetail;

}
