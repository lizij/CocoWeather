package com.lizij.cocoweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lizij.cocoweather.application.AppApplication;
import com.lizij.cocoweather.util.HttpUtil;
import com.lizij.cocoweather.util.Utility;
import com.lizij.cocoweather.weather.Weather;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class AutoUpdateService extends Service {
    private static final String TAG = "AutoUpdateService";
    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        updateCityList();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int interval = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + interval;
        Intent intent1 = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent1, 0);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String countyCode = sharedPreferences.getString("county_code", null);
        String timeToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String cacheName = countyCode + "_weather_" + timeToday;
        if (countyCode != null){
            String requestAddress = AppApplication.getWeatherApi(countyCode);
            HttpUtil.sendOkHttpRequest(requestAddress, new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                            editor.putString(cacheName, responseText);
                            editor.putString("county_code", countyCode);
                            editor.apply();
                        }
                }
            });
        }
    }

    private void updateBingPic(){
        String requestAddress = AppApplication.getProperties().getProperty("BING_API");
        HttpUtil.sendOkHttpRequest(requestAddress, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    JSONObject bingJson = new JSONObject(response.body().string());
                    final String url = "http://www.bing.com/" + bingJson.getJSONArray("images").getJSONObject(0).getString("url");
                    Log.d(TAG, "onResponse: " + url);
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                    editor.putString("bing_pic", url);
                    editor.apply();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateCityList(){
        String CITY_LIST_ADDRESS = AppApplication.getProperties().getProperty("CITY_LIST_ADDRESS");
        HttpUtil.sendOkHttpRequest(CITY_LIST_ADDRESS, new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(Utility.handleCityListResponse(response.body().string())){
                    Utility.checkDatabase();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
