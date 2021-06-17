package com.example.tree_solution_proyect.Vistas.ui.home;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public interface LibrosClickablesIntefrace {
    void LibroClick(int pos, ImageView imgcontainer, ImageView fotoLibro, TextView nombre,
                    TextView autor, TextView precio, TextView ISBN, TextView categoria,
                    RatingBar ratingBar, TextView estado, TextView fechacreacion,
                    ImageView favorite, TextView descripcion, TextView esVendido);
}
