package ar.com.tesina.climapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import ar.com.tesina.climapp.R;

public class SunshinePreferences {

    private static final String DEFAULT_WEATHER_LOCATION = "La Plata,AR";
    //private static final double[] DEFAULT_WEATHER_COORDINATES = {34.9215, 57.9546};



    /**
     * Devuelve la ubicación actualmente establecida en Preferencias.
     * La ubicación predeterminada que devolverá este método es la de La Plata "3432043"
     *
     * @param context para recuperar SharedPreferences
     * @return Location
     */
    public static String getPreferredWeatherLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return prefs.getString(keyForLocation, defaultLocation);
    }

    /**
     *
     * @return
     */
    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION;
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean isMetric(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForUnits = context.getString(R.string.pref_units_key);
        String defaultUnits = context.getString(R.string.pref_units_metric);
        String preferredUnits = prefs.getString(keyForUnits, defaultUnits);
        String metric = context.getString(R.string.pref_units_metric);
        boolean userPrefersMetric;
        if (metric.equals(preferredUnits)) {
            userPrefersMetric = true;
        } else {
            userPrefersMetric = false;
        }
        return userPrefersMetric;
    }
}
