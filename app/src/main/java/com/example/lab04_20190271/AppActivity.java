package com.example.lab04_20190271;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.button.MaterialButton;

public class AppActivity extends AppCompatActivity {

    private NavController navController;
    private MaterialButton btnLocation, btnForecaster, btnFuture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // Inicializar NavController CORRECTAMENTE
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }

        // Configurar botones de navegación
        btnLocation = findViewById(R.id.btnLocation);
        btnForecaster = findViewById(R.id.btnForecaster);
        btnFuture = findViewById(R.id.btnFuture);

        // Configurar eventos de clic para navegación
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.locationFragment);
                    updateButtonSelection(btnLocation);
                }
            }
        });

        btnForecaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.forecasterFragment);
                    updateButtonSelection(btnForecaster);
                }
            }
        });

        btnFuture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (navController != null) {
                    navController.navigate(R.id.futureFragment);
                    updateButtonSelection(btnFuture);
                }
            }
        });

        // Quitar fragmentos del BackStack y actualizar botones
        if (navController != null) {
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                controller.popBackStack(destination.getId(), false);
                updateNavigationButtonsByDestination(destination.getId());
            });
        }

        // Seleccionar el botón de ubicación por defecto (página inicial)
        updateButtonSelection(btnLocation);
    }

    /**
     * Actualiza la selección de los botones de navegación según el ID de destino
     */
    private void updateNavigationButtonsByDestination(int destinationId) {
        if (destinationId == R.id.locationFragment) {
            updateButtonSelection(btnLocation);
        } else if (destinationId == R.id.forecasterFragment) {
            updateButtonSelection(btnForecaster);
        } else if (destinationId == R.id.futureFragment) {
            updateButtonSelection(btnFuture);
        }
    }

    /**
     * Actualiza la selección visual de los botones de navegación
     */
    private void updateButtonSelection(MaterialButton selectedButton) {
        // Desmarcar todos los botones
        btnLocation.setSelected(false);
        btnForecaster.setSelected(false);
        btnFuture.setSelected(false);

        // Marcar el botón seleccionado
        selectedButton.setSelected(true);
    }

    @Override
    public void onBackPressed() {
        // Si estamos en el nivel raíz de navegación, regresar al MainActivity
        if (navController != null && navController.getCurrentDestination() != null &&
                navController.getCurrentDestination().getId() == R.id.locationFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar la selección según el destino actual cuando la actividad se reanuda
        if (navController != null && navController.getCurrentDestination() != null) {
            updateNavigationButtonsByDestination(navController.getCurrentDestination().getId());
        }
    }
}