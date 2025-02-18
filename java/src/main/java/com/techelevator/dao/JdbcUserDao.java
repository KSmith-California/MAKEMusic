package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.User;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.Authority;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JdbcUserDao implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT user_id, username, password_hash, role FROM users";
        List<User> users = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                users.add(mapRowToUser(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to the database", e);
        }

        return users;
    }


    @Override
    public User getUserById(int id) {
        // ✅ FIX: Now correctly implements getUserById
        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE user_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if (rowSet.next()) {
                return mapRowToUser(rowSet);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return null;
    }
    @Override
    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, role, activated FROM users WHERE username = LOWER(TRIM(?))";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);

        if (rowSet.next()) {
            User user = new User();
            user.setId(rowSet.getInt("user_id"));
            user.setUsername(rowSet.getString("username"));
            user.setPassword(rowSet.getString("password_hash"));
            user.setActivated(rowSet.getBoolean("activated")); // ✅ Ensure activated is properly retrieved

            Set<Authority> authorities = new HashSet<>();
            authorities.add(new Authority(rowSet.getString("role")));
            user.setAuthorities(authorities);

            return user;
        }
        return null;
    }

    @Override
    public List<User> getHosts() {
        String sql = "SELECT user_id, username FROM users WHERE role = 'ROLE_HOST'";
        List<User> hosts = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next()) {
            hosts.add(mapRowToUser(rowSet));
        }
        return hosts;
    }

    @Override
    public List<User> getDJs() {
        String sql = "SELECT user_id, username FROM users WHERE role = 'ROLE_DJ'";
        List<User> djs = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next()) {
            djs.add(mapRowToUser(rowSet));
        }
        return djs;
    }
    @Override
    public User createUser(RegisterUserDto user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Username and password cannot be null");
        }

        String username = user.getUsername().trim().toLowerCase();
        String passwordHash = passwordEncoder.encode(user.getPassword());

        List<String> validRoles = List.of("ROLE_GUEST", "ROLE_HOST", "ROLE_DJ");
        String role = user.getRole().toUpperCase().startsWith("ROLE_") ? user.getRole().toUpperCase() : "ROLE_" + user.getRole().toUpperCase();

        if (!validRoles.contains(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        String insertUserSql = "INSERT INTO users (username, password_hash, role, activated) VALUES (?, ?, ?, ?) RETURNING user_id";

        try {
            int newUserId = jdbcTemplate.queryForObject(insertUserSql, Integer.class, username, passwordHash, role, true);
            return getUserById(newUserId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Username already exists or data integrity violation", e);
        }
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));

        Set<Authority> authorities = new HashSet<>();
        authorities.add(new Authority(rs.getString("role")));
        user.setAuthorities(authorities);

        return user;
    }
}
