package com.example.alex.testtask.web;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Web {
    private final static String CURRENT_WEATER_DATA_FOR_LOCATION = "http://api.openweathermap.org/data/2.5/weather?q=";
    private final static String FORECAST_DATA_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private final static String API = "&APPID=12a649a36571d5278d59d903ede2520b";
    private final static String FORECAST_DATA_COUNT = "&cnt=5";
    public final static String ICON_URL = "http://api.openweathermap.org/img/w/";

    private OkHttpClient mClient = new OkHttpClient();
    public static Web web;

    public static Web newInstance() {
        if (web == null) {
            return new Web();
        }
        return web;
    }

    JSONObject run(String url) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mClient.newCall(request).execute();
        Log.d("dbg", response.body().toString());
        return new JSONObject(response.body().string());
    }

    public JSONObject getWeatherByName(String cityName) {
        JSONObject result;

        try {
            result = run(CURRENT_WEATER_DATA_FOR_LOCATION + cityName + FORECAST_DATA_COUNT + API);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

        return result;
    }

    public JSONObject getForecastData(String cityName) {
        JSONObject result;

        try {
            result = run(FORECAST_DATA_URL + cityName + API);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

        return result;
    }
}
