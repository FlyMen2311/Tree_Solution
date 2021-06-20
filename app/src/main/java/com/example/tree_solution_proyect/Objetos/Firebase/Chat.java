package com.example.tree_solution_proyect.Objetos.Firebase;

import com.example.tree_solution_proyect.Objetos.Logica.LLibro;
import com.example.tree_solution_proyect.Objetos.Logica.LUsuario;
import com.google.firebase.database.ServerValue;

import java.io.Serializable;

public class Chat implements Serializable {
    private String keyemisor;
    private String keyreceptor;
    private String keylibro;

    private String FotoPrincipalUrl;
    private String nombrelibro;
    private String nombrePropietario;
    private String nombreUser;
    private Object createTimestamp;


    public Chat() {
        createTimestamp = ServerValue.TIMESTAMP;
    }

    public Object getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(Object createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public String getKeyemisor() {
        return keyemisor;
    }

    public void setKeyemisor(String keyemisor) {
        this.keyemisor = keyemisor;
    }

    public String getKeyreceptor() {
        return keyreceptor;
    }

    public void setKeyreceptor(String keyreceptor) {
        this.keyreceptor = keyreceptor;
    }

    public String getKeylibro() {
        return keylibro;
    }

    public void setKeylibro(String keylibro) {
        this.keylibro = keylibro;
    }

    public String getFotoPrincipalUrl() {
        return FotoPrincipalUrl;
    }

    public void setFotoPrincipalUrl(String fotoPrincipalUrl) {
        FotoPrincipalUrl = fotoPrincipalUrl;
    }

    public String getNombrelibro() {
        return nombrelibro;
    }

    public void setNombrelibro(String nombrelibro) {
        this.nombrelibro = nombrelibro;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }
}
