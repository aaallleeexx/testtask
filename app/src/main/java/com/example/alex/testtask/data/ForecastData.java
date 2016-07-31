package com.example.alex.testtask.data;

import com.example.alex.testtask.utils.CustomUtils;
import com.example.alex.testtask.web.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForecastData {
    private long dt;
    private double tempMin, tempMax;
    private String mainWeather, description, weatherIcon;
    private ArrayList<ForecastData> dataList = new ArrayList<>();
    private static ForecastData forecastData;

    public static ForecastData newInstance() {
        if (forecastData == null) {
            return new ForecastData();
        }
        return forecastData;
    }

    public ForecastData() {

    }

    public ForecastData(JSONObject obj) {
        try {
            this.dt = obj.getLong("dt");

            JSONObject main = obj.getJSONObject("temp");
            this.tempMin = main.getDouble("min");
            this.tempMax = main.getDouble("max");

            JSONArray weather = obj.getJSONArray("weather");
            for (int k = 0; k < weather.length(); k++) {
                JSONObject weatherObj = weather.getJSONObject(k);

                this.mainWeather = weatherObj.getString("main");
                this.description = weatherObj.getString("description");
                this.weatherIcon = Web.ICON_URL + weatherObj.getString("icon");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ForecastData> getForecastDataList(JSONObject object) {
        try {
            JSONArray list = object.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                dataList.add(new ForecastData(list.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public String getFormattedDate() {
        return CustomUtils.getMMMDate(dt);
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getFormattedMinTemp() {
        return String.valueOf((int) (tempMin - 273.15)) + "°C";
    }

    public String getFormattedMaxTemp() {
        return String.valueOf((int) (tempMax - 273.15)) + "°C";
    }
}
