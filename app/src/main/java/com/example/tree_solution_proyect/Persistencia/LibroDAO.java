package com.example.tree_solution_proyect.Persistencia;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LibroDAO {
    private static LibroDAO libroDAO;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageReferenceFotoLibro;
    private DatabaseReference referenceLibros;
    private String key;

    public static LibroDAO getInstance(){
        if(libroDAO==null){
            libroDAO=new LibroDAO();
        }
        return libroDAO;
    }
    public LibroDAO(){
        database= FirebaseDatabase.getInstance();
        referenceLibros = database.getReference(Constantes.NODO_LIBROS);
        storage= FirebaseStorage.getInstance();
        DatabaseReference databaseReference = database.getInstance().getReference();
        Query lastQuery = databaseReference.child("Libros").orderByKey().limitToLast(1);

        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                key = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        storageReferenceFotoLibro =storage.getReference("Fotos/FotoLibros/"+key);
    }
    public void obtenerInformacionKey(final String key,final UsuarioDAO.IDevolverUsuario iDevolverUsuario){
        referenceLibros.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
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
    public void cambiarFotoUri(Uri uri1, UsuarioDAO.IDevolverUrlFoto iDevolverUrlFoto) {
        String nombreFoto = "";
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("SSSS.ss-mm-hh-dd-MM-yyyy", Locale.getDefault());
        nombreFoto = simpleDateFormat.format(date);
        final StorageReference fotoReferencia = storageReferenceFotoLibro.child(nombreFoto);
        fotoReferencia.putFile(uri1).continueWith((task) -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return fotoReferencia.getDownloadUrl();
        }).addOnCompleteListener(new OnCompleteListener<Task<Uri>>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Task<Uri>> task) {
                if (task.isSuccessful()) {
                    iDevolverUrlFoto.DevolverUrlFoto(uri1.toString());
                }
            }
        });

    }
}
