package com.example.tree_solution_proyect.Holders;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Adaptadores.Adapter_Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.home.LibrosClickablesIntefrace;

import java.util.ArrayList;

public class Holder_Libro extends  RecyclerView.ViewHolder{
    private ImageView Foto_libro,Favorit;
    private TextView nombre;
    private TextView hora;
    private TextView precio;
    private TextView condition;
    private TextView ISBN;
    private TextView categoria;
    private TextView autor;
    private TextView descripcion;
    private TextView esVendido;
    private Context context;
    private RatingBar ratingBar;
    private ImageView containerLibro;
    private LibrosClickablesIntefrace librosClickablesIntefrace1;


    public Holder_Libro(@NonNull View itemView ,LibrosClickablesIntefrace librosClickablesIntefrace) {
        super(itemView);
        context=itemView.getContext();
        Foto_libro=itemView.findViewById(R.id.foto_libro);
        nombre=itemView.findViewById(R.id.nombre);
        autor=itemView.findViewById(R.id.autor);
        categoria=itemView.findViewById(R.id.holder_catergoria);
        ISBN=itemView.findViewById(R.id.holder_Isbn);
        descripcion=itemView.findViewById(R.id.descripcion);
        condition=itemView.findViewById(R.id.holder_condition);
        precio=itemView.findViewById(R.id.holder_precio);
        Favorit=itemView.findViewById(R.id.favoritos);
        ratingBar=itemView.findViewById(R.id.ratingBar_libro);
        hora=itemView.findViewById(R.id.holder_fechacreacion);
        containerLibro= itemView.findViewById(R.id.container_holder_libro);
        this.librosClickablesIntefrace1=librosClickablesIntefrace;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                librosClickablesIntefrace1.LibroClick(getAdapterPosition(),containerLibro,
                        Foto_libro,nombre,
                        autor,precio,ISBN,
                        categoria,ratingBar,
                        condition,hora,
                        Favorit, descripcion, esVendido);
            }
        });
    }

    public ImageView getFavorit() {
        return Favorit;
    }

    public void setFavorit(ImageView favorit) {
        Favorit = favorit;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public ImageView getFoto_libro() {
        return Foto_libro;
    }



    public ImageView getContainerLibro() {
        return containerLibro;
    }

    public void setContainerLibro(ImageView containerLibro) {
        this.containerLibro = containerLibro;
    }

    public void setFoto_libro(ImageView foto_libro) {
        Foto_libro = foto_libro;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getAutor() {
        return autor;
    }

    public void setAutor(TextView autor) {
        this.autor = autor;
    }

    public TextView getCategoria() {
        return categoria;
    }

    public void setCategoria(TextView categoria) {
        this.categoria = categoria;
    }

    public TextView getISBN() {
        return ISBN;
    }

    public void setISBN(TextView ISBN) {
        this.ISBN = ISBN;
    }

    public TextView getCondition() {
        return condition;
    }

    public void setCondition(TextView condition) {
        this.condition = condition;
    }

    public TextView getPrecio() {
        return precio;
    }

    public void setPrecio(TextView precio) {
        this.precio = precio;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public TextView getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(TextView descripcion) {
        this.descripcion = descripcion;
    }



}
