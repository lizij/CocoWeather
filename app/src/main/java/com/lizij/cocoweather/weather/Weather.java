package com.lizij.cocoweather.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Weather {
    public List<Alarm> alarms;
    public Aqi aqi;
    public Basic basic;
    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastList;
    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecastList;
    public Now now;
    public String status;
    public Suggestion suggestion;

    @Override
    public String toString() {
        return "Weather{" +
                "alarms=" + alarms +
                ", aqi=" + aqi +
                ", basic=" + basic +
                ", dailyForecastList=" + dailyForecastList +
                ", hourlyForecastList=" + hourlyForecastList +
                ", now=" + now +
                ", status='" + status + '\'' +
                ", suggestion=" + suggestion +
                '}';
    }
}
