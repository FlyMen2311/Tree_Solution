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
import com.example.tree_solution_proyect.LibroClickActivity;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Adapter_Libro adapter_libro;
    private Calendar calendario = Calendar.getInstance();
    private FirebaseAuth mAuth;
    public static List<LLibro> libroListClick;
    private EditText buscar_librosISBN;
    public View vista;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=vista.findViewById(R.id.recycler_home);
        buscar_librosISBN=vista.findViewById(R.id.buscar_libro_Isbn);
        mAuth=FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        databaseReferenceLibro =database.getReference("Libros");


        storage= FirebaseStorage.getInstance();;
        libroListClick =new ArrayList<LLibro>();
        Calendar calendario = Calendar.getInstance();



        adapter_libro=new Adapter_Libro(getActivity().getApplicationContext(),
                new LibroOpen(getActivity(),getContext()));
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
                final Libro m=snapshot.getValue(Libro.class);
                final LLibro lLibro=new LLibro(m,snapshot.getKey());
                final int posicion=adapter_libro.addLibro(lLibro);
                adapter_libro.addLibroAll(lLibro);

                if(stringLUsuarioMap.get(m.getUserKey())!=null){
                    lLibro.setLUsuario(stringLUsuarioMap.get(m.getUserKey()));
                    adapter_libro.actualizarLibro(posicion,lLibro);
                    libroListClick.add(lLibro);
                }else{
                    UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            stringLUsuarioMap.put(m.getUserKey(),lUsuario);
                            lLibro.setLUsuario(lUsuario);
                            adapter_libro.actualizarLibro(posicion,lLibro);
                            libroListClick.add(lLibro);
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
                final Libro m=snapshot.getValue(Libro.class);
                final LLibro lLibro=new LLibro(m,snapshot.getKey());
                int posicion = 0;
                for(int i=0;i<adapter_libro.getListLibros().size();i++){
                    if(adapter_libro.getListLibros().get(i).getLibro().getReferenceStorage().equals(lLibro.getLibro().getReferenceStorage())){
                        posicion=i;
                    }
                }
                            adapter_libro.getListLibros().remove(posicion);
                            adapter_libro.getListLibrosAll().remove(posicion);
                            libroListClick.remove(posicion);

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
        public void LibroClick ( int pos, ImageView imgcontainer, ImageView fotoLibro,
                                 TextView nombre, TextView autor, TextView precio, TextView ISBN,
                                 TextView categoria,RatingBar ratingBar, TextView estado,
                                 TextView fechacreacion, ImageView favorite, TextView descripcion){
            try {
                Intent intent = new Intent(activity, LibroClickActivity.class);
                LLibro llibro = libroListClick.get(pos);
                if (llibro != null) {
                    intent.putExtra("objectLibro", llibro);
                    ActivityOptionsCompat activityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                    Pair.create((View) imgcontainer, " container_holder_libro")
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