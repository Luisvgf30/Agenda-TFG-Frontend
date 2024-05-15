package com.example.miagenda.api;

import java.sql.Date;

public class Tarea {

    public String task_name;
    public String task_desc;
    public String state;
    public String document;
    public Date date_limit;
    public  Date date_initial;

    public Tarea(String task_name, String task_desc, String state, String document, Date date_limit, Date date_initial) {
        this.task_name = task_name;
        this.task_desc = task_desc;
        this.state = state;
        this.document = document;
        this.date_limit = date_limit;
        this.date_initial = date_initial;
    }
}
