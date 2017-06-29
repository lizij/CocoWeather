package com.lizij.cocoweather.util;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.lizij.cocoweather.MyApplication;
import com.lizij.cocoweather.db.City;
import com.lizij.cocoweather.db.County;
import com.lizij.cocoweather.db.Province;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lizij on 2017/6/29.
 */

public class Utility {
    private static final String TAG = "Utility";

    public static boolean handleCityListResponse(String response){
        HashMap<String, Integer> provinces = new HashMap<>();
        HashMap<String, Integer> cities = new HashMap<>();
        HashMap<String, String> counties = new HashMap<>();

        int provinceCode = 1;
        int cityCode = 1;

        try{
            DataSupport.deleteAll(Province.class);
            DataSupport.deleteAll(City.class);
            DataSupport.deleteAll(County.class);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }


        if (!TextUtils.isEmpty(response)){
            try{
                String[] lines = response.split("\n");
                Pattern pattern = Pattern.compile("(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)\\t(.*)");
                for (String line: lines) {
                    Matcher matcher = pattern.matcher(line);
                    if (!matcher.find()) continue;

                    String weatherId = matcher.group(1);
                    String countyName = matcher.group(3);
                    String provinceName = matcher.group(8);
                    String cityName = matcher.group(10);

                    if (!provinces.containsKey(provinceName)){
                        provinces.put(provinceName, provinceCode);
                        Province province = new Province();
                        province.setProvinceName(provinceName);
                        province.setProvinceCode(provinceCode);
                        province.save();
                        provinceCode++;
                    }

                    if (!cities.containsKey(cityName)){
                        cities.put(cityName, cityCode);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setProvinceCode(provinces.get(provinceName));
                        city.save();
                        cityCode++;
                    }

                    if (!counties.containsKey(countyName)){
                        counties.put(countyName, weatherId);
                        County county = new County();
                        county.setCountyName(countyName);
                        county.setCityCode(cities.get(cityName));
                        county.setWeatherId(weatherId);
                        county.save();
                    }

//                Log.d(TAG, "handleCityListResponse: " + weatherId + " " +  countyName + " " + cityName + " " + provinceName + " " + countryName);
                }
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void checkDatabase(){
        List<Province> provinces = DataSupport.findAll(Province.class);
        List<City> cities = DataSupport.findAll(City.class);
        List<County> counties = DataSupport.findAll(County.class);

        for (Province province : provinces){
            Log.d(TAG, "checkDatabase: " + province.toString());
        }

        for (City city : cities){
            Log.d(TAG, "checkDatabase: " + city.toString());
        }

        for (County county : counties){
            Log.d(TAG, "checkDatabase: " + county.toString());
        }
    }
}
