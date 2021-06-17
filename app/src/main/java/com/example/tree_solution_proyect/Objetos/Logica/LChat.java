package com.example.tree_solution_proyect.Objetos.Logica;

import com.example.tree_solution_proyect.Objetos.Firebase.Chat;

import java.io.Serializable;

public class LChat implements Serializable {
  private String key;
  private Chat chat;


    public LChat( Chat chat,String key) {
        this.key = key;
        this.chat = chat;
    }

    public LChat() {

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


