package com.lizij.cocoweather.weather;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Alarm {
    public String level;

    public String stat;

    public String title;

    public String txt;

    public String type;

    @Override
    public String toString() {
        return "Alarm{" +
                "level='" + level + '\'' +
                ", stat='" + stat + '\'' +
                ", title='" + title + '\'' +
                ", txt='" + txt + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
