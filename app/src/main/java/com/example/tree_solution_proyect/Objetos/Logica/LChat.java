package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Chat;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class LChat implements Serializable {
    //Inicializamos los atributos
  private String key;
  private Chat chat;

    //Metodo Constructor
    public LChat( Chat chat,String key) {
        this.key = key;
        this.chat = chat;
    }

    //Metodo que sirve para obtener y devolver fecha de creacion Chat
    public String obtenerFechaDeCreacionChat() throws ClassCastException{
        Date date=new Date(getCreateTimeLong());
        PrettyTime prettyTime=new PrettyTime(new Date(), Locale.getDefault());
        return prettyTime.format(date);
    }

    //Seters y Geters
    public long getCreateTimeLong(){
        Long aLong=Long.parseLong(String.valueOf(chat.getCreateTimestamp()));
        return aLong;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }
}


