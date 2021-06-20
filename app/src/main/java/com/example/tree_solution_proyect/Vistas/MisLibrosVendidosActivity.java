package com.example.tree_solution_proyect.Vistas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_MisLibros;
import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.perfil.MisLibrosClickablesIntefrace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;


public class MisLibrosVendidosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceLibro;
    private FirebaseStorage storage;
    private Adapter_MisLibros adapter_misLibros;

    private String currentUserKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mislibros);

        recyclerView= findViewById(R.id.recycler_misLibros);

        mAuth= FirebaseAuth.getInstance();

        currentUserKey = mAuth.getCurrentUser().getUid();

        database=FirebaseDatabase.getInstance();
        databaseReferenceLibro =database.getReference("Libros");

        storage = FirebaseStorage.getInstance();


        adapter_misLibros =new Adapter_MisLibros(MisLibrosVendidosActivity.this,
                new LibroOpen(this, MisLibrosVendidosActivity.this));
        LinearLayoutManager l=new LinearLayoutManager(MisLibrosVendidosActivity.this);
        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter_misLibros);
        //Funcion para pasar al ultimo mensaje producido
        adapter_misLibros.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter_misLibros.getItemCount()-1);
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
                if (lLibro.getLibro().getUserKey().equals(currentUserKey) && (lLibro.getLibro().getEsVendido().equals("No"))){
                    final int posicion= adapter_misLibros.addLibro(lLibro);

                    if (stringLUsuarioMap.get(m.getUserKey()) != null) {
                        lLibro.setLUsuario(stringLUsuarioMap.get(m.getUserKey()));
                        adapter_misLibros.actualizarLibro(posicion, lLibro);
                    } else {
                        UsuarioDAO.getInstance().obtenerInformacionKey(m.getUserKey(), new UsuarioDAO.IDevolverUsuario() {
                            @Override
                            public void devolverUsuario(LUsuario lUsuario) {
                                stringLUsuarioMap.put(m.getUserKey(), lUsuario);
                                lLibro.setLUsuario(lUsuario);
                                adapter_misLibros.actualizarLibro(posicion, lLibro);
                            }

                            @Override
                            public void devolverError(String mensajeError) {
                                Toast.makeText(MisLibrosVendidosActivity.this, "Error" + mensajeError, Toast.LENGTH_SHORT).show();
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
                int posicion = 0;
                for(int i = 0; i< adapter_misLibros.getListLibros().size(); i++){
                    if(adapter_misLibros.getListLibros().get(i).getLibro().getReferenceStorage().equals(lLibro.getLibro().getReferenceStorage())){
                        posicion=i;
                    }
                }
                adapter_misLibros.getListLibros().remove(posicion);


                adapter_misLibros.notifyItemRemoved(posicion);

            }


            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    public class LibroOpen  implements MisLibrosClickablesIntefrace {
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
        public void MisLibrosClick(int pos, ImageView imgcontainer, ImageView fotoLibro,
                               TextView nombre, TextView autor, TextView precio, TextView ISBN,
                               TextView categoria, RatingBar ratingBar, TextView estado,
                               TextView fechacreacion, TextView descripcion){
            try {
                Intent intent = new Intent(activity, MisLibrosClickActivity.class);
                LLibro llibro = adapter_misLibros.getListLibros().get(pos);
                if (llibro != null) {
                    intent.putExtra("objectLibro", llibro);
                    ActivityOptionsCompat activityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                                    Pair.create(imgcontainer, " container_holder_libro")
                                    ,Pair.create(fotoLibro, "fotolibro_TR")
                                    ,Pair.create(nombre, "nombre_TR")
                                    ,Pair.create( autor, "autor_TR")
                                    ,Pair.create( precio, "precio_TR")
                                    ,Pair.create( ISBN, "ISBN_TR")
                                    ,Pair.create(categoria, "categoria_TN")
                                    ,Pair.create(ratingBar, "ratingbar_TR")
                                    ,Pair.create(estado, "condition_TR")
                                    ,Pair.create(fechacreacion, "fechacreacion_TR"));
                    startActivity(intent, activityOptionsCompat.toBundle());

                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_LONG).show();
                }

            } catch (Exception exception) {
                Toast.makeText(activity, exception.getMessage(), Toast.LENGTH_LONG).show();
            }



        }


    }

}


