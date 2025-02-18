package com.techelevator.dao;

import com.techelevator.model.Event;

import java.util.List;

public interface EventDao {
    List<Event> findEventsForGuest();
    List<Event> findEventsForHost(int userId);
    List<Event> findEventsForDJ(int userId);

    // ✅ Fix: Add these missing methods
    Event createEvent(Event event);
    Event findEventById(int id);
    Event updateEvent(int id, Event event);
    void deleteEvent(int id);
}

