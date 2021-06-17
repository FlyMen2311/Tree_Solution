package com.example.tree_solution_proyect.Vistas.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.LibroClickActivity;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.LibrosClickablesIntefrace;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class MisLibrosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReferenceLibro;
    public static List<LLibro> libroListClick;
    private Adapter_Libro adapter_libro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mislibros);

        recyclerView= findViewById(R.id.recycler_misLibros);

        mAuth= FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        databaseReferenceLibro =database.getReference("Libros");

        adapter_libro=new Adapter_Libro(this,
                new LibroOpen(this,this));
        LinearLayoutManager l=new LinearLayoutManager(this);
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
        public void LibroClick(int pos, ImageView imgcontainer, ImageView fotoLibro,
                               TextView nombre, TextView autor, TextView precio, TextView ISBN,
                               TextView categoria, RatingBar ratingBar, TextView estado,
                               TextView fechacreacion, ImageView favorite, TextView descripcion, TextView status){
            try {
                Intent intent = new Intent(activity, LibroClickActivity.class);
                LLibro llibro = libroListClick.get(pos);
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
                                    ,Pair.create(fechacreacion, "fechacreacion_TR")
                                    ,Pair.create(favorite, "favorite_TR"));
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
