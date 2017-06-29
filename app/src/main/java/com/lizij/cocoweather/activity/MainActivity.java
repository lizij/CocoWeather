package com.lizij.cocoweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lizij.cocoweather.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String countyCode = sharedPreferences.getString("county_code", null);
        if ( countyCode != null){
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("county_code", countyCode);
            startActivity(intent);
            finish();
        }
    }

}
