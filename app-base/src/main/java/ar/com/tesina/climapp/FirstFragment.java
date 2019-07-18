package ar.com.tesina.climapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import ar.com.tesina.climapp.data.WeatherContract;
import ar.com.tesina.climapp.sync.ClimappSyncUtils;


public class FirstFragment extends Fragment implements
		ForecastAdapter.ForecastAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>{
	// Store instance variables
	private ForecastAdapter mForecastAdapter;
	private RecyclerView mRecyclerView;
	private ProgressBar mLoadingIndicator;
    private static final int ID_FORECAST_LOADER = 44;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_CITY
    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_CONDITION_ID = 3;
    public static final int INDEX_WEATHER_CITY = 4;


	// newInstance constructor for creating fragment with arguments
	public static FirstFragment newInstance(String cityName) {
		FirstFragment fragmentFirst = new FirstFragment();
		Bundle args = new Bundle();
		args.putString("cityName", cityName);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        LoaderManager.getInstance(this).initLoader(ID_FORECAST_LOADER,null,this);
        ClimappSyncUtils.initialize(getActivity());
	}

	// Inflate the view for the fragment based on layout XML
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_forecast, container, false);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_forecast);

		mLoadingIndicator = (ProgressBar) view.findViewById(R.id.pb_loading_indicator);

        showLoading();

		LinearLayoutManager layoutManager
				= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

		mRecyclerView.setLayoutManager(layoutManager);

		mRecyclerView.setHasFixedSize(true);

		mForecastAdapter = new ForecastAdapter(this, getActivity());

		mRecyclerView.setAdapter(mForecastAdapter);

		return view;
	}

	@Override
	public void onClick(long date) {
		Intent weatherDetailIntent = new Intent(getActivity(), DetailActivity.class);
		Uri uriForDateClicked = WeatherContract.WeatherEntry.buildWeatherUriWithDate(date);
		weatherDetailIntent.setData(uriForDateClicked);
		startActivity(weatherDetailIntent);
	}

    /**
     * Método que hace visible la vista de los datos meteorológicos y ocultará el mensaje de error.
     */
    private void showWeatherDataView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    /**
     * Instancia un Loader
     *
     * @param loaderId
     * @param loaderArgs
     *
     * @return new Loader
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, final Bundle loaderArgs) {

        switch (loaderId) {
            case ID_FORECAST_LOADER:
                Uri forecastQueryUri = WeatherContract.WeatherEntry.CONTENT_URI;
                String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards(this.getArguments().getString("cityName"));
                return new CursorLoader(getActivity(),
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        selection,
                        null,
                        sortOrder);
            default:
                throw new RuntimeException("Loader no implementado: " + loaderId);
        }
    }

    /**
     * Se llama cuando un Loader creado anteriormente ha terminado su carga.
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mForecastAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);

        if (data.getCount() != 0) showWeatherDataView();
    }

    /**
     * Se llama cuando un Loader creado anteriormente se está reiniciando y,
     * por lo tanto, sus datos no están disponibles. Se debe eliminar cualquier
     * referencia que tenga a los datos del Loader.
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mForecastAdapter.swapCursor(null);
    }

}