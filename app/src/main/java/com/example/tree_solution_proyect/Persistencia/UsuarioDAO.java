package com.example.tree_solution_proyect.Persistencia;

import androidx.annotation.NonNull;

import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static UsuarioDAO usuarioDAO;
    private FirebaseDatabase database;
    private DatabaseReference referenceUsuarios;

    public static UsuarioDAO getInstance(){
        if(usuarioDAO==null){
            usuarioDAO=new UsuarioDAO();
        }
        return usuarioDAO;
    }

    public UsuarioDAO(){
        database=FirebaseDatabase.getInstance();
        referenceUsuarios= database.getReference(Constantes.NODO_USUARIOS);
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
}
