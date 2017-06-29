package com.lizij.cocoweather.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lizij.cocoweather.R;
import com.lizij.cocoweather.application.MyApplication;
import com.lizij.cocoweather.util.HttpUtil;
import com.lizij.cocoweather.util.Utility;
import com.lizij.cocoweather.weather.DailyForecast;
import com.lizij.cocoweather.weather.Weather;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

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

    private String countyCode;
    private String timeToday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

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

        countyCode = getIntent().getStringExtra("county_code");
        timeToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = sharedPreferences.getString(countyCode + "_weather_" + timeToday, null);
        if (!TextUtils.isEmpty(weatherString)){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }else{
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(countyCode);
        }
    }

    public void requestWeather(final String countyCode){
        String API_ADDRESS = MyApplication.getProperties().getProperty("API_ADDRESS");
        String API_KEY = MyApplication.getProperties().getProperty("API_KEY");
        String requestAddress = API_ADDRESS + "weather?city=" + countyCode + "&key=" + API_KEY;

        Log.d(TAG, "requestWeather: " + requestAddress);

        HttpUtil.sendOkHttpRequest(requestAddress, new okhttp3.Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_LONG).show();
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
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString(countyCode + "_weather_" + timeToday, responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取天气失败", Toast.LENGTH_LONG).show();
                        }
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

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
