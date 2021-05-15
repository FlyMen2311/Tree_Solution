package com.example.tree_solution_proyect.Vistas.ui.NewPost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewPostViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NewPostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}