package com.lizij.cocoweather.weather;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Aqi {
    public City city;
    public class City{
        public String aqi;
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String qlty;
        public String so2;

        @Override
        public String toString() {
            return "City{" +
                    "aqi='" + aqi + '\'' +
                    ", co='" + co + '\'' +
                    ", no2='" + no2 + '\'' +
                    ", o3='" + o3 + '\'' +
                    ", pm10='" + pm10 + '\'' +
                    ", pm25='" + pm25 + '\'' +
                    ", qlty='" + qlty + '\'' +
                    ", so2='" + so2 + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Aqi{" +
                "city=" + city +
                '}';
    }
}
