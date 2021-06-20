package com.example.tree_solution_proyect.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tree_solution_proyect.Holders.Holder_Chats;
import com.example.tree_solution_proyect.Holders.Holder_Libro;
import com.example.tree_solution_proyect.Objetos.Logica.LChat;
import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LMensaje;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.example.tree_solution_proyect.Persistencia.ChatDao;
import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ComunicacionFragment;
import com.example.tree_solution_proyect.Vistas.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class Adapter_Chats extends RecyclerView.Adapter<Holder_Chats>implements Filterable {

    private Context x;
    private List<LChat> listChats=new ArrayList<>();
    private ComunicacionFragment.ChatOpen chatOpen;


    public Adapter_Chats(Context x, ComunicacionFragment.ChatOpen chatOpen) {
        this.x = x;
        this.chatOpen=chatOpen;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @NotNull
    @Override
    public Holder_Chats onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v;
        v= LayoutInflater.from(x).inflate(R.layout.layout_holder_chats,parent,false);
        return new Holder_Chats(v,chatOpen);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Holder_Chats holder, int position) {
        LChat lChat=listChats.get(position);
        String keyenviar;
        Glide.with(x.getApplicationContext()).load(lChat.getChat().getFotoPrincipalUrl()).
                transforms(new RoundedCornersTransformation(26,5)).into(holder.getFoto_libro_chat());
        holder.getFoto_libro_chat().setVisibility(View.VISIBLE);
        holder.getName_libro_chat().setText(lChat.getChat().getNombrelibro());
        holder.getName_propietario().setText(lChat.getChat().getNombrePropietario());

        if(lChat.getChat().getKeyreceptor().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            keyenviar=lChat.getChat().getKeyemisor();
        }else{
            keyenviar=lChat.getChat().getKeyreceptor();
        }
        ChatDao.getInstance().getUltimoMensaje(keyenviar, lChat.getChat().getKeylibro(), new ChatDao.IDevolverUltimoMensaje() {
                    @Override
                    public void DevolverUltimoMensaje(ArrayList<LMensaje> lMensajes) {
                        if(lMensajes!=null){
                            holder.getUltimo_mensaje().setText(lMensajes.get(lMensajes.size() - 1).getMensaje().getMensaje());
                            holder.getHora().setText(lMensajes.get(lMensajes.size() - 1).obtenerFechaDeCreacionMensaje());
                        }else{
                            holder.getUltimo_mensaje().setText("Puedes empezar a chatear");
                            holder.getHora().setText(lChat.obtenerFechaDeCreacionChat());
                        }
                    }

                    @Override
                    public void devolverError(String mensajeError) {
                        Toast.makeText(holder.getContext(), "Error" + mensajeError, Toast.LENGTH_SHORT);
                    }
                });




    }
    //En este metodo añdimos el Libro creado a nuestra lista y notificamos a nuestro activity
    public int addChat(LChat LChat){
        listChats.add(LChat);
        int posicion=listChats.size()-1;
        notifyItemInserted(listChats.size());
        return posicion;
    }
    public void removeChat(int pos){
        listChats.remove(pos);
        notifyItemRemoved(pos);
    }

    public List<LChat> getListChats() {
        return listChats;
    }

    public void setListChats(List<LChat> listChats) {
        this.listChats = listChats;
    }

    @Override
    public int getItemCount() {
        return listChats.size();
    }
}
