package com.lizij.cocoweather.weather;

/**
 * Created by Lizij on 2017/6/29.
 */

public class DailyForecast {
    public Astro astro;
    public class Astro{
        public String mr;
        public String ms;
        public String sr;
        public String ss;

        @Override
        public String toString() {
            return "Astro{" +
                    "mr='" + mr + '\'' +
                    ", ms='" + ms + '\'' +
                    ", sr='" + sr + '\'' +
                    ", ss='" + ss + '\'' +
                    '}';
        }
    }

    public Cond cond;
    public class Cond{
        public String code_d;
        public String code_n;
        public String txt_d;
        public String txt_n;

        @Override
        public String toString() {
            return "Cond{" +
                    "code_d='" + code_d + '\'' +
                    ", code_n='" + code_n + '\'' +
                    ", txt_d='" + txt_d + '\'' +
                    ", txt_n='" + txt_n + '\'' +
                    '}';
        }
    }

    public String date;
    public String hum;
    public String pcpn;
    public String pop;
    public String pres;

    public Tmp tmp;
    public class Tmp{
        public String max;
        public String min;

        @Override
        public String toString() {
            return "Tmp{" +
                    "max='" + max + '\'' +
                    ", min='" + min + '\'' +
                    '}';
        }
    }

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
        return "DailyForecast{" +
                "astro=" + astro +
                ", cond=" + cond +
                ", date='" + date + '\'' +
                ", hum='" + hum + '\'' +
                ", pcpn='" + pcpn + '\'' +
                ", pop='" + pop + '\'' +
                ", pres='" + pres + '\'' +
                ", tmp=" + tmp +
                ", vis='" + vis + '\'' +
                ", wind=" + wind +
                '}';
    }
}
