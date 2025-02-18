package com.techelevator.model;

import java.time.LocalDate;

/**
 * Represents an Event in the system.
 * An event has a unique ID, name, date, and assigned users (DJ and Host).
 */
public class Event {

    private int eventID;
    private String name;
    private LocalDate eventDate;
    private Integer djId;
    private Integer hostId;

    public Event() {}

    public Event(int eventID, String name, LocalDate eventDate, Integer djId, Integer hostId) {
        this.eventID = eventID;
        this.name = name;
        this.eventDate = eventDate;
        this.djId = djId;
        this.hostId = hostId;
    }

    /**
     * Gets the event's unique identifier.
     */
    public int getEventID() {
        return eventID;
    }

    /**
     * Sets the event's unique identifier.
     */
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    /**
     * Gets the name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the event.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the date of the event.
     */
    public LocalDate getEventDate() {
        return eventDate;
    }

    /**
     * Sets the date of the event.
     */
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Gets the ID of the assigned DJ.
     */
    public Integer getDjId() {
        return djId;
    }

    /**
     * Sets the ID of the assigned DJ.
     */
    public void setDjId(Integer djId) {
        this.djId = djId;
    }

    /**
     * Gets the ID of the assigned Host.
     */
    public Integer getHostId() {
        return hostId;
    }

    /**
     * Sets the ID of the assigned Host.
     */
    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }
}
