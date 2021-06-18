package com.example.tree_solution_proyect.Vistas.ui.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tree_solution_proyect.R;
import com.example.tree_solution_proyect.databinding.FragmentFavoriteBinding;

import java.util.zip.Inflater;


public class FavoriteFragment extends Fragment {
    private Button button;

    private Inflater inflater;

    private int container;

    private View vista;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vista = inflater.inflate(R.layout.fragment_favorite, container, false);

        //button = vista.findViewById(R.id.button2);

        return vista;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}