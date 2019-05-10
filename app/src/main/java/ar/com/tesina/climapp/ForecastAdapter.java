package ar.com.tesina.climapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ar.com.tesina.climapp.utilities.SunshineDateUtils;
import ar.com.tesina.climapp.utilities.SunshineWeatherUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{

    private final ForecastAdapterOnClickHandler mClickHandler;

    private final Context mContext;

    private Cursor mCursor;

    /**
     * Para resolver los eventos onClick.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(long date);
    }

    /**
     *
     * @param mClickHandler
     * @param mContext
     */
    public ForecastAdapter(ForecastAdapterOnClickHandler mClickHandler, Context mContext) {
        this.mClickHandler = mClickHandler;
        this.mContext = mContext;
    }

    /**
     *
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            mWeatherTextView = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            mClickHandler.onClick(dateInMillis);
        }
    }

    /**
     * Se llama cuando se crea cada nuevo ViewHolder. Se crearán suficientes ViewHolders
     * para llenar la pantalla y permitir el desplazamiento.
     *
     * @param viewGroup ViewGroup donde estan los ViewHolders.
     * @param viewType
     * @return un nuevo ForecastAdapterViewHolder que contiene la vista para cada elemento de la lista
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder es llamado por RecyclerView para mostrar los datos en la posición
     * especificada. En este método, actualizamos el contenido de ViewHolder para mostrar
     * los datos del clima para esta posición en particular, usando el argumento de "posición".
     *
     * @param forecastAdapterViewHolder El ViewHolder que debe actualizarse para representar
     *                                  el contenido del elemento en la posición dada
     * @param position La posición del elemento dentro de los datos del adapter.
     */
    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);

        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateInMillis, false);
        int weatherId = mCursor.getInt(MainActivity.INDEX_WEATHER_CONDITION_ID);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

        String highAndLowTemperature =
                SunshineWeatherUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);

        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;

        forecastAdapterViewHolder.mWeatherTextView.setText(weatherSummary);
    }

    /**
     * Retorna numero de items a mostrar.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * Swaps the cursor used by the ForecastAdapter for its weather data. This method is called by
     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     * the weather data is reset. When this method is called, we assume we have a completely new
     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     * @param newCursor the new cursor to use as ForecastAdapter's data source
     */
    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

}
