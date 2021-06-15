package com.example.tree_solution_proyect.Objetos.Firebase;

import com.google.firebase.database.ServerValue;

public class Mensaje {

    //Inicializamos los atributos
    private String mensaje,userKey;
    private Object createTimestamp;

    //Metodo constructor vacio
    public Mensaje(){
        createTimestamp = ServerValue.TIMESTAMP;
        }
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
