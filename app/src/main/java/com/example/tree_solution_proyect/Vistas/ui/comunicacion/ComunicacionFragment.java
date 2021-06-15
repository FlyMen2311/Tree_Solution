package com.example.tree_solution_proyect.Vistas.ui.comunicacion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Chats;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Chat;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LChat;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ChatsClick;
import com.example.tree_solution_proyect.Vistas.Login;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ComunicacionFragment extends Fragment{

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceChats;
    private DatabaseReference databaseReferenceChatsUsuario;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Adapter_Chats adapter_chats;
    private Calendar calendario = Calendar.getInstance();
    private FirebaseAuth mAuth;
    private static List<LChat> chatsListClick;
    public String keyreceptor;
    public String keyemisor;
    public String keylibro;
    private EditText buscar_chats_nombre;
    public View vista;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista =inflater.inflate(R.layout.layout_chats, container, false);
        recyclerView=vista.findViewById(R.id.recycler_chats);
        buscar_chats_nombre=vista.findViewById(R.id.buscar_chats_nombre);

       mAuth=FirebaseAuth.getInstance();

       database=FirebaseDatabase.getInstance();
       databaseReferenceChats =database.getReference(Constantes.NODO_CHAT_DATOS);
       databaseReferenceChatsUsuario=database.getReference(Constantes.NODO_CHAT_DATOS).child(mAuth.getCurrentUser().getUid());

        storage= FirebaseStorage.getInstance();;

        chatsListClick=new ArrayList<LChat>();


      mAuth=FirebaseAuth.getInstance();;

            adapter_chats=new Adapter_Chats(getActivity().getApplicationContext(),new ChatOpen(getActivity(),getContext()));
            LinearLayoutManager l=new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(l);
            recyclerView.setAdapter(adapter_chats);




              //Gestion de eventos producidos en FIREBASE
              databaseReferenceChatsUsuario.addChildEventListener(new ChildEventListener() {
                  private DataSnapshot receptorsnapshot;
                  private DataSnapshot librosnapshot;
                  private DataSnapshot emisorsnashot;
                  private long cont = 0;
                  private long cont_libros=1;
                  @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                      keyreceptor=dataSnapshot.getKey();
                      for (DataSnapshot librosnapshot : dataSnapshot.getChildren()) {
                          this.librosnapshot = librosnapshot;
                          keylibro = librosnapshot.getKey();

                          for (DataSnapshot chatsnapshot : librosnapshot.getChildren()) {

                              Chat m = chatsnapshot.getValue(Chat.class);
                              final LChat lChat = new LChat(m, chatsnapshot.getKey());
                              final int posicion = adapter_chats.addChat(lChat);
                              chatsListClick.add(lChat);
                              cont++;
                          }
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
            adapter_chats.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    recyclerView.scrollToPosition(adapter_chats.getItemCount()-1);
                }
            });

        return vista;
        }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentuser=mAuth.getCurrentUser();

        if(currentuser!=null){

        }else{
            startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    public class ChatOpen  implements ChatClickableInterface {
        Activity activity;
        Context context;

        public ChatOpen(Activity activity, Context context) {
            this.activity = activity;
            this.context = context;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }




        @Override
        public void ChatClick(int pos,LinearLayout linearLayout, ImageView Foto_libro_chat, TextView name_libro_chat, TextView ultimo_mensaje, TextView name_propietario, TextView hora) {
            try {
                Intent intent = new Intent(activity, ChatsClick.class);
                LChat lChat = chatsListClick.get(pos);
                intent.putExtra(("keyemisor"),lChat.getChat().getKeyemisor());
                intent.putExtra("keyreceptor",lChat.getChat().getKeyreceptor());
                intent.putExtra("keylibro",lChat.getChat().getKeylibro());


                if (lChat != null) {

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create((View) linearLayout, " container_holder_chat_libro")
                            , Pair.create((View) Foto_libro_chat, "fotolibro_TR")
                            , Pair.create((View) name_libro_chat, "nombre_chat_TR")
                            , Pair.create((View) ultimo_mensaje, "autor_chat_TR")
                            , Pair.create((View) name_propietario, "precio_chat_TR")
                            , Pair.create((View) hora, "fechacreacion_chat_TR"));

                    startActivity(intent);
                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                }
            } catch (Exception exception) {
                Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
