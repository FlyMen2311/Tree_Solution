package com.example.tree_solution_proyect.Persistencia;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatDao {
    //Instanciamos los atributos
    private static ChatDao chatDao;
    private FirebaseDatabase database;
    private DatabaseReference referenceChats;
    private FirebaseAuth mAuth;
    private Libro chat;
    public ArrayList<LMensaje> lMensajes = new ArrayList<>();
    String res;
    //Singelton
    public static ChatDao getInstance() {
        if (chatDao == null) {
            chatDao = new ChatDao();
        }
        return chatDao;
    }
    //Metodo contructor
    public ChatDao() {
        database = FirebaseDatabase.getInstance();
        referenceChats = database.getReference(Constantes.NODO_CHATS);
        mAuth = FirebaseAuth.getInstance();
    }
    //Interface Devolver Ultimo Mensaje
    public interface IDevolverUltimoMensaje{
        void DevolverUltimoMensaje(ArrayList<LMensaje> mensaje);
        void devolverError(String mensajeError);
    }
    //Funcion para devolver ultimo mensaje producido en los chats
    public String getUltimoMensaje(String keyReceptor, String keyLibro, IDevolverUltimoMensaje iDevolverBooDevolverUltimoMensaje) {

        DatabaseReference reference = database.getReference(Constantes.NODO_CHATS).child(mAuth.getCurrentUser().getUid()).child(keyReceptor).child(keyLibro);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                    LMensaje lMensaje = new LMensaje(mensaje, snapshot.getKey());
                    lMensajes.add(lMensaje);
                }

                if(lMensajes.size()!=0) {
                    iDevolverBooDevolverUltimoMensaje.DevolverUltimoMensaje(lMensajes);
                }else{
                    iDevolverBooDevolverUltimoMensaje.DevolverUltimoMensaje(null);
                }
                lMensajes.clear();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                iDevolverBooDevolverUltimoMensaje.devolverError(error.getMessage());
            }

        });
        return res;

    }

}
