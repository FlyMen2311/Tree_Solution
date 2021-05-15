package com.example.tree_solution_proyect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

public class HolderMensaje extends RecyclerView.ViewHolder  {
    
    //Atributos
    private TextView nombre;
    public TextView mensaje;
    private TextView hora;
    private ImageView fotoPerfilMensaje;
    private Context context;


    //Metodo Constructor
    public HolderMensaje(@NonNull View itemView) {

        super(itemView);
        context=itemView.getContext();
        nombre=itemView.findViewById(R.id.nombreMensaje);
        mensaje=itemView.findViewById(R.id.Mensaje_text);
        hora=itemView.findViewById(R.id.hora_mensaje);
        fotoPerfilMensaje=itemView.findViewById(R.id.fotoMensaje);

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


