package com.example.tree_solution_proyect.Holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.favorite.LibrosFavClickableInteface;

public class Holder_Favoritos extends  RecyclerView.ViewHolder{
    //Instanciamos los atributos
    private ImageView Foto_libro;
    private ImageView Favorit;
    private TextView nombre;
    private TextView autor;
    private TextView categoria;
    private TextView ISBN;
    private TextView condition;
    private TextView precio;
    private TextView hora;
    private Context context;
    private RatingBar ratingBar;
    private ImageView containerLibro;
    private LibrosFavClickableInteface librosFavClickableInteface;

    //Metodo Constructor de holder,es donde se hacen todas las vinculasiones con layout
    public Holder_Favoritos(@NonNull View itemView , LibrosFavClickableInteface librosFavClickableInteface) {
        super(itemView);
        context=itemView.getContext();
        Foto_libro=itemView.findViewById(R.id.foto_libro_favotiros);
        nombre=itemView.findViewById(R.id.holder_nombre_favoritos);
        autor=itemView.findViewById(R.id.holder_autor_favoritos);
        categoria=itemView.findViewById(R.id.holder_categoria_favoritos);
        ISBN=itemView.findViewById(R.id.holder_Isbn_favoritos);

        condition=itemView.findViewById(R.id.holder_condition_favoritos);
        precio=itemView.findViewById(R.id.holder_precio_favoritos);
        Favorit=itemView.findViewById(R.id.holder_favoritos_favoritos);
        ratingBar=itemView.findViewById(R.id.holder_ratingBar_libro_favoritos);
        hora=itemView.findViewById(R.id.holder_fechacreacion_favotiros);
        containerLibro= itemView.findViewById(R.id.container_holder_libro_favoritos);
        this.librosFavClickableInteface=librosFavClickableInteface;



        containerLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                librosFavClickableInteface.LibroClick2(getAdapterPosition(),containerLibro,
                        Foto_libro,nombre,
                        autor,precio,ISBN,
                        categoria,ratingBar,
                        condition,hora,
                        Favorit);
            }
        });
    }
    //Seters y Geters
    public ImageView getFoto_libro() {
        return Foto_libro;
    }

    public void setFoto_libro(ImageView foto_libro) {
        Foto_libro = foto_libro;
    }

    public ImageView getFavorit() {
        return Favorit;
    }

    public void setFavorit(ImageView favorit) {
        Favorit = favorit;
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

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public ImageView getContainerLibro() {
        return containerLibro;
    }

    public void setContainerLibro(ImageView containerLibro) {
        this.containerLibro = containerLibro;
    }
}


