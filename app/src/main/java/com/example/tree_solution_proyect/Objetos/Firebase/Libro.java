package com.example.tree_solution_proyect.Objetos.Firebase;

import android.net.Uri;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;

public class Libro implements Serializable {
    private String FotoPrincipalUrl;
    private String nombre;
    private String autor;
    private String condition;
    private String ISBN;
    private Double precio;
    private Object createTimestamp;
    private String categoria;
    private String userKey;
    private String referenceStorage;
    private String descripcion;


    public Libro() {
        createTimestamp = ServerValue.TIMESTAMP;
    }
    public String getUserKey() {
        return userKey;
    }

    public String getReferenceStorage() {
        return referenceStorage;
    }

    public void setReferenceStorage(String referenceStorage) {
        this.referenceStorage = referenceStorage;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCreateTimestamp(Object createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getFotoPrincipalUrl() {
        return FotoPrincipalUrl;
    }

    public void setFotoPrincipalUrl(String fotoPrincipalUrl) {
        FotoPrincipalUrl = fotoPrincipalUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    public Object getCreateTimestamp() {
        return createTimestamp;
    }
}
