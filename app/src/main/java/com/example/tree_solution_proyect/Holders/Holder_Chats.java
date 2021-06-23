package com.example.tree_solution_proyect.Holders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.Vistas.ui.comunicacion.ChatClickableInterface;

import org.jetbrains.annotations.NotNull;

public class Holder_Chats extends  RecyclerView.ViewHolder{
    //Inicializamos los atributos
    private ImageView Foto_libro_chat;
    private TextView name_libro_chat;
    private TextView ultimo_mensaje;
    private TextView name_propietario;
    private TextView hora;
    private Context context;
    private LinearLayout linearLayout;
    private ChatClickableInterface chatClickableInterface;
    public RelativeLayout relativeLayout;
    public CardView cardView;
    //Metodo Constructor de holder,es donde se hacen todas las vinculasiones con layout
    public Holder_Chats(@NonNull @NotNull View itemView,ChatClickableInterface chatClickableInterface) {
        super(itemView);
        Foto_libro_chat =itemView.findViewById(R.id.fotoLibroChat);
        this.name_libro_chat =itemView.findViewById(R.id.NameLibroChat);
        this.name_propietario=itemView.findViewById(R.id.UserNameChats);
        this.ultimo_mensaje=itemView.findViewById(R.id.ultimo_masege_chats);
        this.hora =itemView.findViewById(R.id.hora_ultimo_mesage);
        this.context =itemView.getContext();
        this.linearLayout =itemView.findViewById(R.id.linear);
        this.chatClickableInterface=chatClickableInterface;
        this.relativeLayout=itemView.findViewById(R.id.relative);
        this.cardView=itemView.findViewById(R.id.cardView);


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chatClickableInterface.ChatClick(getAdapterPosition(),linearLayout,Foto_libro_chat,name_libro_chat,ultimo_mensaje,name_libro_chat,hora);
                    }
                });
            }
        });
    }
    //Seters y Geters
    public ImageView getFoto_libro_chat() {
        return Foto_libro_chat;
    }

    public void setFoto_libro_chat(ImageView foto_libro_chat) {
        Foto_libro_chat = foto_libro_chat;
    }

    public TextView getName_libro_chat() {
        return name_libro_chat;
    }

    public void setName_libro_chat(TextView name_libro_chat) {
        this.name_libro_chat = name_libro_chat;
    }

    public TextView getUltimo_mensaje() {
        return ultimo_mensaje;
    }

    public void setUltimo_mensaje(TextView ultimo_mensaje) {
        this.ultimo_mensaje = ultimo_mensaje;
    }

    public TextView getName_propietario() {
        return name_propietario;
    }

    public void setName_propietario(TextView name_propietario) {
        this.name_propietario = name_propietario;
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

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }
}
