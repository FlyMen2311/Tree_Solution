package com.example.tree_solution_proyect.Vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tree_solution_proyect.Adapter_mensaje;
import com.example.tree_solution_proyect.Objetos.Mensaje;
import com.example.tree_solution_proyect.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatActivity extends AppCompatActivity  {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enviar=findViewById(R.id.btn_enviar);
        texto_mensaje=findViewById(R.id.Texto_mensaje);
        userName=findViewById(R.id.nombreMensaje);

        recyclerView=findViewById(R.id.recycler);
        foto_perfil=findViewById(R.id.FotoPerfil);
        foto_perfil.setImageResource(R.drawable.logo);
        fotoPerfilString="";

        nombre=findViewById(R.id.nombreMensaje);
        envial_loc=findViewById(R.id.localizacion_env);

        database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference("message");
        storage = FirebaseStorage.getInstance();

        adapter_mensaje=new Adapter_mensaje(getApplicationContext());
        LinearLayoutManager l=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter_mensaje);

        //Creo un AlertDialog para recoger datos
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Introduce su userName");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombre=input.getText().toString();
                if(!nombre.isEmpty()) {
                    userName.setText(nombre.toString());
                }else{
                    userName.setText("Desconocido");
                    userName.setTextColor(Color.RED);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                userName.setText("Desconocido");
                userName.setTextColor(Color.RED);
            }
        });

        builder.show();
        //Fin lectura Nombre




        //Gestion btn enviar localizacion
        envial_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.push().setValue(new Mensaje(nombre.getText().toString(),""+calendario.get(Calendar.HOUR_OF_DAY)+":"+calendario.get(Calendar.MINUTE)+"","Es mi Ubicacion: "+geolocalizacion(),fotoPerfilString));
                texto_mensaje.setText("");
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
                databaseReference.push().setValue(new Mensaje(nombre.getText().toString(),""+calendario.get(Calendar.HOUR_OF_DAY)+":"+calendario.get(Calendar.MINUTE)+"",texto_mensaje.getText().toString(),fotoPerfilString));
                texto_mensaje.setText("");
            }
        });

        //Gestion de eventos producidos en FIREBASE
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Mensaje m=dataSnapshot.getValue(Mensaje.class);
                adapter_mensaje.addMensaje(m);
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
    }

    //Funcion para obtener geolocalizacion actual
    public String  geolocalizacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{

                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2311 && resultCode == RESULT_OK){

            Uri x = data.getData();
            storageReference = storage.getReference("foto_perfil");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(x.getLastPathSegment());
            fotoReferencia.putFile(x).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> u = taskSnapshot.getStorage().getDownloadUrl();
                    u.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fotoPerfilString = uri.toString();
                            Mensaje m = new Mensaje(nombre.getText().toString(),""+calendario.get(Calendar.HOUR_OF_DAY)+":"+calendario.get(Calendar.MINUTE)+"","Foto ha sido actualizada",fotoPerfilString);
                            databaseReference.push().setValue(m);
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


}
