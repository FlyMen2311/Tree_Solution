package com.example.tree_solution_proyect.Vistas.ui.comunicacion;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


public interface ChatClickableInterface {
        void ChatClick(int pos ,LinearLayout linearLayout, ImageView Foto_libro_chat,
                       TextView name_libro_chat,
                       TextView ultimo_mensaje,
                       TextView name_propietario,
                       TextView hora);

}
