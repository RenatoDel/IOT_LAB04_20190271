package com.example.lab04_20190271.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab04_20190271.R;
import com.example.lab04_20190271.models.FutureResponse;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FutureAdapter extends RecyclerView.Adapter<FutureAdapter.FutureViewHolder> {

    private List<FutureResponse.Hour> hourlyForecast = new ArrayList<>();

    public void setFutureData(FutureResponse futureResponse) {
        if (futureResponse.getForecast() != null &&
                !futureResponse.getForecast().getForecastday().isEmpty()) {
            this.hourlyForecast = futureResponse.getForecast().getForecastday().get(0).getHour();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FutureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_future, parent, false);
        return new FutureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureViewHolder holder, int position) {
        FutureResponse.Hour hour = hourlyForecast.get(position);
        holder.bind(hour);
    }

    @Override
    public int getItemCount() {
        return hourlyForecast.size();
    }

    static class FutureViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvCondition, tvTemperature, tvHumidity, tvRainChance;
        ImageView ivCondition;

        FutureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvCondition = itemView.findViewById(R.id.tvCondition);
            tvTemperature = itemView.findViewById(R.id.tvTemperature);
            tvHumidity = itemView.findViewById(R.id.tvHumidity);
            tvRainChance = itemView.findViewById(R.id.tvRainChance);
            ivCondition = itemView.findViewById(R.id.ivCondition);
        }

        void bind(FutureResponse.Hour hour) {
            // Formato de hora más legible
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm 'hrs'", Locale.getDefault());
                Date date = inputFormat.parse(hour.getTime());
                if (date != null) {
                    tvTime.setText(outputFormat.format(date));
                } else {
                    tvTime.setText(hour.getTime());
                }
            } catch (ParseException e) {
                tvTime.setText(hour.getTime());
            }

            // Condición climática
            FutureResponse.Condition condition = hour.getCondition();
            tvCondition.setText(condition.getText());

            // Cargar ícono de condición climática
            String iconUrl = "https:" + condition.getIcon();
            Picasso.get().load(iconUrl).placeholder(R.drawable.ic_weather_default).into(ivCondition);

            // Temperatura
            tvTemperature.setText(String.format("Temperatura: %.1f°C", hour.getTempC()));

            // Humedad
            tvHumidity.setText(String.format("Humedad: %d%%", hour.getHumidity()));

            // Probabilidad de lluvia
            tvRainChance.setText(String.format("Probabilidad de lluvia: %d%%", hour.getChanceOfRain()));
        }
    }
}
