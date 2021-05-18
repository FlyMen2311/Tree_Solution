package com.example.tree_solution_proyect.Objetos.Firebase;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

public class Usuario {
    private String userName;
    private String email;
    private long fechaDeNacimiento;
    private String fotoPerfilUrl;


    public Usuario() {

    }
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