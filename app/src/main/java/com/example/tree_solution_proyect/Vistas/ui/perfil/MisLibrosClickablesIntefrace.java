package com.example.tree_solution_proyect.Vistas.ui.perfil;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public interface MisLibrosClickablesIntefrace {
    void MisLibrosClick(int pos, ImageView imgcontainer, ImageView fotoLibro, TextView nombre,
                    TextView autor, TextView precio, TextView ISBN, TextView categoria,
                    RatingBar ratingBar, TextView estado, TextView fechacreacion, TextView descripcion);
}
