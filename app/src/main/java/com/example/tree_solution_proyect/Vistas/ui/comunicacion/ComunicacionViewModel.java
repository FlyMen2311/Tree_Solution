package com.example.tree_solution_proyect.Vistas.ui.comunicacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ComunicacionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ComunicacionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is chat");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
