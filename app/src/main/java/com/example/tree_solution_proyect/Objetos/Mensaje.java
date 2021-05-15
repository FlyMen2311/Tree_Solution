package com.example.tree_solution_proyect.Objetos;

import java.io.Serializable;

public class Mensaje {

    //Inicializamos los atributos
    private String nombre;
    private String hora;
    private String mensaje;
    private String foto_perfil;
    private String tipo_mensaje;

    //Metodo constructor
    public Mensaje(String nombre, String hora, String mensaje, String foto_perfil) {
        this.nombre = nombre;
        this.hora = hora;
        this.mensaje = mensaje;
        this.foto_perfil = foto_perfil;
    }

    //Metodo constructor vacio
    public Mensaje(){}

    //Geters y Seters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getTipo_mensaje() {
        return tipo_mensaje;
    }

    public void setTipo_mensaje(String tipo_mensaje) {
        this.tipo_mensaje = tipo_mensaje;
    }
}
