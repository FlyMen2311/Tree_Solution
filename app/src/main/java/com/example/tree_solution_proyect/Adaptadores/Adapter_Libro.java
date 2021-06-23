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
import com.example.tree_solution_proyect.Holders.Holder_Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class Adapter_Libro extends RecyclerView.Adapter<Holder_Libro>implements Filterable {
    public List<LLibro> listLibros=new ArrayList<>();
    public List<LLibro> listLibrosFilter=new ArrayList<>();
    private Context x;
    private  HomeFragment.LibroOpen libroOpen;
    public ISBNFilter isbnFilter;
    private int numItems;
    public boolean isFavorite=false;

    public Adapter_Libro(Context x,HomeFragment.LibroOpen libroOpen) {
        this.x = x;
        isbnFilter=new ISBNFilter(this);
        this.libroOpen=libroOpen;
        numItems=listLibrosFilter.size();
    }

    public void actualizarLibro(int posicion,LLibro lLibro){
        listLibrosFilter.set(posicion,lLibro);
        notifyItemChanged(posicion);
    }

    @NonNull
    @NotNull
    @Override
    public Holder_Libro onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(x).inflate(R.layout.layout_holder_libro,parent,false);
        return new Holder_Libro(v, libroOpen);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder_Libro holder, int position) {
        try {
        LLibro lLibro=listLibrosFilter.get(position);
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



            }catch (Exception exception){
                Toast.makeText(x.getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
            }

    }

    public List<LLibro> getListLibros() {
        return listLibrosFilter;
    }
    public List<LLibro> getListLibrosAll() {
        return listLibros;
    }
    public void setListLibros(List<LLibro> listLibros) {
        this.listLibrosFilter = listLibros;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int getItemCount() {
        return numItems;
    }

    //En este metodo añdimos el Libro creado a nuestra lista y notificamos a nuestro activity
    public int addLibro(LLibro lLibro){

        int posicion=0;
        if(posicion==0) {
            listLibrosFilter.add(lLibro);
            posicion = listLibrosFilter.size() - 1;
            notifyItemInserted(listLibrosFilter.size()-1);
        }

        return posicion;
    }
    public void addLibroAll(LLibro Libro){
        this.listLibros.add(Libro);
    }


    @Override
    public Filter getFilter() {
        return isbnFilter;
    }

    public class ISBNFilter extends Filter {
        private Adapter_Libro listAdapter;


        private ISBNFilter(Adapter_Libro listAdapter) {
            super();
            this.listAdapter = listAdapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            listLibrosFilter.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                listLibrosFilter.addAll(listLibros);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (LLibro libro  : listLibros) {
                    if (libro.getLibro().getISBN().toLowerCase().contains(filterPattern)) {
                        listLibrosFilter.add(libro);
                    }
                }
            }
            results.values = listLibrosFilter;
            results.count = listLibrosFilter.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            this.listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

   public void setNumItems(int numItems){
        this.numItems = numItems;
    }
}
