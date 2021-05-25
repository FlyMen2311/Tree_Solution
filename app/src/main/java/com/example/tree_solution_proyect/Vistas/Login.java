package com.example.tree_solution_proyect.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123 ;
    private EditText txtEmail, txtContracena;
    private Button btnEntrar;
    private FirebaseAuth mAuth;
    private ImageButton googleAut;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase database;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.emailTxt);
        txtContracena = findViewById(R.id.contrasenatxt);

        database= FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        googleAut=findViewById(R.id.entrar_google);
        googleAut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

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
                                        startActivity(new Intent(Login.this, AplicationActivity.class));
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
        createRequest();
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

        if ((currentUser != null)) {
            startActivity(new Intent(Login.this, AplicationActivity.class));
        }
    }

    public void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                mAuth.fetchSignInMethodsForEmail(account.getEmail())
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                if (isNewUser) {
                                    usuario=new Usuario();
                                    usuario.setEmail(account.getEmail());
                                    usuario.setUserName(account.getDisplayName());
                                    firebaseAuthWithGoogle(account.getIdToken());
                                } else {
                                    firebaseAuthWithGoogle(account.getIdToken());
                                }
                            }
                        });
            } catch (ApiException e) {

                Toast.makeText(Login.this, e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser=mAuth.getCurrentUser();
                            DatabaseReference reference=database.getReference("Usuarios/"+currentUser.getUid());
                            reference.setValue(usuario);
                            finish();
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(Login.this, AplicationActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Error al Entrar",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
