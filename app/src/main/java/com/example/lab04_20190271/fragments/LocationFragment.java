package com.example.lab04_20190271.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab04_20190271.R;
import com.example.lab04_20190271.adapters.LocationAdapter;
import com.example.lab04_20190271.api.WeatherApiClient;
import com.example.lab04_20190271.models.Location;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends Fragment implements LocationAdapter.LocationClickListener {

    private EditText etLocationSearch;
    private Button btnSearch;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<Location> locationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar vistas
        etLocationSearch = view.findViewById(R.id.etLocationSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerView = view.findViewById(R.id.recyclerView);

        // Configurar RecyclerView
        locationList = new ArrayList<>();
        adapter = new LocationAdapter(locationList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Configurar botón de búsqueda
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etLocationSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchLocations(query);
                } else {
                    Toast.makeText(getContext(), "Ingrese una ubicación para buscar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchLocations(String query) {
        // Mostrar carga
        Toast.makeText(getContext(), "Buscando ubicaciones...", Toast.LENGTH_SHORT).show();

        // Realizar la llamada a la API
        WeatherApiClient.getInstance().getApiService().searchLocations(
                WeatherApiClient.getInstance().getApiKey(),
                query
        ).enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationList.clear();
                    locationList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (locationList.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron ubicaciones", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al buscar ubicaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocationClick(Location location) {
        // Navegar al fragmento de pronóstico con el ID de la ubicación
        Bundle bundle = new Bundle();
        bundle.putLong("locationId", location.getId());
        bundle.putString("locationName", location.getName());

        Navigation.findNavController(requireView())
                .navigate(R.id.action_locationFragment_to_forecasterFragment, bundle);
    }
}