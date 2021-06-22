package com.example.tree_solution_proyect.Vistas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_mensaje;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatsClick extends AppCompatActivity {
    private ImageButton enviar;
    private EditText texto_mensaje;
    private RecyclerView recyclerView;


    private ImageView foto_libro;
    private TextView nombre;
    private ImageButton envial_loc;
    private TextView userName;

    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceChat;
    private DatabaseReference databaseReferenceLibros;
    private DatabaseReference databaseReferenceUsuario;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Adapter_mensaje adapter_mensaje;

    private Calendar calendario = Calendar.getInstance();

    private LocationManager locationManager;

    private String fotoPerfilString;

    private FirebaseAuth mAuth;

    private ImageView foto_libro_propietario;
    private TextView  nombre_libro_propietario;
    private TextView autor;
    private TextView categoria;
    private TextView ISBN;
    private TextView condition;
    private TextView precio;
    private TextView hora;
    private RatingBar ratingBar;

    private  LLibro Llibro;
    public ChatsClick() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);


        enviar = findViewById(R.id.btn_enviar);
        texto_mensaje = findViewById(R.id.Texto_mensaje);
        recyclerView = findViewById(R.id.recycler_chat);

        foto_libro = findViewById(R.id.foto_libro_chat);

        nombre = findViewById(R.id.nombre_chat);
        autor = findViewById(R.id.autor_chat);
        categoria = findViewById(R.id.catergoria_chat);
        ISBN = findViewById(R.id.Isbn_chat);
        condition = findViewById(R.id.condition_chat);
        precio = findViewById(R.id.precio_chat);
        hora = findViewById(R.id.fechacreacion_chat);
        ratingBar = findViewById(R.id.ratingBar_libro_chat);
        foto_libro_propietario = findViewById(R.id.foto_propietario_libro_chat);
        nombre_libro_propietario = findViewById(R.id.nombre_usuario_propietario_chat);


        envial_loc = findViewById(R.id.localizacion_env);
        userName = findViewById(R.id.NameLibroChat);
        mAuth = FirebaseAuth.getInstance();

        Llibro = (LLibro) getIntent().getExtras().getSerializable("libro");
        if (Llibro == null) {
            Libro libro = new Libro();
            String receptor = getIntent().getExtras().getString("keyreceptor");

            if (!receptor.equals(mAuth.getCurrentUser().getUid())) {
                libro.setUserKey(receptor);
            } else {
                libro.setUserKey(getIntent().getExtras().getString("keyemisor"));
            }
            Llibro = new LLibro(libro, getIntent().getExtras().getString("keylibro"));
        }

        if((!Llibro.getKey().equals(""))&&(!Llibro.getLibro().getUserKey().equals(""))&&(Llibro!=null)){
        database = FirebaseDatabase.getInstance();
        databaseReferenceChat = database.getReference(Constantes.NODO_CHATS).
                child(mAuth.getCurrentUser().getUid()).
                child(Llibro.getLibro().getUserKey()).
                child(Llibro.getKey());

        databaseReferenceLibros = database.getReference(Constantes.NODO_LIBROS).child(Llibro.getKey());
        databaseReferenceUsuario = database.getReference(Constantes.NODO_USUARIOS).child(Llibro.getLibro().getUserKey());

        storage = FirebaseStorage.getInstance();

        fotoPerfilString = "";

        mAuth = FirebaseAuth.getInstance();


        adapter_mensaje = new Adapter_mensaje(getApplicationContext());
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter_mensaje);

        //Gestion btn enviar localizacion
        envial_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMensaje = geolocalizacion();
                if (!textMensaje.isEmpty()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(textMensaje);
                    mensaje.setUserKey(UsuarioDAO.getKeyUsuario());

                    DatabaseReference referenceEmisor = database.getReference(Constantes.NODO_CHATS).
                            child(mAuth.getCurrentUser().getUid()).
                            child(Llibro.getLibro().getUserKey())
                            .child(Llibro.getKey());

                    DatabaseReference referenceReceptor = database.
                            getReference(Constantes.NODO_CHATS).
                            child(Llibro.getLibro().getUserKey()).
                            child(mAuth.getCurrentUser().getUid()).
                            child(Llibro.getKey());
                    referenceEmisor.push().setValue(mensaje);
                    referenceReceptor.push().setValue(mensaje);
                    texto_mensaje.setText("");
                }

            }
        });

        //Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
        //se reflejen en la aplicacion
        databaseReferenceLibros.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Libro> libro = new GenericTypeIndicator<Libro>() {
                };

                Libro libro1 = dataSnapshot.getValue(libro);
                if (dataSnapshot.exists()) {
                    Uri uri = Uri.parse(libro1.getFotoPrincipalUrl());
                    if (uri != null) {
                        try {
                            Picasso.with(getApplicationContext())
                                    .load(uri.getPath())
                                    .into(foto_libro);

                            nombre.setText(libro1.getNombre());
                            autor.setText("by " + libro1.getAutor());
                            categoria.setText(libro1.getCategoria());
                            ISBN.setText(libro1.getISBN());
                            condition.setText(libro1.getCondition());
                            precio.setText(String.valueOf(libro1.getPrecio() + "€"));
                            hora.setText(Llibro.obtenerFechaDeCreacionLibro());

                            if (libro1.getCondition().equals("Nuevo")) {
                                ratingBar.setRating(5);
                            } else if (libro1.getCondition().equals("Muy buen estado")) {
                                ratingBar.setRating(4);
                            } else if (libro1.equals("Buen estado")) {
                                ratingBar.setRating(3);
                            } else if (libro1.getCondition().equals("Defecto estético")) {
                                ratingBar.setRating(2);
                            } else if (libro1.getCondition().equals("Mala condición")) {
                                ratingBar.setRating(1);
                            } else {
                                ratingBar.setRating(0);
                            }
                            databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    //leeremos un objeto de tipo Estudiante
                                    GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {
                                    };
                                    try {
                                        Usuario usuario = snapshot.getValue(u);
                                        Picasso.with(getApplicationContext()).load(usuario.getFotoPerfilUrl()).into(foto_libro_propietario);
                                        nombre_libro_propietario.setText(usuario.getUserName());
                                    }catch (Exception e){}
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                        } catch (Exception e) {
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });


        //Gestion btn enviar mensaje
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMensaje = texto_mensaje.getText().toString();
                if (!textMensaje.isEmpty()) {
                    Mensaje mensaje = new Mensaje();
                    mensaje.setMensaje(textMensaje);
                    mensaje.setUserKey(UsuarioDAO.getInstance().getKeyUsuario());

                    DatabaseReference referenceEmisor = database.getReference(Constantes.NODO_CHATS).
                            child(mAuth.getCurrentUser().getUid()).
                            child(Llibro.getLibro().getUserKey())
                            .child(Llibro.getKey());

                    DatabaseReference referenceReceptor = database.
                            getReference(Constantes.NODO_CHATS).
                            child(Llibro.getLibro().getUserKey()).
                            child(mAuth.getCurrentUser().getUid()).
                            child(Llibro.getKey());

                    referenceEmisor.push().setValue(mensaje);
                    referenceReceptor.push().setValue(mensaje);
                    texto_mensaje.setText("");
                }
            }
        });

        //Gestion de eventos producidos en FIREBASE
        databaseReferenceChat.addChildEventListener(new ChildEventListener() {

            Map<String, LUsuario> stringLUsuarioMap = new HashMap<>();

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final Mensaje m = dataSnapshot.getValue(Mensaje.class);
                final LMensaje lMensaje = new LMensaje(m, dataSnapshot.getKey());
                final int posicion = adapter_mensaje.addMensaje(lMensaje);

                if (stringLUsuarioMap.get(m.getUserKey()) != null) {
                    lMensaje.setlUsuario(stringLUsuarioMap.get(m.getUserKey()));
                    adapter_mensaje.actualizarMensaje(posicion, lMensaje);
                } else {
                    UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            stringLUsuarioMap.put(m.getUserKey(), lUsuario);
                            lMensaje.setlUsuario(lUsuario);
                            adapter_mensaje.actualizarMensaje(posicion, lMensaje);
                        }

                        @Override
                        public void devolverError(String mensajeError) {
                            Toast.makeText(getApplicationContext(), "Error" + mensajeError, Toast.LENGTH_SHORT);
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Funcion para pasar al ultimo mensaje producido
        adapter_mensaje.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter_mensaje.getItemCount() - 1);
            }
        });
    }else{
            Toast.makeText(getApplicationContext(), "Libro no existe,hubo un error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentuser=mAuth.getCurrentUser();

        if(currentuser!=null){

            databaseReferenceLibros.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Libro libro = snapshot.getValue(Libro.class);
                    LLibro llibro = new LLibro(libro, snapshot.getKey());
                    if (libro != null) {
                        Picasso.with(getApplicationContext())
                                .load(libro.getFotoPrincipalUrl())
                                .into(foto_libro);

                        nombre.setText(libro.getNombre());
                        autor.setText("by " + libro.getAutor());
                        categoria.setText(libro.getCategoria());
                        ISBN.setText(libro.getISBN());
                        condition.setText(libro.getCondition());
                        precio.setText(String.valueOf(libro.getPrecio() + "€"));
                        hora.setText(llibro.obtenerFechaDeCreacionLibro());

                        if (libro.getCondition().equals("Nuevo")) {
                            ratingBar.setRating(5);
                        } else if (libro.getCondition().equals("Muy buen estado")) {
                            ratingBar.setRating(4);
                        } else if (libro.equals("Buen estado")) {
                            ratingBar.setRating(3);
                        } else if (libro.getCondition().equals("Defecto estético")) {
                            ratingBar.setRating(2);
                        } else if (libro.getCondition().equals("Mala condición")) {
                            ratingBar.setRating(1);
                        } else {
                            ratingBar.setRating(0);
                        }
                        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                //leeremos un objeto de tipo Usuario
                                GenericTypeIndicator<Usuario> u = new GenericTypeIndicator<Usuario>() {
                                };
                                try {
                                    Usuario usuario = snapshot.getValue(u);
                                    Picasso.with(getApplicationContext()).load(usuario.getFotoPerfilUrl()).into(foto_libro_propietario);
                                    nombre_libro_propietario.setText(usuario.getUserName());
                                }catch(Exception e){}
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }

                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }else{
            startActivity(new Intent(getApplicationContext(), Login.class));
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    //Funcion para obtener geolocalizacion actual
    public String  geolocalizacion() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{

                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        String localizacion =
                String.format(Locale.ENGLISH, "Estoy en esta localizacion http://maps.google.com/maps?q=%f,%f ", loc.getLatitude(),loc.getLongitude());


        return localizacion;
    }

    //Funcion para extraer desde una cadena de texto un Link
    public static List<String> extractURL(String cadena) {

        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = (String) "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile((String) urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(cadena.toString());

        while (urlMatcher.find())
        {
            containedUrls.add((String) cadena.toString().substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;

    }


    public ImageView getFoto_libro() {
        return foto_libro;
    }

    public void setFoto_libro(ImageView foto_libro) {
        this.foto_libro = foto_libro;
    }


}
