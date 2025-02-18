package com.techelevator.dao;

import com.techelevator.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventDao {

    List<Event> findAllEvents(); // Show all events
    List<Event> findFilteredEvents(String name, LocalDate date, Integer djId); // Filtered events
    List<Event> findEventsByDJ(int djId); // DJs see only their events
    List<Event> findEventsByHost(int hostId); // Hosts see only their events
    Event findEventById(int eventId); // Get event details
}
