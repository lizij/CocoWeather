package com.lizij.cocoweather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.litepal.LitePalApplication;
import org.litepal.util.LogUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Lizij on 2017/6/29.
 */

public class MyApplication extends Application {
    private static Context context;
    private static Properties properties;
    private static final String TAG = "MyApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        try{
            properties = new Properties();
            InputStream is = context.getAssets().open("config.properties");
            properties.load(is);
            is.close();
            LogUtil.d(TAG, properties.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        LitePalApplication.initialize(context);
    }

    public static Context getContext(){
        return context;
    }

    public static Properties getProperties(){
        return properties;
    }

}
