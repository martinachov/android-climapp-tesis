package ar.com.tesina.climapp.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.HttpURLConnection;

import ar.com.tesina.climapp.MainActivity;

public class OpenWeatherJsonUtils {

    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {


        final String OWM_LIST = "list";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";
        final String OWM_WEATHER = "weather";
        final String OWM_DESCRIPTION = "main";
        final String OWM_MESSAGE_CODE = "cod";

        /* Info diaria */
        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = forecastJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server DOWN */
                    return null;
            }
        }

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
        parsedWeatherData = new String[weatherArray.length()];

        long localDate = System.currentTimeMillis();
        long utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate);
        long startDay = SunshineDateUtils.normalizeDate(utcDate);

        for (int i = 0; i < weatherArray.length(); i++) {
            String date;
            String highAndLow;

            long dateTimeMillis;
            double high;
            double low;
            String description;

            /* JSON OBJECT que representa el dia */
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            /*
             * asumimos que los valores se devuelven en orden por día
             */
            dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i;
            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false);

            //WEATHER
            JSONObject weatherObject =
                    dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            //MAIN
            JSONObject mainObject = dayForecast.getJSONObject(OWM_DESCRIPTION);
            high = mainObject.getDouble(OWM_MAX);
            low = mainObject.getDouble(OWM_MIN);
            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low);

            parsedWeatherData[i] = date + " - " + description + " - " + highAndLow;
        }

        return parsedWeatherData;
    }

}
