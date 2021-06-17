package com.example.tree_solution_proyect.Vistas.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class MisLibrosClickActivity extends AppCompatActivity {
    private ImageView foto_libro,foto_libro_propietario,favorit;
    private TextView precio;
    private TextView hora;
    private TextView condition;
    private TextView ISBN;
    private TextView categoria;
    private TextView autor;
    private TextView nombre;
    private TextView nombre_libro_propietario;
    private TextView descripcion;
    private Context context;
    private RatingBar ratingBar;
    private DatabaseReference databaseReferenceUsuario;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Button btnEliminar,btnVolver,btnModificar,btnVendido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mislibros_click);

        foto_libro=findViewById(R.id.foto_libro);
        favorit=findViewById(R.id.favoritos);
        nombre=findViewById(R.id.nombre);
        autor=findViewById(R.id.autor);
        categoria=findViewById(R.id.holder_catergoria);
        ISBN=findViewById(R.id.holder_Isbn);
        condition=findViewById(R.id.holder_condition);
        precio=findViewById(R.id.holder_precio);
        hora=findViewById(R.id.holder_fechacreacion);
        descripcion=findViewById(R.id.descripcion);
        ratingBar=findViewById(R.id.ratingBar_libro);
        foto_libro_propietario =findViewById(R.id.foto_user_libro);
        nombre_libro_propietario=findViewById(R.id.nombre_user);
        btnEliminar=findViewById(R.id.btn_eliminar);
        btnModificar=findViewById(R.id.btn_modificar);
        btnVendido=findViewById(R.id.btn_vendido);
        btnVolver=findViewById(R.id.btn_cancelar);

        LLibro Llibro= (LLibro) getIntent().getExtras().getSerializable("objectLibro");
        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        databaseReferenceUsuario =database.getReference("Usuarios/"+Llibro.getLibro().getUserKey());
        mAuth=FirebaseAuth.getInstance();
        LoadLibros(Llibro);


        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void LoadLibros(LLibro libro){
        Picasso.with(getApplicationContext()).load(libro.getLibro().getFotoPrincipalUrl()).into(foto_libro);
        nombre.setText(libro.getLibro().getNombre());
        autor.setText("by "+libro.getLibro().getAutor());
        categoria.setText(libro.getLibro().getCategoria());
        ISBN.setText(libro.getLibro().getISBN());
        descripcion.setText(libro.getLibro().getDescripcion());
        condition.setText(libro.getLibro().getCondition());
        precio.setText(String.valueOf(libro.getLibro().getPrecio()+"€"));
        hora.setText(libro.obtenerFechaDeCreacionLibro());

        if(libro.getLibro().getCondition().equals("Nuevo")){
            ratingBar.setRating(5);
        }else if(libro.getLibro().getCondition().equals("Muy buen estado")){
            ratingBar.setRating(4);
        }
        else if(libro.getLibro().getCondition().equals("Buen estado")){
            ratingBar.setRating(3);
        }
        else if(libro.getLibro().getCondition().equals("Defecto estético")){
            ratingBar.setRating(2);
        }
        else if(libro.getLibro().getCondition().equals("Mala condición")){
            ratingBar.setRating(1);
        }
        else{
            ratingBar.setRating(0);
        }
        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //leeremos un objeto de tipo Estudiante
                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {};
                Usuario usuario = snapshot.getValue(u);
                Picasso.with(getApplicationContext()).load(usuario.getFotoPerfilUrl()).into(foto_libro_propietario);
                nombre_libro_propietario.setText(usuario.getUserName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
