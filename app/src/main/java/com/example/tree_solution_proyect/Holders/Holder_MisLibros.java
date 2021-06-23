package com.example.tree_solution_proyect.Holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.perfil.MisLibrosClickablesIntefrace;

public class Holder_MisLibros extends  RecyclerView.ViewHolder{
    //Inicializamos los atributos
    private ImageView Foto_libro;
    private TextView nombre;
    private TextView hora;
    private TextView precio;
    private TextView condition;
    private TextView ISBN;
    private TextView categoria;
    private TextView autor;
    private TextView descripcion;
    private Context context;
    private RatingBar ratingBar;
    private ImageView containerLibro;
    private MisLibrosClickablesIntefrace librosClickablesIntefrace1;

    //Metodo Constructor de holder,es donde se hacen todas las vinculasiones con layout
    public Holder_MisLibros(@NonNull View itemView , MisLibrosClickablesIntefrace librosClickablesIntefrace) {
        super(itemView);
        context=itemView.getContext();
        Foto_libro=itemView.findViewById(R.id.foto_libro_mislibros);
        nombre=itemView.findViewById(R.id.holder_nombre_mislibros);
        autor=itemView.findViewById(R.id.holder_autor_mislibros);
        categoria=itemView.findViewById(R.id.holder_categoria_mislibros);
        ISBN=itemView.findViewById(R.id.holder_Isbn_mislibros);

        condition=itemView.findViewById(R.id.holder_condition_mislibros);
        precio=itemView.findViewById(R.id.holder_precio_mislibros);
        ratingBar=itemView.findViewById(R.id.holder_ratingBar_libro_mislibros);
        hora=itemView.findViewById(R.id.holder_fechacreacion_mislibros);
        containerLibro= itemView.findViewById(R.id.container_holder_libro_mislibros);
        this.librosClickablesIntefrace1=librosClickablesIntefrace;



        containerLibro.setOnClickListener(v -> librosClickablesIntefrace1.MisLibrosClick(getAdapterPosition(),containerLibro,
                Foto_libro,nombre,
                autor,precio,ISBN,
                categoria,ratingBar,
                condition,hora));
    }
    //Seters y Geters
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
