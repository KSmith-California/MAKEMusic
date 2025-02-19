package com.techelevator.controller;

import com.techelevator.dao.EventDao;
import com.techelevator.model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for handling event-related requests.
 * Provides endpoints for retrieving and creating events.
 */
@RestController
@CrossOrigin
@RequestMapping("/events")
public class EventController {

    private final EventDao eventDao;

    public EventController(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    /**
     * Retrieves a list of events.
     */
    @GetMapping
    public List<Event> getEvents(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) LocalDate date) {
        return eventDao.findFilteredEvents(name, date);
    }

    /**
     * Retrieves details of a single event by ID.
     */
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable int id) {
        Event event = eventDao.findEventById(id);
        if (event == null) {
            throw new RuntimeException("Event not found.");
        }
        return event;
    }

    /**
     * Allows users to create an event.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event createdEvent = eventDao.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }
}
