package com.example.miagenda.api;

import android.os.Build;

import java.time.LocalDate;

public class Tarea {
    private String taskName;
    private String taskDesc;
    private String state;
    private String document;
    private LocalDate dateLimit;
    private LocalDate dateInitial;
    private String username;

    public Tarea(String taskName, String taskDesc, LocalDate dateLimit, String username) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.dateLimit = dateLimit;
        this.username = username;
    }

    // Getters y Setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public LocalDate getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(LocalDate dateLimit) {
        this.dateLimit = dateLimit;
    }

    public LocalDate getDateInitial() {
        return dateInitial;
    }

    public void setDateInitial(LocalDate dateInitial) {
        this.dateInitial = dateInitial;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // toString() para representación de cadena
    @Override
    public String toString() {
        return "Tarea{" +
                "taskName='" + taskName + '\'' +
                ", taskDesc='" + taskDesc + '\'' +
                ", state='" + state + '\'' +
                ", document='" + document + '\'' +
                ", dateLimit=" + dateLimit +
                ", dateInitial=" + dateInitial +
                ", username='" + username + '\'' +
                '}';
    }
}
