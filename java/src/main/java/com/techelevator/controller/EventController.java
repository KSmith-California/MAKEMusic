package com.techelevator.controller;

import com.techelevator.dao.EventDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Event;
import com.techelevator.security.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public List<Event> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date
    ) {

        String currentUsername = SecurityUtils.getCurrentUsername();
        if (currentUsername == null) {
            return eventDao.findEventsByFilters(name, date);
        } else {
            boolean isAdmin = SecurityUtils.userHasRole("ROLE_ADMIN");
            if (isAdmin) {
                return eventDao.findEventsByFilters(name, date);
            } else {

                int userId = userDao.getUserByUsername(currentUsername).getId();
                return eventDao.findEventsByUserId(userId);
            }
        }
    }
}











