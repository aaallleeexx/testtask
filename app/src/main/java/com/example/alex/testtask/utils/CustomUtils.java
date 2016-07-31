package com.example.alex.testtask.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;

public class CustomUtils {
    public static String getDate(long time) {
        return getDate("yyyy.MMM.d H:mm", time);
    }

    public static String getMMMDate(long time) {
        return getDate("MMM.d", time);
    }

    public static String getMMMHmmDate(long time) {
        return getDate("MMM.d H:mm", time);
    }

    public static String getDate(String format, long time) {
        return new SimpleDateFormat(format).format(time * 1000);
    }

    public static String formatBearing(double bearing) {
        if (bearing < 0 && bearing > -180) {
            bearing = 360.0 + bearing;
        }
        if (bearing > 360 || bearing < -180) {
            return "Unknown";
        }

        String directions[] = {
                "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW",
                "N"};
        return directions[(int) Math.floor(((bearing + 11.25) % 360) / 22.5)];
    }

    public static boolean hasConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
