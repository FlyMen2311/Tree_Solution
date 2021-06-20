package com.example.tree_solution_proyect.Persistencia;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.security.auth.callback.Callback;

public class LibroDAO {
    private static LibroDAO libroDAO;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReferenceFotoLibro;
    private DatabaseReference referenceLibros;
    private String key;
    private Libro libro;
    public boolean isExist=false;

    public static LibroDAO getInstance() {
        if (libroDAO == null) {
            libroDAO = new LibroDAO();
        }
        return libroDAO;
    }
    public LibroDAO() {
        database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        referenceLibros = database.getReference(Constantes.NODO_LIBROS);
        storage = FirebaseStorage.getInstance();
        storageReferenceFotoLibro = storage.getReference("Fotos/FotoLibros");
    }

    public interface IDevolverBooleanExist{
        void devolverExist(boolean isExist);
        void devolverError(String mensajeError);
    }
    public static String getKeyUsuario() {
        return FirebaseAuth.getInstance().getUid();
    }

    public void cambiarFotoUri(Uri uri1, String key, UsuarioDAO.IDevolverUrlFoto iDevolverUrlFoto) {
        String nombreFoto = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombreFoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = storageReferenceFotoLibro.child(key).child(nombreFoto);
        fotoReferencia.putFile(uri1).continueWith((task) -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return fotoReferencia.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    iDevolverUrlFoto.DevolverUrlFoto(uri.toString());
                }
            });
        });

    }
  

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public void crearLibroFavorito(LLibro lLibro){
        database.getReference(Constantes.NODO_LIB_FAV).child(mAuth.getCurrentUser().getUid()).child(lLibro.getKey()).setValue(lLibro.getLibro());
    }
    public void eliminarLibroFavorito(LLibro lLibro){
        database.getReference(Constantes.NODO_LIB_FAV).child(mAuth.getCurrentUser().getUid()).child(lLibro.getKey()).removeValue();
    }

    public void libroExistFavoritos(LLibro lLibro, IDevolverBooleanExist iDevolverBooleanExist){
     DatabaseReference reference=database.getReference(Constantes.NODO_LIB_FAV).child(mAuth.getCurrentUser().getUid()).child(lLibro.getKey());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
               if(snapshot.exists()){
                   isExist=true;
                   iDevolverBooleanExist.devolverExist(isExist);
               }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                iDevolverBooleanExist.devolverError(error.getMessage());
            }
        });

    }

}
