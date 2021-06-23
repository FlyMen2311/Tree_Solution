package com.example.tree_solution_proyect.Holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.R;

public class HolderMensaje extends RecyclerView.ViewHolder  {

    //Inicializamos los atributos
    private TextView nombre,hora;
    public TextView mensaje;
    private ImageView fotoPerfilMensaje;
    private Context context;


    //Metodo Constructor de holder,es donde se hacen todas las vinculasiones con layout
    public HolderMensaje(@NonNull View itemView) {
        super(itemView);
        context=itemView.getContext();
        nombre=itemView.findViewById(R.id.NameLibroChat);
        mensaje=itemView.findViewById(R.id.ultimo_masege_chats);
        hora=itemView.findViewById(R.id.hora_ultimo_mesage);
        fotoPerfilMensaje=itemView.findViewById(R.id.fotoLibroChat);
    }

    //Seters y Geters
    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public TextView getMensaje() {
        return mensaje;
    }

    public void setMensaje(TextView mensaje) {
        this.mensaje = mensaje;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public ImageView getFotoPerfilMensaje() {
        return fotoPerfilMensaje;
    }

    public void setFotoPerfilMensaje(ImageView fotoPerfilMensaje) {
        this.fotoPerfilMensaje = fotoPerfilMensaje;
    }




}


