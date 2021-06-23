package com.example.tree_solution_proyect.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //Instanciamos los atributos
    private FirebaseAuth mAuth;
    Button entrar,registro;

    //Metodo que se llama al crear dicha activity aqui es donde se inicializan todos los variables
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        entrar=findViewById(R.id.btnstartsession);
        entrar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Login.class)));
        registro=findViewById(R.id.btnregistro);
        registro.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,Registro.class)));
    }

    //Metodo que se llama cuando el activity se reinicia
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, AplicationActivity.class));
        }
    }
}
