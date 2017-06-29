package com.lizij.cocoweather;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lizij.cocoweather.util.HttpUtil;
import com.lizij.cocoweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String CITY_LIST_ADDRESS = MyApplication.getProperties().getProperty("CITY_LIST_ADDRESS");
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("正在更新城市列表");
        progressDialog.setMessage("请耐心等待");
        progressDialog.onStart();
        progressDialog.show();
        getCityList();
    }

    private void getCityList(){
        HttpUtil.sendOkHttpRequest(CITY_LIST_ADDRESS, new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Utility.handleCityListResponse(response.body().string());
                Utility.checkDatabase();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

}
