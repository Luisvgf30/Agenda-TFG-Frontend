package com.example.miagenda.api;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Evento implements Serializable {
    @SerializedName("event_name")
    private String name_event;

    @SerializedName("event_desc")
    private String event_desc;

    @SerializedName("event_date")
    private String event_date;

    // Getters and Setters
    public String getName_event() {
        return name_event;
    }

    public void setName_event(String name_event) {
        this.name_event = name_event;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }
}

