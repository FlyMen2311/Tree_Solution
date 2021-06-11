package com.example.tree_solution_proyect.Vistas.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.core.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.Adaptadores.Adapter_mensaje;
import com.example.tree_solution_proyect.LibroClickActivity;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.AplicationActivity;
import com.example.tree_solution_proyect.Vistas.Login;
import com.example.tree_solution_proyect.Vistas.MainActivity;
import com.example.tree_solution_proyect.Vistas.Registro;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class HomeFragment extends Fragment implements LibrosClickablesIntefrace{
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceLibro;
    private DatabaseReference databaseReferenceUsuario;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Adapter_Libro adapter_libro;
    private Calendar calendario = Calendar.getInstance();
    private FirebaseAuth mAuth;
    public static List<LLibro> libroList;
    public View vista;
    private Context context;


    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=vista.findViewById(R.id.recycler_home);
        mAuth=FirebaseAuth.getInstance();





        database=FirebaseDatabase.getInstance();
        databaseReferenceLibro =database.getReference("Libros");
        databaseReferenceUsuario =database.getReference("Usuarios/"+mAuth.getCurrentUser().getUid());

        storage= FirebaseStorage.getInstance();;
        libroList=new ArrayList<LLibro>();
        Calendar calendario = Calendar.getInstance();

        adapter_libro=new Adapter_Libro(getActivity());
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


        databaseReferenceLibro.addChildEventListener(new ChildEventListener() {
            Map<String, LUsuario> stringLUsuarioMap=new HashMap<>();
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                final Libro m=snapshot.getValue(Libro.class);
                final LLibro lLibro=new LLibro(m,snapshot.getKey());
                final int posicion=adapter_libro.addLibro(lLibro);


                if(stringLUsuarioMap.get(m.getUserKey())!=null){
                    lLibro.setLUsuario(stringLUsuarioMap.get(m.getUserKey()));
                    adapter_libro.actualizarLibro(posicion,lLibro);

                }else{
                    UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            stringLUsuarioMap.put(m.getUserKey(),lUsuario);
                            lLibro.setLUsuario(lUsuario);
                            adapter_libro.actualizarLibro(posicion,lLibro);
                            libroList.add(lLibro);
                        }

                        @Override
                        public void devolverError(String mensajeError) {
                            Toast.makeText(getActivity().getApplicationContext(),"Error"+ mensajeError,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

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






    @Override
    public void LibroClick(int pos,ImageView imgcontainer,ImageView fotoLibro,TextView nombre,
                           TextView autor,TextView precio,TextView ISBN, TextView categoria,
                                          RatingBar ratingBar,TextView estado, TextView fechacreacion,ImageView favorite) {
        Intent intent=new Intent();
        Libro libro=libroList.get(pos).getLibro();
        if(libro!=null) {
            intent.putExtra("objectLibro", libro);
        }else{
            Toast.makeText(getActivity(),"Pizda zaibalo",Toast.LENGTH_LONG).show();
        }
        Pair<View,String> p1=Pair.create((View) imgcontainer,"container_holder_libro");
        Pair<View,String> p2=Pair.create((View) fotoLibro,"fotolibro");
        Pair<View,String> p3=Pair.create((View) nombre,"nombre");
        Pair<View,String> p4=Pair.create((View) autor,"autor");
        Pair<View,String> p5=Pair.create((View) precio,"precio");
        Pair<View,String> p6=Pair.create((View) ISBN,"ISBN");
        Pair<View,String> p7=Pair.create((View) categoria,"categoria");
        Pair<View,String> p8=Pair.create((View) ratingBar,"ratingbar");
        Pair<View,String> p9=Pair.create((View) estado,"condition");
        Pair<View,String> p10=Pair.create((View) fechacreacion,"fechacreacion");
        Pair<View,String> p11=Pair.create((View) favorite ,"favorite");

       
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11);
            startActivity(intent, activityOptionsCompat.toBundle());

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