package com.techelevator.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an Event in the system.
 */
public class Event {

    private int eventID;
    private String name;
    private LocalDate eventDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Event() {}

    public Event(int eventID, String name, LocalDate eventDate, LocalTime startTime, LocalTime endTime) {
        this.eventID = eventID;
        this.name = name;
        this.eventDate = eventDate;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
