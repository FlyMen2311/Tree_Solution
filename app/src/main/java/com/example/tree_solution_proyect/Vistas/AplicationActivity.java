package com.example.tree_solution_proyect.Vistas;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.databinding.ActivityAplicationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AplicationActivity extends AppCompatActivity {

    private ActivityAplicationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAplicationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(AplicationActivity.this, R.id.nav_host_fragment_activity_aplication);
        NavigationUI.setupActionBarWithNavController(AplicationActivity.this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}