BEGIN TRANSACTION;

-- Drop tables in reverse dependency order
DROP TABLE IF EXISTS playlist_songs;
DROP TABLE IF EXISTS playlists;
DROP TABLE IF EXISTS event_hosts;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS songs;
DROP TABLE IF EXISTS users;

-- Users Table: Only DJ and Host roles allowed.
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_DJ', 'ROLE_HOST'))
);

-- Events Table with created_by column referencing the DJ who created the event.
CREATE TABLE events (
    event_id SERIAL PRIMARY KEY,
    event_name VARCHAR(100) NOT NULL,
    event_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    created_by INT NOT NULL,
    FOREIGN KEY (created_by) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Event_Host Table: Links events to hosts.
CREATE TABLE event_hosts (
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Songs Table
CREATE TABLE songs (
    song_id SERIAL PRIMARY KEY,
    song_name VARCHAR(200) NOT NULL,
    song_genre VARCHAR(100) NOT NULL
);

-- Playlists Table
CREATE TABLE playlists (
    playlist_id SERIAL PRIMARY KEY,
    event_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);

-- Playlist_Songs Table
CREATE TABLE playlist_songs (
    playlist_song_id SERIAL PRIMARY KEY,
    playlist_id INT NOT NULL,
    song_id INT NOT NULL,
    added_by INT NOT NULL,
    FOREIGN KEY (playlist_id) REFERENCES playlists(playlist_id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(song_id) ON DELETE CASCADE,
    FOREIGN KEY (added_by) REFERENCES users(user_id) ON DELETE SET NULL
);

COMMIT TRANSACTION;
