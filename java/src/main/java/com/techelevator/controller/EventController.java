package com.techelevator.controller;

import com.techelevator.dao.EventDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Event;
import com.techelevator.model.User;
import com.techelevator.security.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

/**
 * Controller for handling event-related requests.
 * Provides endpoints for retrieving events based on user role (Guest, Host, DJ).
 */
@RestController
@CrossOrigin // Allows requests from different origins (needed for frontend integration)
@RequestMapping("/events")
public class EventController {

    private final EventDao eventDao;
    private final UserDao userDao;

    public EventController(EventDao eventDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
    }

    /**
     * Retrieves a list of events.
     */
    @GetMapping
    public List<Event> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(required = false) Integer djId
    ) {
        String currentUsername = SecurityUtils.getCurrentUsername(); // Retrieve logged-in user's username

        if (currentUsername == null) {
            // If no user is logged in, return all public events (Guests)
            return eventDao.findFilteredEvents(name, date, djId);
        }

        // Retrieve user details from database
        User user = userDao.getUserByUsername(currentUsername);
        if (user == null) {
            // Prevents null user error and returns an appropriate HTTP response
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found. Please log in.");
        }

        // Ensure the user has at least one role before proceeding
        if (user.getAuthorities().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User has no assigned role.");
        }

        // Determine user role and return the appropriate events
        String role = user.getAuthorities().iterator().next().getName();
        switch (role) {
            case "ROLE_DJ":
                return eventDao.findEventsByDJ(user.getId()); // DJs see only their events
            case "ROLE_HOST":
                return eventDao.findEventsByHost(user.getId()); // Hosts see only their events
            default:
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied.");
        }
    }

    /**
     * Retrieves details of a single event by ID.
     */
    @GetMapping("/{id}")
    public Event getEventById(@PathVariable int id) {
        Event event = eventDao.findEventById(id);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found.");
        }
        return event;
    }
}
