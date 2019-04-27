package ar.com.tesina.climapp.utilities;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import ar.com.tesina.climapp.R;

class SunshineDateUtils {

    public static final long SECOND_IN_MILLIS = 1000;
    public static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    public static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    public static final long DAY_IN_MILLIS = HOUR_IN_MILLIS * 24;

    /**
     * Devuelve numero de dias desde "January 01, 1970, 12:00 Midnight UTC" hasta ahora
     *
     * @param date
     *
     * @return numero de dias (UTC)
     */
    public static long getDayNumber(long date) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(date);
        return (date + gmtOffset) / DAY_IN_MILLIS;
    }

    /**
     * Normalizamos todas las fechas en hora UTC.
     *
     * @param date
     *
     * @return
     */
    public static long normalizeDate(long date) {
        // Normaliza la fecha de inicio al comienzo del día (UTC) en la hora local
        long retValNew = date / DAY_IN_MILLIS * DAY_IN_MILLIS;
        return retValNew;
    }

    /**
     * Convierte la fecha dada (en la zona horaria UTC) a la fecha en la zona horaria local.
     *
     * @param utcDate
     * @return
     */
    public static long getLocalDateFromUTC(long utcDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(utcDate);
        return utcDate - gmtOffset;
    }

    /**
     * Convierte la fecha local a UTC
     *
     * @param localDate
     * @return
     */
    public static long getUTCDateFromLocal(long localDate) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(localDate);
        return localDate + gmtOffset;
    }

    /**
     * Devuelve los datos de la fecha en forma amigable para el usuario
     *
     * @param context
     * @param dateInMillis
     * @param showFullDate
     *
     * @return
     */
    public static String getFriendlyDateString(Context context, long dateInMillis, boolean showFullDate) {

        long localDate = getLocalDateFromUTC(dateInMillis);
        long dayNumber = getDayNumber(localDate);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());
        Locale spanishLocale=new Locale("es", "ES");

        if (dayNumber == currentDayNumber || showFullDate) {
            /*
             * Para el dia de HOY
             */
            String dayName = getDayName(context, localDate);
            String readableDate = getReadableDateString(context, localDate);
            if (dayNumber - currentDayNumber < 2) {
                String localizedDayName = new SimpleDateFormat("EEEE").format(localDate);
                return readableDate.replace(localizedDayName, dayName);
            } else {
                return readableDate;
            }
        } else if (dayNumber < currentDayNumber + 7) {
            //Para los siguientes 7 dias
            return getDayName(context, localDate);
        } else {
            //Para el resto de los dias
            int flags = DateUtils.FORMAT_SHOW_DATE
                    | DateUtils.FORMAT_NO_YEAR
                    | DateUtils.FORMAT_ABBREV_ALL
                    | DateUtils.FORMAT_SHOW_WEEKDAY;

            return DateUtils.formatDateTime(context, localDate, flags);
        }
    }

    /**
     * Devuelve fecha en forma abreviada
     *
     * @param context
     * @param timeInMillis
     *
     * @return
     */
    private static String getReadableDateString(Context context, long timeInMillis) {
        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_NO_YEAR
                | DateUtils.FORMAT_SHOW_WEEKDAY;

        return DateUtils.formatDateTime(context, timeInMillis, flags);
    }

    /**
     * Dado un dia retorna el nombre, ej: hoy, mañana, miercoles.
     *
     * @param context
     * @param dateInMillis
     *
     * @return dia de la semana
     */
    private static String getDayName(Context context, long dateInMillis) {
        long dayNumber = getDayNumber(dateInMillis);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());
        if (dayNumber == currentDayNumber) {
            return context.getString(R.string.today);
        } else if (dayNumber == currentDayNumber + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            Locale spanishLocale=new Locale("es", "ES");
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", spanishLocale);
            return dayFormat.format(dateInMillis);
        }
    }
}
