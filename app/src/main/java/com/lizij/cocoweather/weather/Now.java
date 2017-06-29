package com.lizij.cocoweather.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Now {
    @SerializedName("cond")
    public More more;
    public class More {
        @SerializedName("txt")
        public String info;

        public String code;

        @Override
        public String toString() {
            return "More{" +
                    "info='" + info + '\'' +
                    ", code='" + code + '\'' +
                    '}';
        }
    }

    public String fl;
    public String hum;
    public String pcpn;
    public String pres;

    @SerializedName("tmp")
    public String temperature;

    public String vis;

    public Wind wind;
    public class Wind{
        public String deg;
        public String dir;
        public String sc;
        public String spd;

        @Override
        public String toString() {
            return "Wind{" +
                    "deg='" + deg + '\'' +
                    ", dir='" + dir + '\'' +
                    ", sc='" + sc + '\'' +
                    ", spd='" + spd + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Now{" +
                "more=" + more +
                ", fl='" + fl + '\'' +
                ", hum='" + hum + '\'' +
                ", pcpn='" + pcpn + '\'' +
                ", pres='" + pres + '\'' +
                ", temperature='" + temperature + '\'' +
                ", vis='" + vis + '\'' +
                ", wind=" + wind +
                '}';
    }
}
