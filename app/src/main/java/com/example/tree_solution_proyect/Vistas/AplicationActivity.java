package com.example.tree_solution_proyect.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.NewPost.NewPostFragment;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.example.tree_solution_proyect.Vistas.ui.favorite.FavoriteFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.example.tree_solution_proyect.Vistas.ui.perfil.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class AplicationActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private boolean viewIsAtHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aplication);

        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        addFragment(new HomeFragment());
                        viewIsAtHome = true;
                        return true;
                    case R.id.navigation_newpost:
                        addFragment(new NewPostFragment());
                        viewIsAtHome = false;
                        return true;
                    case R.id.navigation_favorite:
                        addFragment(new FavoriteFragment());
                        viewIsAtHome = false;
                        return true;

                    case R.id.navigation_comunicacion:
                        addFragment(new ComunicacionFragment());
                        viewIsAtHome = false;
                        return true;
                    case R.id.navigation_perfil:
                        addFragment(new PerfilFragment());
                        viewIsAtHome = false;
                        return true;

                }
                return false;
            }
        });
        navView.setSelectedItemId(R.id.navigation_home);

    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_aplication, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!viewIsAtHome) { //Si la vista actual no es el fragment Home
            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_home); //Selecciona el fragment Home
        } else {
            moveTaskToBack(true);  //Si presionas Back cuando ya muestras el fragment Home, sale de la app
        }
    }
}