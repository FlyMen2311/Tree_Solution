package com.example.tree_solution_proyect.Persistencia;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Vistas.ui.perfil.PerfilFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class UsuarioDAO {
    private static UsuarioDAO usuarioDAO;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReferenceFotoPerfil;
    private DatabaseReference referenceUsuarios;


    public interface IDevolverUsuario{
        public void devolverUsuario(LUsuario lUsuario);
        public void devolverError(String mensajeError);
    }
    public interface IDevolverUrlFoto{
        public void DevolverUrlFoto(String uri);
    }


    public static UsuarioDAO getInstance(){
        if(usuarioDAO==null){
            usuarioDAO=new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public UsuarioDAO(){
        database=FirebaseDatabase.getInstance();
        referenceUsuarios= database.getReference(Constantes.NODO_USUARIOS);
        storage=FirebaseStorage.getInstance();
        storageReferenceFotoPerfil=storage.getReference("Fotos/FotoPerfil/"+getKeyUsuario());
    }
    public void obtenerInformacionKey(final String key,final IDevolverUsuario iDevolverUsuario){
        referenceUsuarios.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Usuario usuario=snapshot.getValue(Usuario.class);
                LUsuario lUsuario=new LUsuario(usuario,key);
                iDevolverUsuario.devolverUsuario(lUsuario);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                iDevolverUsuario.devolverError(error.getMessage());
            }
        });
    }

    public static String getKeyUsuario(){
        return FirebaseAuth.getInstance().getUid();
    }
    public long fechaDeCreacion(){
     return  FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
    }
    public long fechaUltimoLogIn(){
        return  FirebaseAuth.getInstance().getCurrentUser().getMetadata().getLastSignInTimestamp();
    }
    public void DefaulFotoPerfil(){
        referenceUsuarios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                List<LUsuario> lUsuarioList=new ArrayList<LUsuario>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   Usuario usuario=dataSnapshot.getValue(Usuario.class);
                   LUsuario lUsuario=new LUsuario(usuario,dataSnapshot.getKey());
                   lUsuarioList.add(lUsuario);

                   for(LUsuario lUsuario1:lUsuarioList){
                       if(lUsuario1.getUsuario().getFotoPerfilUrl()==null){
                           referenceUsuarios.child(lUsuario1.getKey()).child("fotoPerfilUrl").setValue(Constantes.URL_FOTO_PERFIL);
                       }
                   }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void cambiarFotoUri(Uri uri1,IDevolverUrlFoto iDevolverUrlFoto){
        String nombreFoto="";
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("SSSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombreFoto=simpleDateFormat.format(date);
        final StorageReference fotoReferencia = storageReferenceFotoPerfil.child(nombreFoto);
        fotoReferencia.putFile(uri1).continueWith((task) -> {
            if(!task.isSuccessful()){
                throw task.getException();
            }
            return fotoReferencia.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Task<Uri>> task) {
                if(task.isSuccessful()){
                    Task<Uri> uri=task.getResult();

                    iDevolverUrlFoto.DevolverUrlFoto(uri1.toString());
                }
            }
        });

    }

  }

