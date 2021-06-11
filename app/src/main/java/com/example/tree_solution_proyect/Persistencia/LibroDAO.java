package com.example.tree_solution_proyect.Persistencia;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


    public static LibroDAO getInstance() {
        if (libroDAO == null) {
            libroDAO = new LibroDAO();
        }
        return libroDAO;
    }
    public interface IDevolverLibro{
        void devolverLibro(LLibro lLibro);
         void devolverError(String mensajeError);
    }

    public LibroDAO() {
        database = FirebaseDatabase.getInstance();
        referenceLibros = database.getReference(Constantes.NODO_LIBROS);
        storage = FirebaseStorage.getInstance();
        storageReferenceFotoLibro = storage.getReference("Fotos/FotoLibros");
    }

    public void obtenerInformacionKeyLibro(final String key, final LibroDAO.IDevolverLibro iDevolverLibro) {
        referenceLibros.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Libro libro = snapshot.getValue(Libro.class);
                LLibro lLibro = new LLibro(libro, key);
                iDevolverLibro.devolverLibro(lLibro);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                iDevolverLibro.devolverError(error.getMessage());
            }
        });
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

}
