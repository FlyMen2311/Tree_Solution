package com.example.tree_solution_proyect.Vistas.ui.favorite;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public interface LibrosFavClickableInteface {
    void LibroClick2(int pos, ImageView imgcontainer, ImageView fotoLibro, TextView nombre, TextView autor,
                    TextView precio, TextView ISBN, TextView categoria, RatingBar ratingBar, TextView estado, TextView fechacreacion, ImageView favorite);
}