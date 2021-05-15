package com.example.tree_solution_proyect.Vistas.ui.NewPost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tree_solution_proyect.databinding.FragmentHomeBinding;
import com.example.tree_solution_proyect.databinding.FragmentNewpostBinding;

public class NewPostFragment extends Fragment {

    private NewPostViewModel newPostViewModel;
    private FragmentNewpostBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newPostViewModel =
                new ViewModelProvider(this).get(NewPostViewModel.class);

        binding = FragmentNewpostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        newPostViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}