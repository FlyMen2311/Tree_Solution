package com.example.tree_solution_proyect.Objetos.Firebase;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;

public class Mensaje implements  Serializable{

    //Inicializamos los atributos
    private String mensaje,userKey;
    private Object createTimestamp;

    //Metodo constructor
    public Mensaje(){
        createTimestamp = ServerValue.TIMESTAMP;
        }

    //Seters y Geters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    public Object getCreateTimestamp() {
        return createTimestamp;
    }

}
