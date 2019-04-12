package ar.com.tesina.climapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.net.URL;

import ar.com.tesina.climapp.utilities.NetworkUtils;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        mWeatherTextView = (TextView) findViewById(R.id.tv_weather_data);

        loadWeatherData();
    }

    /**
     *
     */
    private void loadWeatherData() {
        //String location = SunshinePreferences.getPreferredWeatherLocation(this);
        String location = "3432043";
        new FetchWeatherTask().execute(location);
    }

    /**
     * Clase que realiza el llamado asincronico para obtener los datos
     */
    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String location = params[0];

            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            return null;
        }

        //Metodo para mostrar los resultados
        @Override
        protected void onPostExecute(String[] weatherData) {
            if (weatherData != null) {
                for (String weatherString : weatherData) {
                    mWeatherTextView.append((weatherString) + "\n\n\n");
                }
            }
        }
    }

}
