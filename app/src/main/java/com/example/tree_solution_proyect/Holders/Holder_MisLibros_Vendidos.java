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
import com.example.tree_solution_proyect.Vistas.ui.perfil.MisLibrosVendidosClickableInterface;

import org.jetbrains.annotations.NotNull;

public class Holder_MisLibros_Vendidos extends RecyclerView.ViewHolder {
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
    private MisLibrosVendidosClickableInterface misLibrosVendidosClickableInterface;

    public Holder_MisLibros_Vendidos(@NonNull @NotNull View itemView, MisLibrosVendidosClickableInterface misLibrosVendidosClickableInterface) {
        super(itemView);
        context=itemView.getContext();
        Foto_libro=itemView.findViewById(R.id.foto_libro_mislibros_vendidos);
        nombre=itemView.findViewById(R.id.holder_nombre_mislibros_vendidos);
        autor=itemView.findViewById(R.id.holder_autor_mislibros_vendidos);
        categoria=itemView.findViewById(R.id.holder_categoria_mislibros_vendidos);
        ISBN=itemView.findViewById(R.id.holder_Isbn_mislibros_vendidos);

        condition=itemView.findViewById(R.id.holder_condition_mislibros_vendidos);
        precio=itemView.findViewById(R.id.holder_precio_mislibros_vendidos);
        ratingBar=itemView.findViewById(R.id.holder_ratingBar_libro_mislibros_vendidos);
        hora=itemView.findViewById(R.id.holder_fechacreacion_mislibros_vendidos);
        containerLibro= itemView.findViewById(R.id.container_holder_libro_mislibros_vendidos);
        this.misLibrosVendidosClickableInterface=misLibrosVendidosClickableInterface;



        containerLibro.setOnClickListener(v -> misLibrosVendidosClickableInterface.MisLibrosVendidosClick(getAdapterPosition(),containerLibro,
                Foto_libro,nombre,
                autor,precio,ISBN,
                categoria,ratingBar,
                condition,hora));
    }

    public ImageView getFoto_libro() {
        return Foto_libro;
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

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public TextView getPrecio() {
        return precio;
    }

    public void setPrecio(TextView precio) {
        this.precio = precio;
    }

    public TextView getCondition() {
        return condition;
    }

    public void setCondition(TextView condition) {
        this.condition = condition;
    }

    public TextView getISBN() {
        return ISBN;
    }

    public void setISBN(TextView ISBN) {
        this.ISBN = ISBN;
    }

    public TextView getCategoria() {
        return categoria;
    }

    public void setCategoria(TextView categoria) {
        this.categoria = categoria;
    }

    public TextView getAutor() {
        return autor;
    }

    public void setAutor(TextView autor) {
        this.autor = autor;
    }

    public TextView getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(TextView descripcion) {
        this.descripcion = descripcion;
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

    public MisLibrosVendidosClickableInterface getMisLibrosVendidosClickableInterface() {
        return misLibrosVendidosClickableInterface;
    }

    public void setMisLibrosVendidosClickableInterface(MisLibrosVendidosClickableInterface misLibrosVendidosClickableInterface) {
        this.misLibrosVendidosClickableInterface = misLibrosVendidosClickableInterface;
    }
}
