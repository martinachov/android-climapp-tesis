package ar.com.tesina.climapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{

    private String[] mWeatherData;

    private final ForecastAdapterOnClickHandler mClickHandler;

    /**
     * Para resolver los eventos onClick.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    /**
     *
     * @param mClickHandler
     */
    public ForecastAdapter(ForecastAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
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
                String weatherForDay = mWeatherData[adapterPosition];
                mClickHandler.onClick(weatherForDay);
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
        String weatherForThisDay = mWeatherData[position];
        forecastAdapterViewHolder.mWeatherTextView.setText(weatherForThisDay);
    }

    /**
     * Retorna numero de items a mostrar.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;
    }

    /**
     * Este método se utiliza para establecer el pronóstico del tiempo en un ForecastAdapter
     * si ya hemos creado uno. Esto es útil cuando obtenemos nuevos datos de la web pero no
     * queremos crear un nuevo ForecastAdapter para mostrarlos.
     *
     * @param weatherData The new weather data to be displayed.
     */
    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

}
