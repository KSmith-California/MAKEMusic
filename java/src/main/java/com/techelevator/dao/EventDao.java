package com.techelevator.dao;

import com.techelevator.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventDao {

    List<Event> findAllEvents();

    List<Event> findEventsByFilters(String name, LocalDate date);

    List<Event> findEventsByUserId(int userId);
}
