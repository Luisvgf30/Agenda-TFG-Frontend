package com.example.miagenda;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
<<<<<<< HEAD
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.sign_in);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
=======
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
>>>>>>> ibra3
        });
        Button signinButton = findViewById(R.id.signin_login);
        Button signupButton = findViewById(R.id.signup_login);

        signinButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.signin_login){
            intent = new Intent(this, Profile.class);
            startActivity(intent);
        }
        if(v.getId() == R.id.signup_login){
            intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }

    }





}