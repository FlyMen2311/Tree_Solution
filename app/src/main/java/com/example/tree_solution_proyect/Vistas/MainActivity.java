package com.example.tree_solution_proyect.Vistas;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {
    //Instanciamos los atributos
    private FirebaseAuth mAuth;
    Button entrar, registro;

    //Metodo que se llama al crear dicha activity aqui es donde se inicializan todos los variables
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        entrar = findViewById(R.id.btnstartsession);
        entrar.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Login.class)));
        registro = findViewById(R.id.btnregistro);
        registro.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Registro.class)));
    }

    //Metodo que se llama cuando el activity se reinicia
    @Override
    protected void onStart() {
        super.onStart();

        if (isNetworkAvailable(getApplicationContext())) {
            FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentuser != null) {

            } else {
                startActivity(new Intent(getApplicationContext(), Login.class));
                try {
                    finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "No hay conexion a internet", Toast.LENGTH_LONG).show();
            finishAffinity();
        }
    }
    //Metodo que sirve para comprobar si movil eta conectado al internet o no
    public  boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {

                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {

                    return true;
                }  else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)){
                    return true;
                }
            }
        }

        return false;

    }
}
