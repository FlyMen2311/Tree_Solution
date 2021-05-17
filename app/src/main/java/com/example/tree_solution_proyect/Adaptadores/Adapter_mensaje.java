package com.example.tree_solution_proyect.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Holders.HolderMensaje;
import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_mensaje extends RecyclerView.Adapter<HolderMensaje> {

   private List<LMensaje> listMensaje=new ArrayList<>();
   private Context x;
   private ComunicacionFragment mainActivity=new ComunicacionFragment();;


   public Adapter_mensaje(Context x){
       this.x=x;
   }




    @NonNull
    @Override
    //Llama a este metodo cada vez cuando se esta creando el Holder para vincularlos
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(x).inflate(R.layout.layout_mensaje,parent,false);
        return new HolderMensaje(v);
    }

    
    //Llama a este metodo para vincular holder con los datos asociados
    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {

       LMensaje lMensaje=listMensaje.get(position);
        //vinculamos holder con los datos asociados
        holder.getNombre().setText(lMensaje.getlUsuario().getUsuario().getUserName());
        holder.getMensaje().setText(lMensaje.getMensaje().getMensaje());
        if(!lMensaje.getMensaje().toString().isEmpty()) {
            holder.getFotoPerfilMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }


        Glide.with(x.getApplicationContext()).load(lMensaje.getlUsuario().getUsuario().getFotoPerfilUrl()).into(holder.getFotoPerfilMensaje());

        holder.getHora().setText(lMensaje.obtenerFechaDeCreacionMensaje());

        //Gestion mensaje este utilizamos para abrir nuestra ubicacion mandada.
        holder.mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> cadena=mainActivity.extractURL((String) holder.getMensaje().getText());

                String URL = "";
                for (String url : cadena)
                {
                    URL=String.format(Locale.ENGLISH, url);
                }

                try {
                    Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse((String) URL));
                    i.setPackage("com.google.android.apps.maps");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    x.startActivity(i);
                    Log.i("xyna", (String) holder.getMensaje().getText());
                }catch(Exception x){
                    Log.i("xyna",x.getMessage());
                }
            }
        });

    }

    //Recycler View utiliza este metodo para saber cuantos Objetos tiene que mostrar
    @Override
    public int getItemCount() {
        return listMensaje.size();
    }

    //En este metodo añdimos el mensaje creado a nuestra lista y notificamos a nuestro activity
    public void addMensaje(LMensaje m){
       listMensaje.add(m);
       notifyItemInserted(listMensaje.size());
    }

}
