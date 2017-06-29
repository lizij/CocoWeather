package com.lizij.cocoweather.weather;

/**
 * Created by Lizij on 2017/6/29.
 */

public class HourlyForecast {
    public Cond cond;
    public class Cond{
        public String code;
        public String txt;

        @Override
        public String toString() {
            return "Cond{" +
                    "code='" + code + '\'' +
                    ", txt='" + txt + '\'' +
                    '}';
        }
    }

    public String date;
    public String hum;
    public String pop;
    public String pres;
    public String tmp;

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
        return "HourlyForecast{" +
                "cond=" + cond +
                ", date='" + date + '\'' +
                ", hum='" + hum + '\'' +
                ", pop='" + pop + '\'' +
                ", pres='" + pres + '\'' +
                ", tmp='" + tmp + '\'' +
                ", wind=" + wind +
                '}';
    }
}
