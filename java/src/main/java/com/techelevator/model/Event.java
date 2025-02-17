package com.techelevator.model;

import  java.time.LocalDate;

public class Event {

    private int eventID;
    private String name;
    private LocalDate eventDate;

    public Event(int eventID, String name, LocalDate eventDate) {
        this.eventID = eventID;
        this.name = name;
        this.eventDate = eventDate;
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
        this.eventDate = eventDate;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }
}


















