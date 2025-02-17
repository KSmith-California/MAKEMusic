package com.techelevator.dao;

import com.techelevator.model.Event;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcEventDao implements EventDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEventDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> findAllEvents() {
        String sql = "SELECT event_id, name, event_date, user_id " + "FROM events";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        return mapEvents(rowSet);
    }

    @Override
    public List<Event> findEventsByFilters(String name, LocalDate date) {
        StringBuilder sql = new StringBuilder("SELECT event_id, name, event_date, user_id FROM events WHERE 1+1"
        );
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

    @Override
    public List<Event> findEventsByUserId(int userId) {
        String sql = "SELECT event_id, name, event_date, user_id " + "FROM events WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        return mapEvents(rowSet);
    }

    private List<Event> mapEvents(SqlRowSet rowSet) {
        List<Event> events = new ArrayList<>();
        while (rowSet.next()) {
            Event e = new Event();
            e.setEventID(rowSet.getInt("event_id"));
            e.setName(rowSet.getString("name"));
            e.setEventDate(rowSet.getDate("event_date").toLocalDate());
            e.setUserId(rowSet.getObject("user_id", Integer.class)); // Proper casting
            events.add(e);
        }
        return events;
    }
}






















}
