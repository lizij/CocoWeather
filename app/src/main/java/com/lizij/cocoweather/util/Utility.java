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
        HashMap<String, Integer> provinceHashMap = new HashMap<>();
        HashMap<String, Integer> cityHashMap = new HashMap<>();
        HashMap<String, String> countyHashMap = new HashMap<>();

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
                    if (!line.startsWith("CN")) continue;
                    Matcher matcher = pattern.matcher(line);
                    if (!matcher.find()) continue;

                    String countyCode = matcher.group(1);
                    String countyName = matcher.group(3);
                    String provinceName = matcher.group(8);
                    String cityName = matcher.group(10);

                    if (!provinceHashMap.containsKey(provinceName)){
                        provinceHashMap.put(provinceName, provinceCode);
                        Province province = new Province();
                        province.setProvinceName(provinceName);
                        province.setProvinceCode(provinceCode);
                        province.save();
                        provinceCode++;
                    }

                    if (!cityHashMap.containsKey(cityName)){
                        cityHashMap.put(cityName, cityCode);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setCityCode(cityCode);
                        city.setProvinceCode(provinceHashMap.get(provinceName));
                        city.save();
                        cityCode++;
                    }

                    if (!countyHashMap.containsKey(countyName)){
                        countyHashMap.put(countyName, countyCode);
                        County county = new County();
                        county.setCountyName(countyName);
                        county.setCountyCode(countyCode);
                        county.setCityCode(cityHashMap.get(cityName));
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
        List<Province> provinceList = DataSupport.findAll(Province.class);
        List<City> cityList = DataSupport.findAll(City.class);
        List<County> countyList = DataSupport.findAll(County.class);

        for (Province province : provinceList){
            Log.d(TAG, "checkDatabase: " + province.toString());
        }

        for (City city : cityList){
            Log.d(TAG, "checkDatabase: " + city.toString());
        }

        for (County county : countyList){
            Log.d(TAG, "checkDatabase: " + county.toString());
        }
    }
}
