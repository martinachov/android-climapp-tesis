package ar.com.tesina.climapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.net.URL;
import java.util.Date;

import ar.com.tesina.climapp.data.SunshinePreferences;
import ar.com.tesina.climapp.utilities.NetworkUtils;
import ar.com.tesina.climapp.utilities.OpenWeatherJsonUtils;

public class DetailActivity extends AppCompatActivity {

    private String mForecast;
    private TextView mWeatherDisplay;
    private static final String FORECAST_SHARE_HASHTAG = " #ClimApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mForecast = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mWeatherDisplay.setText(mForecast);
            } else {
                Uri data = getIntent().getData();
                if (data != null && data.isHierarchical()) {
                    String uri = data.getQueryParameter("Q1");
                    mWeatherDisplay.setText("ENTRE IGUAL");
                    Log.i("MyApp", "Deep link clicked " + uri);
                }
                try {
                    String[] result = new AsyncCaller().execute(DetailActivity.this).get();
                  //  mWeatherDisplay.setText(result[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Se usa ShareCompat Intent builder para compartir datos. S
     *
     * @return
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast + FORECAST_SHARE_HASHTAG)
                .getIntent();
        return shareIntent;
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

class AsyncCaller extends AsyncTask<Context, Void, String[]>
{

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(Context... params) {

        String locationQuery = SunshinePreferences
                .getPreferredWeatherLocation(params[0]);

        URL weatherRequestUrl = NetworkUtils.buildUrl(locationQuery);

        try {
            String jsonWeatherResponse = NetworkUtils
                    .getResponseFromHttpUrl(weatherRequestUrl);
            String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                    .getSimpleWeatherStringsFromJson(params[0], jsonWeatherResponse);
            return simpleJsonWeatherData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
    }

}
