package com.techelevator.controller;

import com.techelevator.dao.EventDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Event;
import com.techelevator.model.User;
import com.techelevator.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/events")
public class EventController {

    private final EventDao eventDao;
    private final UserDao userDao;

    public EventController(EventDao eventDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
    }

    /**
     * GET /events - Retrieve events based on user role
     * Guests see all events, Hosts and DJs see only their events.
     */
    @GetMapping
    public List<Event> getEvents() {
        String currentUsername = SecurityUtils.getCurrentUsername();

        if (currentUsername == null) {
            return eventDao.findEventsForGuest(); // Guests see all events
        }

        User user = userDao.getUserByUsername(currentUsername);
        String role = user.getAuthorities().iterator().next().getName(); // Single role

        switch (role) {
            case "ROLE_DJ":
                return eventDao.findEventsForDJ(user.getId());
            case "ROLE_HOST":
                return eventDao.findEventsForHost(user.getId());
            default:
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }
    }

    /**
     * POST /events - Create a new event (Only authenticated users)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@RequestBody Event event, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You must be logged in to create an event.");
        }

        // Get the logged-in user
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }

        // Set the event owner
        event.setUserId(user.getId());

        // Save event in database
        return eventDao.createEvent(event);
    }

    /**
     * GET /events/{id} - Retrieve a single event by ID
     */
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable int id) {
        Event event = eventDao.findEventById(id);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found.");
        }
        return event;
    }

    /**
     * PUT /events/{id} - Update an event (Only event owner can update)
     */
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable int id, @RequestBody Event updatedEvent, Principal principal) {
        Event existingEvent = eventDao.findEventById(id);
        if (existingEvent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found.");
        }

        // Ensure only the event owner can update it
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        if (user.getId() != existingEvent.getUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this event.");
        }

        return eventDao.updateEvent(id, updatedEvent);
    }

    /**
     * DELETE /events/{id} - Delete an event (Only event owner can delete)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable int id, Principal principal) {
        Event existingEvent = eventDao.findEventById(id);
        if (existingEvent == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found.");
        }

        // Ensure only the event owner can delete it
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        if (user.getId() != existingEvent.getUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this event.");
        }

        eventDao.deleteEvent(id);
    }
}
