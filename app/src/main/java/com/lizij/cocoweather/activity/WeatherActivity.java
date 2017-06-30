package com.lizij.cocoweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lizij.cocoweather.R;
import com.lizij.cocoweather.application.AppApplication;
import com.lizij.cocoweather.service.AutoUpdateService;
import com.lizij.cocoweather.util.HttpUtil;
import com.lizij.cocoweather.util.Utility;
import com.lizij.cocoweather.weather.DailyForecast;
import com.lizij.cocoweather.weather.Weather;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    private ImageView imageView;
    public SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private Button navButton;
    public DrawerLayout drawerLayout;

    private String countyCode;
    private String cacheName;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        imageView = (ImageView) findViewById(R.id.bing_pic_img);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        navButton = (Button) findViewById(R.id.nav_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();

        String bingPic = sharedPreferences.getString("bing_pic", null);
        if (bingPic != null){
            Glide.with(this).load(bingPic).into(imageView);
        }else {
            loadBingPic();
        }

//        countyCode = getIntent().getStringExtra("county_code");
        countyCode = sharedPreferences.getString("county_code", null);
        if (TextUtils.isEmpty(countyCode)){
            drawerLayout.openDrawer(GravityCompat.START);
        }

        String timeToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        cacheName = countyCode + "_weather_" + timeToday;
        String weatherString = sharedPreferences.getString(cacheName, null);
        if (!TextUtils.isEmpty(weatherString)){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(countyCode);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(countyCode);
            }
        });

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void requestWeather(final String countyCode){
        if (TextUtils.isEmpty(countyCode)) return;
        String requestAddress = AppApplication.getWeatherApi(countyCode);

        Log.d(TAG, "requestWeather: " + requestAddress);

        HttpUtil.sendOkHttpRequest(requestAddress, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            editor.putString(cacheName, responseText);
                            editor.putString("county_code", countyCode);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_LONG).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    public void showWeatherInfo(Weather weather){
        Log.d(TAG, "showWeatherInfo: " + weather.toString());
        titleCity.setText(weather.basic.cityName);
        titleUpdateTime.setText(weather.basic.update.localTime.split(" ")[1]);
        degreeText.setText(weather.now.temperature + "℃");
        weatherInfoText.setText(weather.now.more.info);

        forecastLayout.removeAllViews();
        for (DailyForecast dailyForecast : weather.dailyForecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = ((TextView) view.findViewById(R.id.date_text));
            dateText.setText(dailyForecast.date);
            TextView infoText = ((TextView) view.findViewById(R.id.info_text));
            infoText.setText(dailyForecast.cond.txt_d);
            TextView maxText = ((TextView) view.findViewById(R.id.max_text));
            maxText.setText(dailyForecast.tmp.max);
            TextView minText = ((TextView) view.findViewById(R.id.min_text));
            minText.setText("-" + dailyForecast.tmp.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        comfortText.setText("舒适度:" + weather.suggestion.comfort.info);
        carWashText.setText("洗车建议" + weather.suggestion.carWash.info);
        sportText.setText("运动建议:" + weather.suggestion.sport.info);
        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void loadBingPic(){
        String requestAddress = AppApplication.getProperties().getProperty("BING_API");
        HttpUtil.sendOkHttpRequest(requestAddress, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "背景图片加载失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    JSONObject bingJson = new JSONObject(response.body().string());
                    com.alibaba.fastjson.JSONObject bj = com.alibaba.fastjson.JSONObject.parseObject(response.body().string());
                    final String picUrl = bj.getJSONArray("images").getJSONObject(0).getString("url");
                    final String url = "http://www.bing.com/" + bingJson.getJSONArray("images").getJSONObject(0).getString("url");
                    Log.d(TAG, "onResponse: " + picUrl);

                    editor.putString("bing_pic", url);
                    editor.apply();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(WeatherActivity.this).load(url).into(imageView);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
