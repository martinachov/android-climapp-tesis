package ar.com.tesina.climapp.sync;

import android.app.IntentService;
import android.content.Intent;

public class ClimappSyncIntentService extends IntentService {

    public ClimappSyncIntentService() {
        super("ClimappSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ClimappSyncTask.syncWeather(this);
    }

}
