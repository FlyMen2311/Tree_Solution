package com.example.tree_solution_proyect.Vistas;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {
    private EditText txtNombre,txtCorreo,txtContraseña,txtContraseñaRepetida;
    private Button registro;
    private FirebaseAuth mAuth;

    private FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        txtNombre=findViewById(R.id.UsernameTxt);
        txtCorreo=findViewById(R.id.emailtxt);
        txtContraseña=findViewById(R.id.contrasenatxt);
        txtContraseñaRepetida=findViewById(R.id.repetircontrasenatxt);

        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        registro= (Button) findViewById(R.id.btnRegistro);
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtCorreo.getText().toString();
                String nombre=txtNombre.getText().toString();

                if(isValidEmail(email) && validContracena() && validarNombreUsuario(nombre)){
                    String contrasena=txtContraseña.getText().toString();
                    mAuth.createUserWithEmailAndPassword(email, contrasena)
                            .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Registro.this, "Se registro correctamente",
                                                Toast.LENGTH_SHORT).show();
                                        Usuario usuario=new Usuario();
                                        usuario.setEmail(email);
                                        usuario.setUserName(nombre);
                                        FirebaseUser currentUser=mAuth.getCurrentUser();
                                        DatabaseReference reference=database.getReference("Usuarios/"+currentUser.getUid());
                                        reference.setValue(usuario);
                                        finish();
                                    } else {
                                        mAuth.fetchSignInMethodsForEmail(email)
                                                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                                        boolean isEmailExist = task.getResult().getSignInMethods().isEmpty();
                                                        if (!isEmailExist) {
                                                            Toast.makeText(Registro.this, "Email ya existe",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }else{
                    Toast.makeText(Registro.this, "Error en formato de registro",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });







    }
    public final static boolean isValidEmail(CharSequence charsequence){
        return !TextUtils.isEmpty(charsequence) && Patterns.EMAIL_ADDRESS.matcher(charsequence).matches();
    }
    public boolean validContracena(){
        String contrasena,contrasenarepetida;
        contrasena=txtContraseña.getText().toString();
        contrasenarepetida=txtContraseñaRepetida.getText().toString();

        if(contrasena.equals(contrasenarepetida)){
            if(contrasena.length()>=6 && contrasena.length()<=16){
                return true;
            }
            else{
               return false;
            }
        }else{

            return  false;
        }
    }
    public boolean validarNombreUsuario(String nombre){
        return !nombre.isEmpty();
    }
}
