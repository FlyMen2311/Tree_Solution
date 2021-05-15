package com.example.tree_solution_proyect;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.Objetos.Mensaje;
import com.example.tree_solution_proyect.Vistas.ChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_mensaje extends RecyclerView.Adapter<HolderMensaje> {

   private List<Mensaje> listMensaje=new ArrayList<>();
   private Context x;
   private ChatActivity mainActivity=new ChatActivity();;


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
        //vinculamos holder con los datos asociados
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());
        holder.getHora().setText(listMensaje.get(position).getHora());

        //Comprobamos foto si la foto esta vacia y ponemos una nuestra o una por default.
        if(listMensaje.get(position).getFoto_perfil().isEmpty()){
            holder.getFotoPerfilMensaje().setImageResource(R.drawable.logo);
        }else{
           // Glide.with(x).load(listMensaje.get(position).getFoto_perfil()).into(holder.getFotoPerfilMensaje());
        }

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
    public void addMensaje(Mensaje m){
       listMensaje.add(m);
       notifyItemInserted(listMensaje.size());
    }

}
