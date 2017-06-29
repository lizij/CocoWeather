package com.lizij.cocoweather.weather;

import com.bumptech.glide.load.resource.SimpleResource;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("cnty")
    public String countryName;

    public String id;

    @SerializedName("lat")
    public String latitude;

    @SerializedName("lon")
    public String longitude;

    @SerializedName("prov")
    public String provinceName;

    public Update update;
    public class Update{
        @SerializedName("loc")
        public String localTime;

        @SerializedName("utc")
        public String utcTime;

        @Override
        public String toString() {
            return "Update{" +
                    "localTime='" + localTime + '\'' +
                    ", utcTime='" + utcTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Basic{" +
                "cityName='" + cityName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", update=" + update +
                '}';
    }
}
