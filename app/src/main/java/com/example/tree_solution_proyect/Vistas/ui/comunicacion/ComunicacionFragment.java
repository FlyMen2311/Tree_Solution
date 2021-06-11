package com.example.tree_solution_proyect.Vistas.ui.comunicacion;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Adaptadores.Adapter_mensaje;
import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.Login;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


public class ComunicacionFragment extends Fragment {
    private ImageButton enviar;
    private EditText texto_mensaje;
    private RecyclerView recyclerView;


    private ImageView foto_perfil;
    private TextView nombre;
    private ImageButton envial_loc;
    private TextView userName;

    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceChat;
    private DatabaseReference databaseReferenceUsuario;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private Adapter_mensaje adapter_mensaje;

    private Calendar calendario = Calendar.getInstance();

    private LocationManager locationManager;

    private String fotoPerfilString;

    private FirebaseAuth mAuth;

    private String nombreUsuario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View vista =inflater.inflate(R.layout.fragment_chat, container, false);
        enviar=vista.findViewById(R.id.btn_enviar);
        texto_mensaje=vista.findViewById(R.id.Texto_mensaje);
        recyclerView=vista.findViewById(R.id.recycler);

       foto_perfil=vista.findViewById(R.id.FotoCambiarPerfil);


       nombre=vista.findViewById(R.id.UserNamePerfil);
       envial_loc=vista.findViewById(R.id.localizacion_env);
       userName=vista.findViewById(R.id.UserNamePerfil);
       mAuth=FirebaseAuth.getInstance();

       database=FirebaseDatabase.getInstance();
       databaseReferenceChat =database.getReference("chat");
       databaseReferenceUsuario =database.getReference("Usuarios/"+mAuth.getCurrentUser().getUid());

       storage= FirebaseStorage.getInstance();;

       Calendar calendario = Calendar.getInstance();

      fotoPerfilString="";

      mAuth=FirebaseAuth.getInstance();;

            adapter_mensaje=new Adapter_mensaje(getActivity());
            LinearLayoutManager l=new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(l);
            recyclerView.setAdapter(adapter_mensaje);

            //Gestion btn enviar localizacion
            envial_loc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String textMensaje=texto_mensaje.getText().toString();
                    if(!textMensaje.isEmpty()){
                        Mensaje mensaje =new Mensaje();
                        mensaje.setMensaje(textMensaje);
                        mensaje.setUserKey(UsuarioDAO.getKeyUsuario());

                        databaseReferenceChat.push().setValue(mensaje);
                        texto_mensaje.setText("");
                    }

                }
            });

        //Agregamos un ValueEventListener para que los cambios que se hagan en la base de datos
        //se reflejen en la aplicacion
        databaseReferenceUsuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //leeremos un objeto de tipo Usuario
                GenericTypeIndicator<Usuario> usuario = new GenericTypeIndicator<Usuario>() {
                };
                Usuario usuario1 = dataSnapshot.getValue(usuario);
                Uri uri=Uri.parse(usuario1.getFotoPerfilUrl());
                if(uri!=null) {
                    try {
                        Picasso.with(getActivity().getApplicationContext())
                                .load(uri.getPath()).resize(50,50)
                                .into(foto_perfil);
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    String textMensaje=texto_mensaje.getText().toString();
                    if(!textMensaje.isEmpty()){
                        Mensaje mensaje =new Mensaje();
                        mensaje.setMensaje(textMensaje);
                        mensaje.setUserKey(UsuarioDAO.getInstance().getKeyUsuario());

                        databaseReferenceChat.push().setValue(mensaje);
                        texto_mensaje.setText("");
                    }

                }
            });

            //Gestion de eventos producidos en FIREBASE
            databaseReferenceChat.addChildEventListener(new ChildEventListener() {

                Map<String, LUsuario> stringLUsuarioMap=new HashMap<>();
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final Mensaje m=dataSnapshot.getValue(Mensaje.class);
                    final LMensaje lMensaje=new LMensaje(m,dataSnapshot.getKey());
                    final int posicion=adapter_mensaje.addMensaje(lMensaje);

                    if(stringLUsuarioMap.get(m.getUserKey())!=null){
                        lMensaje.setlUsuario(stringLUsuarioMap.get(m.getUserKey()));
                        adapter_mensaje.actualizarMensaje(posicion,lMensaje);
                    }else{
                        UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                            @Override
                            public void devolverUsuario(LUsuario lUsuario) {
                                stringLUsuarioMap.put(m.getUserKey(),lUsuario);
                                lMensaje.setlUsuario(lUsuario);
                                adapter_mensaje.actualizarMensaje(posicion,lMensaje);
                            }

                            @Override
                            public void devolverError(String mensajeError) {
                                Toast.makeText(getActivity().getApplicationContext(),"Error"+ mensajeError,Toast.LENGTH_SHORT);
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
                    recyclerView.scrollToPosition(adapter_mensaje.getItemCount()-1);
                }
            });

        return vista;
        }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentuser=mAuth.getCurrentUser();

        if(currentuser!=null){
            DatabaseReference reference=database.getReference("Usuarios/"+currentuser.getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    Usuario usuario=snapshot.getValue(Usuario.class);
                    nombreUsuario=usuario.getUserName();
                    nombre.setText(nombreUsuario);
                    Picasso.with(getActivity().getApplicationContext())
                            .load(Uri.parse(usuario.getFotoPerfilUrl())).resize(50,50)
                            .into(foto_perfil);

                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }else{
            startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    //Funcion para obtener geolocalizacion actual
    public String  geolocalizacion() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{

                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }
        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        String localizacion =
                String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=%f,%f ", loc.getLatitude(),loc.getLongitude());


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


    public ImageView getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(ImageView foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
