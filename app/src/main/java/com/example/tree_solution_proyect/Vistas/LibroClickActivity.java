package com.example.tree_solution_proyect.Vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Chat;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class LibroClickActivity extends AppCompatActivity {
    private ImageView foto_libro;
    private ImageView favorit;
    private ImageView foto_libro_propietario;
    private TextView  nombre_libro_propietario;
    private TextView nombre;
    private TextView autor;
    private TextView categoria;
    private TextView ISBN;
    private TextView condition;
    private TextView precio;
    private TextView hora;
    private Context context;
    private RatingBar ratingBar;
    private DatabaseReference databaseReferenceUsuario;
    private DatabaseReference databaseReferenceDatosChat;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Button btnchat;
    private Button btnVolver;
    private  Usuario receptor;
    private LUsuario lreceptor;
    private Usuario emisor;
    private LUsuario lemisor;
    private LLibro Llibro;
    private  Chat chat ;
    private Boolean isExist=false;
    private HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro_click);

        foto_libro=findViewById(R.id.foto_libro);
        favorit=findViewById(R.id.favoritos1);
        nombre=findViewById(R.id.nombre_chat);
        autor=findViewById(R.id.autor);
        categoria=findViewById(R.id.catergoria_chat);
        ISBN=findViewById(R.id.Isbn_chat);
        condition=findViewById(R.id.condition_chat);
        precio=findViewById(R.id.precio_chat);
        hora=findViewById(R.id.fechacreacion_chat);
        ratingBar=findViewById(R.id.ratingBar_libro_chat);
        foto_libro_propietario =findViewById(R.id.foto_user_libro);
        nombre_libro_propietario=findViewById(R.id.nombre_user);
        btnchat=findViewById(R.id.btn_aceptar);
        btnVolver=findViewById(R.id.btn_cancelar);
        chat=new Chat();

        Llibro = (LLibro) getIntent().getExtras().getSerializable("objectLibro");
        isExist= getIntent().getExtras().getBoolean("isFavorite");

        if(isExist){
            favorit.setBackgroundResource(R.drawable.favorite_libro);
        }else{
            favorit.setBackgroundResource(R.drawable.favorite);
        }
        homeFragment=new HomeFragment();

        database=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        databaseReferenceUsuario =database.getReference("Usuarios/"+Llibro.getLibro().getUserKey());
        databaseReferenceDatosChat=database.getReference(Constantes.NODO_CHAT_DATOS);
        mAuth=FirebaseAuth.getInstance();
        LoadLibros(Llibro);

        favorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorit.getBackground().getConstantState().equals(favorit.getContext().getDrawable(R.drawable.favorite).getConstantState())){
                    favorit.setBackgroundResource(R.drawable.favorite_libro);
                    LibroDAO.getInstance().crearLibroFavorito(Llibro);
                }else if(favorit.getBackground().getConstantState().equals(favorit.getContext().getDrawable(R.drawable.favorite_libro).getConstantState())){
                    LibroDAO.getInstance().eliminarLibroFavorito(Llibro);
                    favorit.setBackgroundResource(R.drawable.favorite);
                }
            }
        });



        DatabaseReference reference=database.getReference(Constantes.NODO_USUARIOS).child(Llibro.getLibro().getUserKey());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {
                };
                receptor = snapshot.getValue(u);
                lreceptor=new LUsuario(receptor,snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        DatabaseReference reference2=database.getReference(Constantes.NODO_USUARIOS).child(mAuth.getCurrentUser().getUid());
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {
                };
                emisor = snapshot.getValue(u);
                lemisor=new LUsuario(emisor,snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Boolean apto=comprobarDatos();
            if(apto){
                cargarDatosChat();
            }else{
                Toast.makeText(getApplicationContext(),"No se puede escribir mensajes a si mismo",Toast.LENGTH_SHORT).show();
            }
            }
        });
        favorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorit.getBackground().getConstantState().equals(favorit.getContext().getDrawable(R.drawable.favorite).getConstantState())){
                    favorit.setBackgroundResource(R.drawable.favorite_libro);
                    LibroDAO.getInstance().crearLibroFavorito(Llibro);
                }else if(favorit.getBackground().getConstantState().equals(favorit.getContext().getDrawable(R.drawable.favorite_libro).getConstantState())){
                    LibroDAO.getInstance().eliminarLibroFavorito(Llibro);
                    favorit.setBackgroundResource(R.drawable.favorite);

                }
            }
        });

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
                //leeremos un objeto de tipo Usuario
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

    public void cargarDatosChat(){
                DatabaseReference reference=database.getReference(Constantes.NODO_CHAT_DATOS).
                        child(mAuth.getCurrentUser().getUid()).
                        child(lreceptor.getKey()).
                        child(Llibro.getKey());
                 reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                                Chat chat1 = chatSnapshot.getValue(Chat.class);

                                if (chat1 != null) {
                                    if (compararChats(chat1,chat)) {
                                        isExist = true;
                                    } else {
                                        isExist = false;
                                    }
                                }
                            }
                            if (isExist) {
                                Intent intent = new Intent(LibroClickActivity.this, ChatsClick.class);
                                intent.putExtra("libro", Llibro);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(LibroClickActivity.this, ChatsClick.class);
                                intent.putExtra("libro", Llibro);
                                databaseReferenceDatosChat.child(lemisor.getKey()).child(lreceptor.getKey()).child(Llibro.getKey()).push().setValue(chat);
                                databaseReferenceDatosChat.child(lreceptor.getKey()).child(lemisor.getKey()).child(Llibro.getKey()).push().setValue(chat);
                                startActivity(intent);
                            }
                        } else {
                            Intent intent = new Intent(LibroClickActivity.this, ChatsClick.class);
                            intent.putExtra("libro", Llibro);
                            databaseReferenceDatosChat.child(lemisor.getKey()).child(lreceptor.getKey()).child(Llibro.getKey()).push().setValue(chat);
                            databaseReferenceDatosChat.child(lreceptor.getKey()).child(lemisor.getKey()).child(Llibro.getKey()).push().setValue(chat);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

            }



    public boolean comprobarDatos(){
        Boolean apto=false;
        if((lemisor!=null)&&(lreceptor!=null)&&(Llibro!=null)) {
            if(!lemisor.getKey().equals(lreceptor.getKey())) {
                apto=true;
                chat.setFotoPrincipalUrl(Llibro.getLibro().getFotoPrincipalUrl());
                chat.setKeyemisor(lemisor.getKey());
                chat.setKeylibro(Llibro.getKey());
                chat.setKeyreceptor(lreceptor.getKey());
                if(mAuth.getCurrentUser().getUid().equals(lemisor.getKey())) {
                    chat.setNombrePropietario(lreceptor.getUsuario().getUserName());
                }else{
                    chat.setNombrePropietario(lemisor.getUsuario().getUserName());
                }
                chat.setNombrelibro(Llibro.getLibro().getNombre());


            }else{
                apto=false;
            }
        }
        return  apto;
    }
    public boolean compararChats(Chat chat1,Chat chat2){
        boolean isigual=false;

        if((chat1.getFotoPrincipalUrl().equals(chat2.getFotoPrincipalUrl()))&&
                (chat1.getKeyemisor().equals(chat2.getKeyemisor()))&&
                (chat1.getKeylibro().equals(chat2.getKeylibro()))&&
                (chat1.getKeyreceptor().equals(chat2.getKeyreceptor()))&&
                (chat1.getNombrelibro().equals(chat2.getNombrelibro()))&&
                (chat1.getNombrePropietario().equals(chat2.getNombrePropietario()))){

            isigual=true;

        }
        return isigual;
    }
}