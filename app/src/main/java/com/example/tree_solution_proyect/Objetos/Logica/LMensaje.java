package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Mensaje;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LMensaje {
    private Mensaje mensaje;
    private String key;
    private LUsuario lUsuario;

    public LMensaje(Mensaje mensaje, String key) {
        this.mensaje = mensaje;
        this.key = key;
    }

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

    public String obtenerFechaDeCreacionMensaje(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm:ss a",Locale.getDefault());
        Date date =new Date(getCreateTimeLong());
        return  simpleDateFormat.format(date);
    }

    public LUsuario getlUsuario() {
        return lUsuario;
    }

    public void setlUsuario(LUsuario lUsuario) {
        this.lUsuario = lUsuario;
    }
}
