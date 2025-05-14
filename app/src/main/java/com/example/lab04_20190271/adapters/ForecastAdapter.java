package com.example.lab04_20190271.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab04_20190271.R;
import com.example.lab04_20190271.models.ForecastResponse;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<ForecastResponse.ForecastDay> forecastDays = new ArrayList<>();
    private ForecastResponse.LocationDetails locationDetails;

    public void setForecastData(ForecastResponse forecastResponse) {
        this.locationDetails = forecastResponse.getLocation();
        this.forecastDays = forecastResponse.getForecast().getForecastday();
        notifyDataSetChanged();
    }

    public void clearData() {
        this.forecastDays.clear();
        this.locationDetails = null;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        ForecastResponse.ForecastDay forecastDay = forecastDays.get(position);
        holder.bind(forecastDay, locationDetails);
    }

    @Override
    public int getItemCount() {
        return forecastDays.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvLocationInfo, tvCondition, tvTemperatures, tvDetails;
        ImageView ivCondition;

        ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocationInfo = itemView.findViewById(R.id.tvLocationInfo);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvTemperatures = itemView.findViewById(R.id.tvTemperatures);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            ivCondition = itemView.findViewById(R.id.ivCondition);
        }

        void bind(ForecastResponse.ForecastDay forecastDay, ForecastResponse.LocationDetails location) {
            // Información de fecha
            tvDate.setText(forecastDay.getDate());

            // Información de ubicación
            if (location != null) {
                tvLocationInfo.setText(location.getName() + ", " + location.getRegion() + ", " +
                        location.getCountry() + " (ID: " + location.getId() + ")");
            } else {
                tvLocationInfo.setText("Ubicación desconocida");
            }

            // Condición climática
            ForecastResponse.Condition condition = forecastDay.getDay().getCondition();
            tvCondition.setText(condition.getText());

            // Cargar ícono de condición climática
            String iconUrl = "https:" + condition.getIcon();
            Picasso.get().load(iconUrl).placeholder(R.drawable.ic_weather_default).into(ivCondition);

            // Temperaturas
            tvTemperatures.setText(String.format("Máx: %.1f°C, Mín: %.1f°C",
                    forecastDay.getDay().getMaxtempC(),
                    forecastDay.getDay().getMintempC()));

            // Detalles adicionales
            tvDetails.setText(String.format("Temperatura Promedio: %.1f°C",
                    forecastDay.getDay().getAvgtempC()));
        }
    }
}