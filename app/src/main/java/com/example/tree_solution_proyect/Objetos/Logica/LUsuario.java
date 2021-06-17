package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Usuario;
import com.example.tree_solution_proyect.Persistencia.UsuarioDAO;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LUsuario implements Serializable {
    private Usuario usuario;
    private String key;

    public LUsuario(Usuario usuario, String key) {
        this.usuario = usuario;
        this.key = key;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String obtenerFechaDeCreacionUsuario(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date =new Date(UsuarioDAO.getInstance().fechaDeCreacion());
        return  simpleDateFormat.format(date);
    }

    public String obtenerFechaDeLastLogIn(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date =new Date(UsuarioDAO.getInstance().fechaDeCreacion());
        return  simpleDateFormat.format(date);
    }
}
