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

import com.example.tree_solution_proyect.Holders.HolderMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ChatsClick;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter_mensaje extends RecyclerView.Adapter<HolderMensaje> {
    //Instanciamos los atributos
   private List<LMensaje> listMensaje=new ArrayList<>();
   private Context x;
   private ChatsClick chatsClick=new ChatsClick();;

    //Metodo constructor
   public Adapter_mensaje(Context x){
       this.x=x;
   }
    //Metodo que sirve para actualizar los mensajes
   public void actualizarMensaje(int posicion,LMensaje lMensaje){
       listMensaje.set(posicion,lMensaje);
       notifyItemChanged(posicion);
   }


    @NonNull
    @Override
    //Llama a este metodo cada vez cuando se esta creando el Holder para vincularlos
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if(viewType==1){
            v=LayoutInflater.from(x).inflate(R.layout.layout_mensaje_receptor,
                    parent,false);
        }else{
            v= LayoutInflater.from(x).inflate(R.layout.layout_mensaje_emisor,
                    parent,false);
        }
        return new HolderMensaje(v);
    }



    
    //Llama a este metodo para vincular holder con los datos asociados
    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {

       LMensaje lMensaje=listMensaje.get(position);
       LUsuario lUsuario=lMensaje.getlUsuario();
       if(lUsuario!=null){
           //vinculamos holder con los datos asociados
           holder.getNombre().setText(lUsuario.getUsuario().getUserName());
           Picasso.with(x.getApplicationContext()).load(lUsuario.getUsuario().getFotoPerfilUrl())
                   .resize(50,50).into(holder.getFotoPerfilMensaje());
       }
        holder.getMensaje().setText(lMensaje.getMensaje().getMensaje());
        if(!lMensaje.getMensaje().toString().isEmpty()) {
            holder.getFotoPerfilMensaje().setVisibility(View.VISIBLE);
            holder.getMensaje().setVisibility(View.VISIBLE);
        }
        holder.getHora().setText(lMensaje.obtenerFechaDeCreacionMensaje());

        //Gestion mensaje este utilizamos para abrir nuestra ubicacion mandada.
        holder.mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> cadena=chatsClick.extractURL((String) holder.getMensaje().getText());

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

    @Override
    public int getItemViewType(int position) {
        if (listMensaje.get(position).getlUsuario() != null) {
            if (listMensaje.get(position).getlUsuario().getKey()
                    .equals(UsuarioDAO.getKeyUsuario())) {
                return 1;
            } else {
                return -1;
            }
        }
        else{
            return -1;
        }
    }

    //En este metodo añdimos el mensaje creado a nuestra lista y notificamos a nuestro activity
    public int addMensaje(LMensaje m){
       listMensaje.add(m);
       int posicion=listMensaje.size()-1;
       notifyItemInserted(listMensaje.size());
       return posicion;
    }

}
