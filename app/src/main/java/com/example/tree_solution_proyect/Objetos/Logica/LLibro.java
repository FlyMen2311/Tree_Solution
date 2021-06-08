package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Libro;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class LLibro {
    private Libro libro;
    private String key;
    private LUsuario LUsuario;

    public LLibro(Libro libro, String key) {
        this.libro = libro;
        this.key = key;
    }
    public String obtenerFechaDeCreacionLibro(){
        Date date=new Date(getCreateTimeLong());
        PrettyTime prettyTime=new PrettyTime(new Date(), Locale.getDefault());
        return prettyTime.format(date);

    }
    public long getCreateTimeLong(){
        return (long) libro.getCreateTimestamp();
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
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
