package com.example.lab04_20190271.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab04_20190271.R;
import com.example.lab04_20190271.adapters.ForecastAdapter;
import com.example.lab04_20190271.api.WeatherApiClient;
import com.example.lab04_20190271.models.ForecastResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecasterFragment extends Fragment implements SensorEventListener {

    private EditText etLocationId, etDays;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private ForecastAdapter adapter;
    private TextView tvTitle;

    // Sensor de acelerómetro
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 20.0f; // Umbral de agitación
    private long lastUpdate;
    private float lastX, lastY, lastZ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        return inflater.inflate(R.layout.fragment_forecaster, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar vistas
        etLocationId = view.findViewById(R.id.etLocationId);
        etDays = view.findViewById(R.id.etDays);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTitle = view.findViewById(R.id.tvTitle);

        // Configurar RecyclerView
        adapter = new ForecastAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Comprobar si hay argumentos (navegación desde LocationFragment)
        if (getArguments() != null) {
            long locationId = getArguments().getLong("locationId", 0);
            String locationName = getArguments().getString("locationName", "");

            if (locationId > 0) {
                etLocationId.setText("id:" + locationId);
                tvTitle.setText("Pronóstico para " + locationName);
                getForecast("id:" + locationId, 14);
            }
        }

        // Configurar botón de búsqueda
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationId = etLocationId.getText().toString().trim();
                String daysStr = etDays.getText().toString().trim();

                if (!locationId.isEmpty() && !daysStr.isEmpty()) {
                    int days = Integer.parseInt(daysStr);
                    if (days > 0 && days <= 14) {
                        getForecast(locationId, days);
                    } else {
                        Toast.makeText(getContext(), "Los días deben estar entre 1 y 14", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inicializar sensor
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    private void getForecast(String locationId, int days) {
        // Mostrar carga
        Toast.makeText(getContext(), "Obteniendo pronóstico...", Toast.LENGTH_SHORT).show();

        // Realizar la llamada a la API
        WeatherApiClient.getInstance().getApiService().getForecast(
                WeatherApiClient.getInstance().getApiKey(),
                locationId,
                days
        ).enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setForecastData(response.body());

                    // Actualizar título si viene de búsqueda directa
                    if (getArguments() == null || getArguments().getLong("locationId", 0) == 0) {
                        ForecastResponse.LocationDetails location = response.body().getLocation();
                        tvTitle.setText("Pronóstico para " + location.getName());
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener el pronóstico", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Registrar el sensor cuando el fragmento está en primer plano
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Desregistrar el sensor cuando el fragmento no está en primer plano
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            // Solo procesar lecturas cada 100ms para ahorrar batería
            if ((currentTime - lastUpdate) > 100) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    // Detectó agitación
                    showConfirmClearDialog();
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No es necesario implementar
    }

    private void showConfirmClearDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Limpiar pronósticos")
                .setMessage("¿Desea eliminar los pronósticos actuales?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Limpiar los datos del adaptador
                        adapter.clearData();
                        tvTitle.setText("Pronósticos del Clima");
                        etLocationId.setText("");
                        etDays.setText("");
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}