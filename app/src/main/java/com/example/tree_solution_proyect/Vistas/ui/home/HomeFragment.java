package com.example.tree_solution_proyect.Vistas.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.util.Pair;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Vistas.LibroClickActivity;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.Login;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceLibro;
    private DatabaseReference databaseReferenceChatDatos;


    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Adapter_Libro adapter_libro;
    private FirebaseAuth mAuth;
    private String keyEmisor;
    private String keyreceptor;
    private String keyLibro;
    private int posicion = 0;
    private EditText buscar_librosISBN;
    private boolean isFavorite;
    public View vista;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=vista.findViewById(R.id.recycler_home);
        buscar_librosISBN=vista.findViewById(R.id.buscar_libro_isbn);
        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        databaseReferenceLibro = database.getReference(Constantes.NODO_LIBROS);
        databaseReferenceChatDatos=database.getReference(Constantes.NODO_CHAT_DATOS);

        storage= FirebaseStorage.getInstance();;

        adapter_libro=new Adapter_Libro(getActivity().getApplicationContext(),new LibroOpen(getActivity(),getContext()));

        LinearLayoutManager l=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter_libro);

        //Funcion para pasar al ultimo mensaje producido
        adapter_libro.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter_libro.getItemCount()-1);
            }
        });

        buscar_librosISBN.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter_libro.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        databaseReferenceLibro.addChildEventListener(new ChildEventListener() {
            Map<String, LUsuario> stringLUsuarioMap=new HashMap<>();

            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot,
                                     @Nullable @org.jetbrains.annotations.Nullable
                                             String previousChildName) {
                final Libro m = snapshot.getValue(Libro.class);
                final LLibro lLibro = new LLibro(m, snapshot.getKey());

                if(lLibro.getLibro().getEsVendido().equals("No")){
                final int posicion = adapter_libro.addLibro(lLibro);//listFilter
                adapter_libro.addLibroAll(lLibro);//listAll

                if (stringLUsuarioMap.get(m.getUserKey()) != null) {
                    lLibro.setLUsuario(stringLUsuarioMap.get(m.getUserKey()));
                    adapter_libro.actualizarLibro(posicion, lLibro);
                } else {
                    UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            stringLUsuarioMap.put(m.getUserKey(), lUsuario);
                            lLibro.setLUsuario(lUsuario);
                            adapter_libro.actualizarLibro(posicion, lLibro);
                        }

                        @Override
                        public void devolverError(String mensajeError) {
                            Toast.makeText(getActivity().getApplicationContext(), "Error" + mensajeError, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                final Libro m=snapshot.getValue(Libro.class);
                final LLibro lLibro=new LLibro(m,snapshot.getKey());

                for(int i=0;i<adapter_libro.getListLibros().size();i++){
                    if(adapter_libro.getListLibros().get(i).getLibro().getReferenceStorage().equals(lLibro.getLibro().getReferenceStorage())){
                        posicion=i;
                    }
                }

                database.getReference(Constantes.NODO_LIB_FAV).child(mAuth.getCurrentUser().getUid()).child(adapter_libro.getListLibros().get(posicion).getKey()).removeValue();


                   databaseReferenceChatDatos.addListenerForSingleValueEvent(new ValueEventListener() {

                       DataSnapshot snapshot1;
                       DataSnapshot snapshot2;
                       DataSnapshot snapshot3;
                       boolean keyExiste = false;

                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            this.snapshot1=snapshot1;
                            keyEmisor=snapshot1.getKey();
                            for(DataSnapshot snapshot2:this.snapshot1.getChildren()) {
                                this.snapshot2 = snapshot2;
                                keyreceptor = snapshot2.getKey();
                                for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                                    keyExiste = false;
                                    this.snapshot3 = snapshot3;
                                    keyLibro = snapshot3.getKey();

                                    if (adapter_libro.getListLibrosAll().size() != 0) {
                                        int a = adapter_libro.getListLibrosAll().size();
                                        for (int i = 0; i < adapter_libro.getListLibrosAll().size(); i ++) {
                                            if (keyLibro.equals(adapter_libro.getListLibrosAll().get(i).getKey())) {
                                                keyExiste = true;
                                            }
                                        }
                                            if (!keyExiste) {
                                                database.getReference(Constantes.NODO_CHATS).child(keyEmisor).child(keyreceptor).child(keyLibro).removeValue();
                                                database.getReference(Constantes.NODO_CHAT_DATOS).child(keyEmisor).child(keyreceptor).child(keyLibro).removeValue();
                                                keyExiste = false;
                                            }

                                    } else {

                                        database.getReference(Constantes.NODO_CHATS).child(keyEmisor).child(keyreceptor).child(keyLibro).removeValue();
                                        database.getReference(Constantes.NODO_CHAT_DATOS).child(keyEmisor).child(keyreceptor).child(keyLibro).removeValue();
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
                adapter_libro.getListLibros().remove(posicion);

                adapter_libro.getListLibrosAll().remove(posicion);

                adapter_libro.notifyItemRemoved(posicion);

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        return vista;
    }

    public Adapter_Libro getAdapter_libro() {
        return adapter_libro;
    }

    public void setAdapter_libro(Adapter_Libro adapter_libro) {
        this.adapter_libro = adapter_libro;
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentuser=mAuth.getCurrentUser();

        if(currentuser!=null){
            adapter_libro.notifyDataSetChanged();
        }else{
            startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public class LibroOpen  implements LibrosClickablesIntefrace {
        Activity activity;
        Context context;

        public LibroOpen(Activity activity,Context context) {
            this.activity=activity;
            this.context=context;
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
        public void LibroClick (int pos, ImageView imgcontainer, ImageView fotoLibro, TextView nombre,
                                TextView autor, TextView precio, TextView ISBN, TextView categoria,
                                RatingBar ratingBar, TextView estado, TextView fechacreacion,
                                ImageView favorite, TextView descripcion){

            try {
                Intent intent = new Intent(activity, LibroClickActivity.class);
                LLibro llibro = adapter_libro.getListLibros().get(pos);

                if (llibro != null) {
                    if(favorite.getBackground().getConstantState().equals(favorite.getContext().getDrawable(R.drawable.favorite).getConstantState())){
                        isFavorite=false;
                        intent.putExtra("isFavorite", isFavorite);

                    }else if(favorite.getBackground().getConstantState().equals(favorite.getContext().getDrawable(R.drawable.favorite_libro).getConstantState())){
                        isFavorite=true;
                        intent.putExtra("isFavorite", isFavorite);
                    }
                    intent.putExtra("objectLibro", llibro);

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, Pair.create((View) imgcontainer, " container_holder_libro")
                            ,Pair.create((View) fotoLibro, "fotolibro_TR")
                            ,Pair.create((View) nombre, "nombre_TR")
                            ,Pair.create((View) autor, "autor_TR")
                            ,Pair.create((View) precio, "precio_TR")
                            ,Pair.create((View) ISBN, "ISBN_TR")
                            ,Pair.create((View) categoria, "categoria_TN")
                            ,Pair.create((View) ratingBar, "ratingbar_TR")
                            ,Pair.create((View) estado, "condition_TR")
                            ,Pair.create((View) fechacreacion, "fechacreacion_TR")
                            ,Pair.create((View) favorite, "favorite_TR"));

                    startActivity(intent, activityOptionsCompat.toBundle());

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
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}