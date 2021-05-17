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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_mensaje;
import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
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
    private DatabaseReference databaseReference;

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
        enviar=vista.findViewById(R.id.btn_enviar);;
        texto_mensaje=vista.findViewById(R.id.Texto_mensaje);;
        recyclerView=vista.findViewById(R.id.recycler);;

       foto_perfil=vista.findViewById(R.id.FotoPerfil);
       foto_perfil.setImageResource(R.drawable.logo);

       nombre=vista.findViewById(R.id.nombreMensaje);;
       envial_loc=vista.findViewById(R.id.localizacion_env);;
       userName=vista.findViewById(R.id.nombreMensaje);;

       database=FirebaseDatabase.getInstance();;
       databaseReference=database.getReference("chat");;

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

                        databaseReference.push().setValue(mensaje);
                        texto_mensaje.setText("");
                    }

                }
            });

            //Gestion btn cambiar foto perfil
            foto_perfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/jpg");
                    i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
                    startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),2311);
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

                        databaseReference.push().setValue(mensaje);
                        texto_mensaje.setText("");
                    }

                }
            });

            //Gestion de eventos producidos en FIREBASE
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Mensaje m=dataSnapshot.getValue(Mensaje.class);
                    LMensaje lMensaje=new LMensaje(m,dataSnapshot.getKey());
                    adapter_mensaje.addMensaje(lMensaje);
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

    //Recoger datos de Intent producido en esta clase
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2311 && resultCode == RESULT_OK){

            Uri x = data.getData();
            storageReference = storage.getReference("foto_perfil");
            final StorageReference fotoReferencia = storageReference.child(x.getLastPathSegment());
            fotoReferencia.putFile(x).addOnSuccessListener((Executor) ComunicacionFragment.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> u = taskSnapshot.getStorage().getDownloadUrl();
                    u.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fotoPerfilString = uri.toString();
                           // Mensaje m = new Mensaje(nombreUsuario,""+calendario.get(Calendar.HOUR_OF_DAY)+":"+calendario.get(Calendar.MINUTE)+"","Foto ha sido actualizada",fotoPerfilString);
                            //databaseReference.push().setValue(m);
                            //Glide.with(MainActivity.this).load(fotoPerfilString).into(foto_perfil);
                        }
                    });

                }
            });
        }
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
