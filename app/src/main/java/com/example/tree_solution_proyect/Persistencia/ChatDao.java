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
    private static ChatDao chatDao;
    private FirebaseDatabase database;
    private DatabaseReference referenceChats;
    private FirebaseAuth mAuth;
    private Libro chat;
    public ArrayList<LMensaje> lMensajes = new ArrayList<>();
    String res;

    public static ChatDao getInstance() {
        if (chatDao == null) {
            chatDao = new ChatDao();
        }
        return chatDao;
    }

    public ChatDao() {
        database = FirebaseDatabase.getInstance();
        referenceChats = database.getReference(Constantes.NODO_CHATS);
        mAuth = FirebaseAuth.getInstance();
    }

    public String getUltimoMensaje(String keyReceptor, String keyLibro) {

        DatabaseReference reference = database.getReference(Constantes.NODO_CHATS).child(mAuth.getCurrentUser().getUid()).child(keyReceptor).child(keyLibro);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Mensaje mensaje = dataSnapshot.getValue(Mensaje.class);
                    LMensaje lMensaje = new LMensaje(mensaje, snapshot.getKey());
                    lMensajes.add(lMensaje);
                    Log.i("pizda", "" + lMensajes.size());
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }

        });
        return res;

    }
    public String devolverRes(ArrayList<LMensaje> mensajes){
        if(mensajes.size()>0) {
            return mensajes.get(mensajes.size() - 1).getMensaje().getMensaje();
        }
        return null;
    }
}
