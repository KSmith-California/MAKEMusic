BEGIN TRANSACTION;

DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS dj;
DROP TABLE IF EXISTS host;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE dj (
	dj_id SERIAL NOT NULL,
	user_id int NOT NULL REFERENCES users(user_id),
	CONSTRAINT PK_dj PRIMARY KEY (dj_id)
);

CREATE TABLE host (
	host_id SERIAL NOT NULL,
	user_id int NOT NULL REFERENCES users(user_id)
	CONSTRAINT PK_host PRIMARY KEY (host_id)
);

CREATE TABLE song (
	song_id SERIAL,
	song_name varchar NOT NULL,
	song_genre varchar NOT NULL
);

CREATE TABLE events (
	event_id SERIAL,
	event_name varchar NOT NULL,
	event_date DATE NOT NULL,
	event_start TIME NOT NULL,
	event_end TIME NOT NULL,
	host_id int NOT NULL REFERENCES host(host_id),
	dj_id int NOT NULL REFERENCES dj(dj_id)
);



COMMIT TRANSACTION;
