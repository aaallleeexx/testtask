package com.example.alex.testtask.data;

import android.util.Log;

import com.example.alex.testtask.exceptions.CityNotFoundException;
import com.example.alex.testtask.web.Web;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import io.realm.RealmObject;

/*

Parameters:

city
city.id City ID
city.cityName City cityName
city.coord
city.coord.lon City geo location, longitude
city.coord.lat City geo location, latitude
city.country Country code (GB, JP etc.)
city.sun
city.sun.rise Sunrise time
city.sun.set Sunset time
temperature
temperature.value Temperature
temperature.min Minimum temperature at the moment of calculation. This is deviation from 'temp' that is possible for large cities and megalopolises geographically expanded (use these parameter optionally).
temperature.max Maximum temperature at the moment of calculation. This is deviation from 'temp' that is possible for large cities and megalopolises geographically expanded (use these parameter optionally).
temperature.unit Unit of measurements. Possilbe valure is Celsius, Kelvin, Fahrenheit.
humidity
humidity.value Humidity value
humidity.unit %
pressure
pressure.value Pressure value
pressure.unit hPa
wind
wind.speed
wind.speed.value Wind speed, mps
wind.speed.cityName Type of the wind
wind.direction
wind.direction.value Wind direction, degrees (meteorological)
wind.direction.code Code of the wind direction. Possilbe value is WSW, N, S etc.
wind.direction.cityName Full cityName of the wind direction.
clouds
clouds.value Cloudiness
clouds.cityName Name of the cloudiness
visibility
visibility.value Visibility, meter
precipitation
precipitation.value Precipitation, mm
precipitation.mode Possible values are 'no", cityName of weather phenomena as 'rain', 'snow'
weather
weather.number Weather condition id
weather.value Weather condition cityName
weather.icon Weather icon id
lastupdate
lastupdate.value Last time when data was updated
 */

public class CityInfo extends RealmObject implements Serializable {

    private int cityId;
    private String cityName;
    private double cityLon, cityLan;
    private int cityCountryCode;
    private int citySunrise;
    private int citySunset;
    private int cityWindSpeed;
    private int cityWindDeg;
    private int cityPressure;
    private int cityHumidity;
    private double temperature;
    private String weatherIcon;
    private String cityMainWeather;
    private int date;
    private String country;
    private String cityDescr;

    public CityInfo() {

    }

    public CityInfo(JSONObject json) throws CityNotFoundException {
        try {
            this.cityName = json.getString("name");
            this.cityId = json.getInt("id");
            this.cityCountryCode = json.getInt("cod");
            this.date = json.getInt("dt");

            JSONObject main = json.getJSONObject("main");
            this.temperature = main.getDouble("temp");
            this.cityPressure = main.getInt("pressure");
            this.cityHumidity = main.getInt("humidity");

            JSONArray weather = json.getJSONArray("weather");
            for (int i = 0; i < weather.length(); i++) {
                JSONObject weatherJSONObject = weather.getJSONObject(i);
                this.weatherIcon = Web.ICON_URL + weatherJSONObject.getString("icon");
                this.cityMainWeather = weatherJSONObject.getString("main");
                this.cityDescr = weatherJSONObject.getString("description");
            }

            JSONObject wind = json.getJSONObject("wind");
            this.cityWindSpeed = wind.getInt("speed");
            this.cityWindDeg = wind.getInt("deg");

            JSONObject sys = json.getJSONObject("sys");
            this.citySunrise = sys.getInt("sunrise");
            this.citySunset = sys.getInt("sunset");
            this.country = sys.getString("country");

            JSONObject coord = json.getJSONObject("coord");
            this.cityLon = coord.getDouble("lon");
            this.cityLan = coord.getDouble("lat");
        } catch (JSONException e) {
            e.printStackTrace();
            checkError(json);
        }
    }

    private void checkError(JSONObject json) throws CityNotFoundException {
        try {
            if (json.getString("message").equals("Error: Not found city")) {
                throw new CityNotFoundException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCityName() {
        return cityName;
    }

    public CityInfo updateData(JSONObject json) {
        try {
            this.date = json.getInt("dt");

            JSONObject main = json.getJSONObject("main");
            this.temperature = main.getDouble("temp");
            this.cityPressure = main.getInt("pressure");
            this.cityHumidity = main.getInt("humidity");

            JSONArray weather = json.getJSONArray("weather");
            for (int i = 0; i < weather.length(); i++) {
                JSONObject weatherJSONObject = weather.getJSONObject(i);
                this.weatherIcon = Web.ICON_URL + weatherJSONObject.getString("icon");
                this.cityMainWeather = weatherJSONObject.getString("main");
                this.cityDescr = weatherJSONObject.getString("description");
            }

            JSONObject wind = json.getJSONObject("wind");
            this.cityWindSpeed = wind.getInt("speed");
            this.cityWindDeg = wind.getInt("deg");

            JSONObject sys = json.getJSONObject("sys");
            this.citySunrise = sys.getInt("sunrise");
            this.citySunset = sys.getInt("sunset");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return this;
    }

    public String getFormattedTemp() {
        return String.valueOf((int) (temperature - 273.15)) + "°C";
    }

    public String getWeatherIconURL() {
        return weatherIcon;
    }

    public int getCityId() {
        return cityId;
    }

    public double getCityLon() {
        return cityLon;
    }

    public String getCityDescr() {
        return cityDescr;
    }

    public String getFormattedPos() {
        return "[" + cityLon + ", " + cityLan + "]";
    }

    public double getCityLan() {
        return cityLan;
    }

    public int getCityCountryCode() {
        return cityCountryCode;
    }

    public long getCitySunrise() {
        return citySunrise;
    }

    public long getCitySunset() {
        return citySunset;
    }

    public double getCityWindSpeed() {
        return cityWindSpeed;
    }

    public String getFormattedWindSpeed() {
        return cityWindSpeed + "m/s";
    }

    public double getCityWindDeg() {
        return cityWindDeg;
    }

    public int getCityPressure() {
        return cityPressure;
    }

    public String getFormattedPressure() {
        return cityPressure + "hpa";
    }

    public int getCityHumidity() {
        return cityHumidity;
    }

    public String getFormattedHuidity() {
        return cityHumidity + " %";
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public String getCityMainWeather() {
        return cityMainWeather;
    }

    public int getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }
}
