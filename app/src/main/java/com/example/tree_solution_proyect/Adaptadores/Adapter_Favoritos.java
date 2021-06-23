package com.example.tree_solution_proyect.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Holders.Holder_Favoritos;
import com.example.tree_solution_proyect.Objetos.Logica.LChat;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.example.tree_solution_proyect.Vistas.ui.favorite.FavoriteFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Adapter_Favoritos extends RecyclerView.Adapter<Holder_Favoritos>{
    //Inicializamos los atributos
    public List<LLibro> listLibros=new ArrayList<>();
    private Context x;
    private  FavoriteFragment.LibroOpenFav libroOpen;
    public boolean isFavorite=false;
    //Metodo Constructor
    public Adapter_Favoritos(Context x, FavoriteFragment.LibroOpenFav libroOpen) {
        this.x = x;
        this.libroOpen=libroOpen;
    }
    //Metodo que sirve para actualizar un Libro Favorito
    public void actualizarLibro(int posicion,LLibro lLibro){
        listLibros.set(posicion,lLibro);
        notifyItemChanged(posicion);
    }

    //Llama a este metodo cada vez cuando se esta creando el Holder para vincularlos
    @NonNull
    @NotNull
    @Override
    public Holder_Favoritos onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(x).inflate(R.layout.layout_holder_favoritos,parent,false);

        return new Holder_Favoritos(v, libroOpen);
    }




    //Llama a este metodo para vincular holder con los datos asociados    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder_Favoritos holder, int position) {

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


            LibroDAO.getInstance().libroExistFavoritos(lLibro, new LibroDAO.IDevolverBooleanExist() {
                @Override
                public void devolverExist(boolean isExist) {
                    if(isExist){
                        holder.getFavorit().setBackgroundResource(R.drawable.favorite_libro);
                    }else{
                        holder.getFavorit().setBackgroundResource(R.drawable.favorite);
                    }
                }

                @Override
                public void devolverError(String mensajeError) {
                    Toast.makeText(holder.getContext(), "Error" + mensajeError, Toast.LENGTH_SHORT);
                }
            });



            holder.getFavorit().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.getFavorit().getBackground().getConstantState().equals(holder.getFavorit().getContext().getDrawable(R.drawable.favorite).getConstantState())){
                        holder.getFavorit().setBackgroundResource(R.drawable.favorite_libro);
                        LibroDAO.getInstance().crearLibroFavorito(lLibro);
                        isFavorite=true;
                    }else if(holder.getFavorit().getBackground().getConstantState().equals(holder.getFavorit().getContext().getDrawable(R.drawable.favorite_libro).getConstantState())){
                        LibroDAO.getInstance().eliminarLibroFavorito(lLibro);
                        holder.getFavorit().setBackgroundResource(R.drawable.favorite);
                        isFavorite=false;
                    }
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

    //Metodo que sirve para devolver Lista de Libros Favoritos
    public List<LLibro> getListLibros() {
        return listLibros;
    }

    //Get booleano
    public boolean isFavorite() {
        return isFavorite;
    }
    //Set booleano
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    //Metodo que sirve para devolver numero de holders creados
    @Override
    public int getItemCount() {
        return listLibros.size();
    }

    //En este metodo añadimos el Libro creado a nuestra lista y notificamos a nuestro activity
    public int addLibro(LLibro lLibro){
        listLibros.add(lLibro);
        int posicion=listLibros.size()-1;
        notifyItemInserted(listLibros.size());
        return posicion;
    }
}
