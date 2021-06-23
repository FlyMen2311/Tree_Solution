package com.example.tree_solution_proyect.Vistas.ui.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Favoritos;
import com.example.tree_solution_proyect.Objetos.Constantes;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.LibroClickActivity;
import com.example.tree_solution_proyect.Vistas.Login;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class FavoriteFragment extends Fragment {
    //Instanciamos los atributos
    private View vista;
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private DatabaseReference databaseReferenceLibrosFavoritos;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Adapter_Favoritos adapter_favoritos;
    private Calendar calendario = Calendar.getInstance();
    private FirebaseAuth mAuth;
    public  boolean isFavorite;

    //Llama a este metodo al crear View aqui es donde se inician todos los atributos(Metodo Constructor que returna la vista creada)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        vista =inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView=vista.findViewById(R.id.recycler_favoritos);
        mAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        databaseReferenceLibrosFavoritos =database.getReference(Constantes.NODO_LIB_FAV).child(mAuth.getCurrentUser().getUid());
        storage= FirebaseStorage.getInstance();;
        adapter_favoritos=new Adapter_Favoritos(getActivity().getApplicationContext(), new LibroOpenFav(getActivity(),getContext()));
        LinearLayoutManager l=new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter_favoritos);
        //Funcion para pasar al ultimo mensaje producido
        adapter_favoritos.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter_favoritos.getItemCount()-1);
            }
        });
        //Gestion de Eventos producidos en Firebase sobre la tabla Favoritos
        databaseReferenceLibrosFavoritos.addChildEventListener(new ChildEventListener() {
            Map<String, LUsuario> stringLUsuarioMap=new HashMap<>();

            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                final Libro m=snapshot.getValue(Libro.class);
                final LLibro lLibro=new LLibro(m,snapshot.getKey());

                final int posicion=adapter_favoritos.addLibro(lLibro);//listFilter

                if(stringLUsuarioMap.get(m.getUserKey())!=null){
                    lLibro.setLUsuario(stringLUsuarioMap.get(m.getUserKey()));
                    adapter_favoritos.actualizarLibro(posicion,lLibro);
                }else{
                    UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                        @Override
                        public void devolverUsuario(LUsuario lUsuario) {
                            stringLUsuarioMap.put(m.getUserKey(),lUsuario);
                            lLibro.setLUsuario(lUsuario);
                            adapter_favoritos.actualizarLibro(posicion,lLibro);
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
                try {
                } catch (Exception e) {
                adapter_favoritos.notifyDataSetChanged();
                 }
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                    final Libro m = snapshot.getValue(Libro.class);
                    final LLibro lLibro = new LLibro(m, snapshot.getKey());
                    int posicion = 0;
                    for (int i = 0; i < adapter_favoritos.getListLibros().size(); i++) {
                        if (adapter_favoritos.getListLibros().get(i).getLibro().getReferenceStorage().equals(lLibro.getLibro().getReferenceStorage())) {
                            posicion = i;
                        }
                    }
                    adapter_favoritos.getListLibros().remove(posicion);
                    adapter_favoritos.notifyItemRemoved(posicion);

            }


            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                adapter_favoritos.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                adapter_favoritos.notifyDataSetChanged();
            }
        });
        return vista;


    }
    //Get y Set Adapter
    public Adapter_Favoritos getAdapter_libro() {
        return adapter_favoritos;
    }

    public void setAdapter_libro(Adapter_Favoritos adapter_favoritos) {
        this.adapter_favoritos = adapter_favoritos;
    }
    //Llama al este metodo cuando se restablece nuestro View
    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentuser=mAuth.getCurrentUser();

        if(currentuser!=null){
            adapter_favoritos.notifyDataSetChanged();
        }else{
            startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
            try {
                finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
    //Clase que gestiona evento producido al hacer click en el Libro Favorito
    public class LibroOpenFav  implements LibrosFavClickableInteface {
        Activity activity;
        Context context;

        public LibroOpenFav(Activity activity,Context context) {
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
        public void LibroClick2 (int pos, ImageView imgcontainer, ImageView fotoLibro, TextView nombre, TextView autor, TextView precio, TextView ISBN, TextView categoria,
                                RatingBar ratingBar, TextView estado, TextView fechacreacion, ImageView favorite){

            try {
                Intent intent = new Intent(activity, LibroClickActivity.class);
                LLibro llibro = adapter_favoritos.getListLibros().get(pos);

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

    //Llama a este metodo cuando se finaliza un Fragmento
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