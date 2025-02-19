package com.techelevator.dao;

import com.techelevator.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for handling database interactions related to events.
 */
@Component
public class JdbcEventDao implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> findAllEvents() {
        String sql = "SELECT event_id, name, event_date, start_time, end_time FROM events";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        return mapEvents(rowSet);
    }

    @Override
    public Event createEvent(Event event) {
        String sql = "INSERT INTO events (name, event_date, start_time, end_time) " +
                "VALUES (?, ?, ?, ?) RETURNING event_id";

        try {
            System.out.println("DEBUG: Inserting event -> Name: " + event.getName() +
                    ", Date: " + event.getEventDate() +
                    ", Start Time: " + event.getStartTime() +
                    ", End Time: " + event.getEndTime());

            int eventId = jdbcTemplate.queryForObject(sql, Integer.class,
                    event.getName(),
                    java.sql.Date.valueOf(event.getEventDate()),  // ✅ Ensures correct DATE format
                    java.sql.Time.valueOf(event.getStartTime().toString()),  // ✅ Converts LocalTime to TIME
                    java.sql.Time.valueOf(event.getEndTime().toString()));   // ✅ Converts LocalTime to TIME

            event.setEventID(eventId);
            System.out.println("DEBUG: Event inserted successfully! ID: " + eventId);
            return event;
        } catch (Exception e) {
            System.out.println("ERROR: SQL Insert Failed. " + e.getMessage());
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    @Override
    public Event findEventById(int eventId) {
        String sql = "SELECT event_id, name, event_date, start_time, end_time FROM events WHERE event_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, eventId);

        if (rowSet.next()) {
            return mapRowToEvent(rowSet);
        }
        return null;
    }

    @Override
    public List<Event> findFilteredEvents(String name, LocalDate date) {
        StringBuilder sql = new StringBuilder("SELECT event_id, name, event_date, start_time, end_time FROM events WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + name + "%");
        }

        if (date != null) {
            sql.append(" AND event_date = ?");
            params.add(date);
        }

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql.toString(), params.toArray());
        return mapEvents(rowSet);
    }

    private List<Event> mapEvents(SqlRowSet rowSet) {
        List<Event> events = new ArrayList<>();
        while (rowSet.next()) {
            events.add(mapRowToEvent(rowSet));
        }
        return events;
    }

    private Event mapRowToEvent(SqlRowSet rowSet) {
        Event event = new Event();
        event.setEventID(rowSet.getInt("event_id"));
        event.setName(rowSet.getString("name"));
        event.setEventDate(rowSet.getDate("event_date").toLocalDate());
        event.setStartTime(rowSet.getTime("start_time").toLocalTime());
        event.setEndTime(rowSet.getTime("end_time").toLocalTime());
        return event;
    }
}
