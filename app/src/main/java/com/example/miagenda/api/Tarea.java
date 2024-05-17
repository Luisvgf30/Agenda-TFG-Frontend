package com.example.miagenda.api;

import java.time.LocalDate;
import com.google.gson.annotations.SerializedName;

public class Tarea {
    @SerializedName("task_name")
    private String taskName;
    @SerializedName("task_desc")
    private String taskDesc;
    @SerializedName("state")
    private String state;
    @SerializedName("document")
    private String document;
    @SerializedName("date_limit")
    private String dateLimit;
    @SerializedName("date_initial")
    private String dateInitial;
    @SerializedName("username")
    private String username;

    public Tarea(String taskName, String taskDesc, String dateLimit, String username) {
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

    public String getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(String dateLimit) {
        this.dateLimit = dateLimit;
    }

    public String getDateInitial() {
        return dateInitial;
    }

    public void setDateInitial(String dateInitial) {
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
