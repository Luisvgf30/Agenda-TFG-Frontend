package com.example.miagenda.api;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;

public class Usuario implements Serializable {

    public  String username;
    public String password;
    public  String email;
    public String foto;
    public Date diary_time;
    public String token;
    public  Tarea tareas;
    public  Nota notas;
    public  Evento eventos;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Date getDiary_time() {
        return diary_time;
    }

    public void setDiary_time(Date diary_time) {
        this.diary_time = diary_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Tarea getTareas() {
        return tareas;
    }

    public void setTareas(Tarea tareas) {
        this.tareas = tareas;
    }

    public Nota getNotas() {
        return notas;
    }

    public void setNotas(Nota notas) {
        this.notas = notas;
    }

    public Evento getEventos() {
        return eventos;
    }

    public void setEventos(Evento eventos) {
        this.eventos = eventos;
    }

    public Usuario(String username, String password, String email,
                   String foto, Date diary_time, String token, Tarea tareas,
                   Nota notas, Evento eventos) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.foto = foto;
        this.diary_time = diary_time;
        this.token = token;
        this.tareas = tareas;
        this.notas = notas;
        this.eventos = eventos;
    }

    public Usuario(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
