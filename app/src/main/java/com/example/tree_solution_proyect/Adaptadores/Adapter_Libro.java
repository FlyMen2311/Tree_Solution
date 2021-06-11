package com.example.tree_solution_proyect.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.tree_solution_proyect.Holders.Holder_Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.LibrosClickablesIntefrace;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Adapter_Libro extends RecyclerView.Adapter<Holder_Libro> {
    private List<LLibro> listLibros=new ArrayList<>();
    private Context x;
    private HomeFragment homeFragment=new HomeFragment();


    public Adapter_Libro(Context x) {
        this.x = x;
    }

    public void actualizarLibro(int posicion,LLibro lLibro){
        listLibros.set(posicion,lLibro);
        notifyItemChanged(posicion);
    }

    @NonNull
    @NotNull
    @Override
    public Holder_Libro onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(x).inflate(R.layout.layout_holder_libro,parent,false);

        return new Holder_Libro(v,homeFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder_Libro holder, int position) {
        LLibro lLibro=listLibros.get(position);
        LUsuario lUsuario=lLibro.getLUsuario();
        if((lLibro!=null)&&(lUsuario!=null)){
            //vinculamos holder con los datos asociados
            holder.getAutor().setText("by "+lLibro.getLibro().getAutor());
            holder.getCategoria().setText(lLibro.getLibro().getCategoria());
            holder.getNombre().setText(lLibro.getLibro().getNombre());
            holder.getISBN().setText(lLibro.getLibro().getISBN());
            holder.getPrecio().setText(String.valueOf(lLibro.getLibro().getPrecio())+"€");
            Glide.with(x.getApplicationContext()).load(lLibro.getLibro().getFotoPrincipalUrl()).transforms(new RoundedCornersTransformation(26,5)).into(holder.getFoto_libro());
            holder.getFoto_libro().setVisibility(View.VISIBLE);
            holder.getCondition().setText(lLibro.getLibro().getCondition());


            if(lLibro.getLibro().getCondition().equals("Nuevo")){
                holder.getRatingBar().setRating(5);
            }else if(lLibro.getLibro().getCondition().equals("Muy buen estado")){
                holder.getRatingBar().setRating(4);
            }
            else if(lLibro.getLibro().getCondition().equals("Buen estado")){
                holder.getRatingBar().setRating(3);
            }
            else if(lLibro.getLibro().getCondition().equals("Defecto estético")){
                holder.getRatingBar().setRating(2);
            }
            else if(lLibro.getLibro().getCondition().equals("Mala condición")){
                holder.getRatingBar().setRating(1);
            }
            else{
                holder.getRatingBar().setRating(0);
            }
        }
        holder.getHora().setText(lLibro.obtenerFechaDeCreacionLibro());


    }

    public List<LLibro> getListLibros() {
        return listLibros;
    }

    public void setListLibros(List<LLibro> listLibros) {
        this.listLibros = listLibros;
    }

    @Override
    public int getItemCount() {
        return listLibros.size();
    }
    //En este metodo añdimos el Libro creado a nuestra lista y notificamos a nuestro activity
    public int addLibro(LLibro lLibro){
        listLibros.add(lLibro);
        int posicion=listLibros.size()-1;
        notifyItemInserted(listLibros.size());
        return posicion;
    }
}
