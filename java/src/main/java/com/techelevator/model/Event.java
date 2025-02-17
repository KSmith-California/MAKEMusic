package com.techelevator.model;

import org.springframework.data.relational.core.sql.In;

import  java.time.LocalDate;

public class Event {

    private int eventID;
    private String name;
    private LocalDate eventDate;
    private Integer userId;

    public Event() {}

    public Event(int eventID, String name, LocalDate eventDate) {
        this.eventID = eventID;
        this.name = name;
        this.eventDate = eventDate;
        this.userId = userId;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId;
    }
}


















