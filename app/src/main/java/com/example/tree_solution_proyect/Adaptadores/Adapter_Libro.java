package com.example.tree_solution_proyect.Adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.Toast;

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

public class Adapter_Libro extends RecyclerView.Adapter<Holder_Libro>implements Filterable {
    public List<LLibro> listLibros=new ArrayList<>();
    public List<LLibro> listLibrosFilter=new ArrayList<>();
    private Context x;
    private  HomeFragment.LibroOpen libroOpen;
    public ISBNFilter isbnFilter;

    public Adapter_Libro(Context x,HomeFragment.LibroOpen libroOpen) {
        this.x = x;
        isbnFilter=new ISBNFilter(this);
        this.libroOpen=libroOpen;
    }

    public void actualizarLibro(int posicion,LLibro lLibro){
        listLibrosFilter.set(posicion,lLibro);
        notifyItemChanged(posicion);
    }


    @NonNull
    @NotNull
    @Override
    public Holder_Libro onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(x).inflate(R.layout.layout_holder_libro,parent,false);

        return new Holder_Libro(v, libroOpen);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder_Libro holder, int position) {

        LLibro lLibro=listLibrosFilter.get(position);
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


            try {

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

    @Override
    public int getItemCount() {
        return listLibrosFilter.size();
    }

    //En este metodo añdimos el Libro creado a nuestra lista y notificamos a nuestro activity
    public int addLibro(LLibro lLibro){
        listLibrosFilter.add(lLibro);
        int posicion=listLibrosFilter.size()-1;
        notifyItemInserted(listLibrosFilter.size());
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


}
