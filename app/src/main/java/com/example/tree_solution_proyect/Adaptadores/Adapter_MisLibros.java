package com.example.tree_solution_proyect.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Holders.Holder_MisLibros;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.MisLibrosActivity;
import com.example.tree_solution_proyect.Vistas.MisLibrosVendidosActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Adapter_MisLibros extends RecyclerView.Adapter<Holder_MisLibros> {
    //Inicializamos los atributos
    public List<LLibro> listMisLibros =new ArrayList<>();
    private Context x;
    private  MisLibrosActivity.LibroOpen libroOpen;
    public boolean isFavorite=false;
    //Metodo Constructor
    public Adapter_MisLibros(Context x, MisLibrosActivity.LibroOpen libroOpen) {
        this.x = x;

        this.libroOpen=libroOpen;
    }

    //Metodo que sirve para actualizar Mis Libros
    public void actualizarLibro(int posicion,LLibro lLibro){
        listMisLibros.set(posicion,lLibro);
        notifyItemChanged(posicion);
    }

    //Llama a este metodo cada vez cuando se esta creando el Holder para vincularlos
    @NonNull
    @NotNull
    @Override
    public Holder_MisLibros onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(x).inflate(R.layout.layout_holder_mislibros,parent,false);

        return new Holder_MisLibros(v, libroOpen);
    }
    //Llama a este metodo para vincular holder con los datos asociados
    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder_MisLibros holder, int position) {

        LLibro lLibro= listMisLibros.get(position);
        LUsuario lUsuario=lLibro.getLUsuario();

        if((lLibro!=null)&&(lUsuario!=null)){
            //vinculamos holder con los datos asociados
            holder.getAutor().setText("by "+lLibro.getLibro().getAutor());
            holder.getCategoria().setText(lLibro.getLibro().getCategoria());
            holder.getNombre().setText(lLibro.getLibro().getNombre());
            holder.getISBN().setText(lLibro.getLibro().getISBN());
            holder.getPrecio().setText(String.valueOf(lLibro.getLibro().getPrecio())+"€");
            Glide.with(x.getApplicationContext()).load(lLibro.getLibro().getFotoPrincipalUrl())
                    .transforms(new RoundedCornersTransformation(26,5))
                    .into(holder.getFoto_libro());
            holder.getFoto_libro().setVisibility(View.VISIBLE);
            holder.getCondition().setText(lLibro.getLibro().getCondition());

            holder.getFoto_libro().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

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


        try {

        }catch (Exception exception){
            Toast.makeText(x.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public List<LLibro> getListLibros() {
        return listMisLibros;
    }
    public void setListLibros(List<LLibro> listLibros) {
        this.listMisLibros = listLibros;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int getItemCount() {
        return listMisLibros.size();
    }

    //En este metodo añdimos el Libro creado a nuestra lista y notificamos a nuestro activity
    public int addLibro(LLibro lLibro){
        listMisLibros.add(lLibro);
        int posicion= listMisLibros.size()-1;
        notifyItemInserted(listMisLibros.size());
        return posicion;
    }
}
