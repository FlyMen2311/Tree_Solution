package com.example.tree_solution_proyect.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private EditText txtEmail, txtContracena;
    private Button btnEntrar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.emailTxt);
        txtContracena = findViewById(R.id.contrasenatxt);

        mAuth = FirebaseAuth.getInstance();

        btnEntrar = findViewById(R.id.btnEntrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                if (isValidEmail(email) && validContracena()) {
                    String contracena = txtContracena.getText().toString();

                    mAuth.signInWithEmailAndPassword(email, contracena)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login.this, "Se logeo correctamente",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this, ChatActivity.class));
                                    } else {
                                        Toast.makeText(Login.this, "Error al Entrar",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    Toast.makeText(Login.this, "Email o la contraceña son incorectos",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public final static boolean isValidEmail(CharSequence charsequence) {
        return !TextUtils.isEmpty(charsequence) && Patterns.EMAIL_ADDRESS.matcher(charsequence).matches();
    }

    public boolean validContracena() {
        String contrasena;
        contrasena = txtContracena.getText().toString();
        if (contrasena.length() >= 6 && contrasena.length() <= 16) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(Login.this, AplicationActivity.class));
        }
    }

}
