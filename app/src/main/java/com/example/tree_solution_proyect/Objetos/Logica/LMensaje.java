package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LMensaje implements Serializable {
    //Inicializamos los atributos
    private Mensaje mensaje;
    private String key;
    private LUsuario lUsuario;

    //Metodo Constructor
    public LMensaje(Mensaje mensaje, String key) {
        this.mensaje = mensaje;
        this.key = key;
    }


    //Metodo que sirve para obtener y devolver fecha de creacion de Mensaje
    public String obtenerFechaDeCreacionMensaje(){
        Date date=new Date(getCreateTimeLong());
        PrettyTime prettyTime=new PrettyTime(new Date(),Locale.getDefault());
        return prettyTime.format(date);

    }
    //Seters y Geters
    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public long getCreateTimeLong(){
        return (long) mensaje.getCreateTimestamp();
    }
    public LUsuario getlUsuario() {
        return lUsuario;
    }

    public void setlUsuario(LUsuario lUsuario) {
        this.lUsuario = lUsuario;
    }
}
