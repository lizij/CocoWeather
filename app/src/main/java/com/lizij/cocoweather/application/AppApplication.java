package com.lizij.cocoweather.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;
import org.litepal.LitePalApplication;
import org.litepal.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Lizij on 2017/6/29.
 */

public class AppApplication extends Application {
    private static Context context;
    private static Properties properties;
    private static final String TAG = "AppApplication";
    private String DATABASE_NAME = "coco_weather.db";
    private String JOURNAL_NAME = "coco_weather.db-journal";
    private String DATABASE_PATH = "/data/data/com.lizij.cocoweather/databases/";

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
        initiateDataBase();
    }

    public static Context getContext(){
        return context;
    }

    public static Properties getProperties(){
        return properties;
    }

    public static String getWeatherApi(String countyCode){
        String API_ADDRESS = AppApplication.getProperties().getProperty("API_ADDRESS");
        String API_KEY = AppApplication.getProperties().getProperty("API_KEY");
        String requestAddress = API_ADDRESS + "weather?city=" + countyCode + "&key=" + API_KEY;
        return requestAddress;
    }

    public void initiateDataBase(){
        if (!checkDatabase()){
            copyDataBase();
        }
    }

    public boolean checkDatabase(){
        SQLiteDatabase checkDB = null;
        try{
            String databaseFileName = DATABASE_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(databaseFileName, null, SQLiteDatabase.OPEN_READONLY);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    public void copyDataBase(){
        File dir = new File(DATABASE_PATH);
        if (!dir.exists()){
            dir.mkdir();
        }
        copyFile(DATABASE_NAME, DATABASE_PATH + DATABASE_NAME);
        copyFile(JOURNAL_NAME, DATABASE_PATH + JOURNAL_NAME);
    }

    private void copyFile(String fileName, String destPath){
        FileOutputStream fileOutputStream;
        InputStream inputStream;
        try{
            fileOutputStream = new FileOutputStream(destPath);
            inputStream = getAssets().open(fileName);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0){
                fileOutputStream.write(buffer, 0, count);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
