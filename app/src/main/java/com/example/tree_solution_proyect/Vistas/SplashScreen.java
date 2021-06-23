package com.example.tree_solution_proyect.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.R;
//Clase para mostrar SplashScreen en primer lugar al abrir la aplicacion
public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(() -> startActivity(new Intent
                (SplashScreen.this,MainActivity.class)),3000);
    }
}
