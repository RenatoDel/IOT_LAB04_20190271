package com.example.lab04_20190271.fragments;

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
import com.example.lab04_20190271.adapters.FutureAdapter;
import com.example.lab04_20190271.api.WeatherApiClient;
import com.example.lab04_20190271.models.FutureResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FutureFragment extends Fragment {

    private EditText etLocationId, etFutureDate;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private FutureAdapter adapter;
    private TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        return inflater.inflate(R.layout.fragment_future, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar vistas
        etLocationId = view.findViewById(R.id.etLocationId);
        etFutureDate = view.findViewById(R.id.etFutureDate);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvTitle = view.findViewById(R.id.tvTitle);

        // Configurar RecyclerView
        adapter = new FutureAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Sugerir una fecha futura (por ejemplo, 30 días a partir de hoy) en el campo de texto
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etFutureDate.setText(sdf.format(calendar.getTime()));

        // Configurar botón de búsqueda
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String locationId = etLocationId.getText().toString().trim();
                String futureDate = etFutureDate.getText().toString().trim();

                if (!locationId.isEmpty() && !futureDate.isEmpty()) {
                    getFutureForecast(locationId, futureDate);
                } else {
                    Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getFutureForecast(String locationId, String futureDate) {
        // Mostrar carga
        Toast.makeText(getContext(), "Obteniendo pronóstico futuro...", Toast.LENGTH_SHORT).show();

        // Realizar la llamada a la API
        WeatherApiClient.getInstance().getApiService().getFutureForecast(
                WeatherApiClient.getInstance().getApiKey(),
                locationId,
                futureDate
        ).enqueue(new Callback<FutureResponse>() {
            @Override
            public void onResponse(Call<FutureResponse> call, Response<FutureResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FutureResponse futureResponse = response.body();
                    adapter.setFutureData(futureResponse);

                    // Actualizar título
                    FutureResponse.LocationDetails location = futureResponse.getLocation();
                    tvTitle.setText("Pronóstico Futuro para " + location.getName());
                } else {
                    Toast.makeText(getContext(), "Error al obtener el pronóstico futuro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FutureResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}