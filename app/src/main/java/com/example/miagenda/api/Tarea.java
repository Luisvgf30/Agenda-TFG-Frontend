package com.example.miagenda.api;

import java.io.Serializable;
import java.time.LocalDate;
import com.google.gson.annotations.SerializedName;

public class Tarea implements Serializable {
    @SerializedName("task_name")
    private String taskName;
    @SerializedName("task_desc")
    private String taskDesc;
    @SerializedName("estado")
    private String estado;
    @SerializedName("task_level")
    private String task_level;
    @SerializedName("limit_date")
    private String dateLimit;
    @SerializedName("initial_date")
    private String dateInitial;
    @SerializedName("username")
    private String username;

/*    public Tarea(String taskName, String taskDesc, String estado, String task_level, String dateLimit, String username) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.estado = estado;
        this.task_level = task_level;
        this.dateLimit = dateLimit;
        this.username = username;
    }*/

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTask_level() {
        return task_level;
    }

    public void setTask_level(String task_level) {
        this.task_level = task_level;
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
}
