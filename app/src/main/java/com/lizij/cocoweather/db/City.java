package com.lizij.cocoweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Lizij on 2017/6/29.
 */

public class City extends DataSupport {
    private int id;
    private String cityName;
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + cityName + '\'' +
                ", provinceCode=" + provinceCode +
                '}';
    }
}
