package com.techelevator.controller;

import com.techelevator.dao.EventDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.CreateEventDto;
import com.techelevator.model.Event;
import com.techelevator.security.SecurityUtils;
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
    private final UserDao userDao;

    public EventController(EventDao eventDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
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
     * Allows a DJ to create an event and assign host(s) to it.
     * The event creator is assumed to be the DJ.
     */
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody CreateEventDto createEventDto) {
        // Verify that the user is logged in.
        String currentUsername = SecurityUtils.getCurrentUsername();
        if (currentUsername == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login required to create an event.");
        }
        // Ensure the user has the DJ role.
        if (!SecurityUtils.userHasRole("ROLE_DJ")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only DJs can create events.");
        }
        // Validate that at least one host is assigned.
        if (createEventDto.getHostIds() == null || createEventDto.getHostIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("At least one host must be assigned to the event.");
        }
        // Create the event from the provided details.
        Event event = new Event();
        event.setName(createEventDto.getName());
        event.setEventDate(createEventDto.getEventDate());
        event.setStartTime(createEventDto.getStartTime());
        event.setEndTime(createEventDto.getEndTime());

        // Optionally, you could set the creator (DJ) on the event if your Event model and DAO support it.
        // For example: event.setCreatedBy(currentUserId);

        // Insert the event into the database.
        Event createdEvent = eventDao.createEvent(event);

        // Now assign each host to the event.
        for (Integer hostId : createEventDto.getHostIds()) {
            eventDao.assignHost(createdEvent.getEventID(), hostId);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }
}
