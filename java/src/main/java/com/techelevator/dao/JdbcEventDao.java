package com.techelevator.dao;

import com.techelevator.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcEventDao implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> findEventsForGuest() {
        String sql = "SELECT event_id, name, event_date, user_id FROM events";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        return mapEvents(rowSet);
    }

    @Override
    public List<Event> findEventsForHost(int userId) {
        String sql = "SELECT event_id, name, event_date, user_id FROM events WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        return mapEvents(rowSet);
    }

    @Override
    public List<Event> findEventsForDJ(int userId) {
        String sql = "SELECT event_id, name, event_date, user_id FROM events WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        return mapEvents(rowSet);
    }

    @Override
    public Event createEvent(Event event) {
        String sql = "INSERT INTO events (name, event_date, user_id) VALUES (?, ?, ?) RETURNING event_id";
        int eventId = jdbcTemplate.queryForObject(sql, Integer.class, event.getName(), event.getEventDate(), event.getUserId());
        event.setEventID(eventId);
        return event;
    }

    @Override
    public Event findEventById(int id) {
        String sql = "SELECT event_id, name, event_date, user_id FROM events WHERE event_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            return mapRowToEvent(rowSet);
        }
        return null;
    }

    @Override
    public Event updateEvent(int id, Event event) {
        String sql = "UPDATE events SET name = ?, event_date = ? WHERE event_id = ?";
        jdbcTemplate.update(sql, event.getName(), event.getEventDate(), id);
        return findEventById(id);
    }

    @Override
    public void deleteEvent(int id) {
        String sql = "DELETE FROM events WHERE event_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private List<Event> mapEvents(SqlRowSet rowSet) {
        List<Event> events = new ArrayList<>();
        while (rowSet.next()) {
            events.add(mapRowToEvent(rowSet));
        }
        return events;
    }

    private Event mapRowToEvent(SqlRowSet rs) {
        Event event = new Event();
        event.setEventID(rs.getInt("event_id"));
        event.setName(rs.getString("name"));
        event.setEventDate(rs.getDate("event_date").toLocalDate());
        event.setUserId(rs.getInt("user_id"));
        return event;
    }
}
