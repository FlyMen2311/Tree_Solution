package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Libro;
import com.example.tree_solution_proyect.Persistencia.LibroDAO;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LLibro implements Serializable  {
    private Libro libro;
    private LLibro lLibro;
    private String key;
    private LUsuario LUsuario;

    public LLibro(Libro libro, String key) {
        this.libro = libro;
        this.key = key;
    }

    public String obtenerFechaDeCreacionLibro() throws ClassCastException{
        Date date=new Date(getCreateTimeLong());
        PrettyTime prettyTime=new PrettyTime(new Date(),Locale.getDefault());
        return prettyTime.format(date);
    }
    public long getCreateTimeLong(){
        Long aLong=Long.parseLong(String.valueOf(libro.getCreateTimestamp()));
        return aLong;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public LLibro getlLibro() {
        return lLibro;
    }

    public void setlLibro(LLibro lLibro) {
        this.lLibro = lLibro;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LUsuario getLUsuario() {
        return LUsuario;
    }

    public void setLUsuario(LUsuario LUsuario) {
        this.LUsuario = LUsuario;
    }



}
