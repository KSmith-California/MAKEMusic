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
 * This class retrieves, filters, and maps events stored in the database.
 */
@Component // Marks this class as a Spring-managed component
public class JdbcEventDao implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves a list of all events.
     */
    @Override
    public List<Event> findAllEvents() {
        String sql = "SELECT event_id, name, event_date, dj_id, host_id FROM events";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        return mapEvents(rowSet);
    }

    /**
     * Retrieves a filtered list of events based on optional parameters.
     */
    @Override
    public List<Event> findFilteredEvents(String name, LocalDate date, Integer djId) {
        StringBuilder sql = new StringBuilder("SELECT event_id, name, event_date, dj_id, host_id FROM events WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            sql.append(" AND LOWER(name) LIKE LOWER(?)");
            params.add("%" + name + "%");
        }

        if (date != null) {
            sql.append(" AND event_date = ?");
            params.add(date);
        }

        if (djId != null) {
            sql.append(" AND dj_id = ?");
            params.add(djId);
        }

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql.toString(), params.toArray());
        return mapEvents(rowSet);
    }

    /**
     * Retrieves events assigned to a specific DJ.
     */
    @Override
    public List<Event> findEventsByDJ(int djId) {
        String sql = "SELECT event_id, name, event_date, dj_id, host_id FROM events WHERE dj_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, djId);
        return mapEvents(rowSet);
    }

    /**
     * Retrieves events assigned to a specific Host.
     */
    @Override
    public List<Event> findEventsByHost(int hostId) {
        String sql = "SELECT event_id, name, event_date, dj_id, host_id FROM events WHERE host_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, hostId);
        return mapEvents(rowSet);
    }

    /**
     * Retrieves a specific event by its ID.
     */
    @Override
    public Event findEventById(int eventId) {
        String sql = "SELECT event_id, name, event_date, dj_id, host_id FROM events WHERE event_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, eventId);

        if (rowSet.next()) {
            return mapRowToEvent(rowSet);
        }
        return null;
    }

    /**
     * Maps a rowset of multiple event records to a list of Event objects.
     */
    private List<Event> mapEvents(SqlRowSet rowSet) {
        List<Event> events = new ArrayList<>();
        while (rowSet.next()) {
            events.add(mapRowToEvent(rowSet));
        }
        return events;
    }

    /**
     * Maps a single row from the SQL result set to an Event object.
     */
    private Event mapRowToEvent(SqlRowSet rowSet) {
        Event event = new Event();
        event.setEventID(rowSet.getInt("event_id"));
        event.setName(rowSet.getString("name"));
        event.setEventDate(rowSet.getDate("event_date").toLocalDate());
        event.setDjId(rowSet.getInt("dj_id"));
        event.setHostId(rowSet.getInt("host_id"));
        return event;
    }
}
