package ar.com.tesina.climapp.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class ClimappSyncUtils {

    /**
     * @param context
     */
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, ClimappSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
