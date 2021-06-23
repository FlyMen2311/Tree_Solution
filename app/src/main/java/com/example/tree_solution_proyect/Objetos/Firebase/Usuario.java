package com.example.tree_solution_proyect.Objetos.Firebase;

import java.io.Serializable;

public class Usuario implements Serializable {
    //Instanciamos los atributos
    private String userName,email,fotoPerfilUrl;
    private long fechaDeNacimiento;

    //Metodo constructor vacio
    public Usuario() {
    }

    //Seters y Geters
    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(long fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }
}
