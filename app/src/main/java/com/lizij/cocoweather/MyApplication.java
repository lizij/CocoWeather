package com.lizij.cocoweather;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by Lizij on 2017/6/29.
 */

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }

    public static Context getContext(){
        return context;
    }

}
