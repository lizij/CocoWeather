package com.lizij.cocoweather.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Suggestion {
    @SerializedName("comf")
    public Info comfort;

    @SerializedName("cw")
    public Info carWash;

    @SerializedName("drsg")
    public Info dressSuggestion;

    public Info flu;
    public Info sport;
    public Info trav;
    public Info uv;

    public class Info{
        public String brf;
        @SerializedName("txt")
        public String info;

        @Override
        public String toString() {
            return "Info{" +
                    "brf='" + brf + '\'' +
                    ", info='" + info + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "comfort=" + comfort +
                ", carWash=" + carWash +
                ", dressSuggestion=" + dressSuggestion +
                ", flu=" + flu +
                ", sport=" + sport +
                ", trav=" + trav +
                ", uv=" + uv +
                '}';
    }
}
