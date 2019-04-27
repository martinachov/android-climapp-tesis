package ar.com.tesina.climapp.data;

import android.content.Context;

public class SunshinePreferences {

    private static final String DEFAULT_WEATHER_LOCATION = "3432043";
    //private static final double[] DEFAULT_WEATHER_COORDINATES = {34.9215, 57.9546};



    /**
     * Devuelve la ubicación actualmente establecida en Preferencias.
     * La ubicación predeterminada que devolverá este método es "3432043"
     *
     * @param context para recuperar SharedPreferences
     * @return Location
     */
    public static String getPreferredWeatherLocation(Context context) {
        return getDefaultWeatherLocation();
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_WEATHER_LOCATION;
    }

    public static boolean isMetric(Context context) {
        return true;
    }
}
