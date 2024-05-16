package com.example.miagenda.api;

import java.util.Date;

public class Nota {

    private String id;
    private String username;
    private String note_desc;
    private Date note_date;

    // Métodos getter y setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNoteDesc() {
        return note_desc;
    }

    public void setNoteDesc(String note_desc) {
        this.note_desc = note_desc;
    }

    public Date getNoteDate() {
        return note_date;
    }

    public void setNoteDate(Date note_date) {
        this.note_date = note_date;
    }
}
