package com.example.miagenda;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.miagenda.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       setUpNavigation();
    }

    private void setUpNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_hostfragment);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(
                bottomNavigationView,
                navController
        );

        // Configura el listener para actualizar el título
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_calendar) {
                if(navController.getCurrentDestination().getId() != R.id.navigation_calendar) {
                    navController.navigate(R.id.navigation_calendar);
                }
            } else if (id == R.id.navigation_notes) {
                if(navController.getCurrentDestination().getId() != R.id.navigation_notes) {
                    navController.navigate(R.id.navigation_notes);
                }
            } else if (id == R.id.navigation_tasks) {
                if(navController.getCurrentDestination().getId() != R.id.navigation_tasks) {
                    navController.navigate(R.id.navigation_tasks);
                }
            } else if (id == R.id.navigation_profile) {
                // Si el destino actual no es el perfil, navega al perfil
                if (navController.getCurrentDestination().getId() != R.id.navigation_profile) {
                    navController.navigate(R.id.navigation_profile);
                }
            }
            return true;
        });
    }





}