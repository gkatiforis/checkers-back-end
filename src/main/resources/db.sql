DROP DATABASE IF EXISTS checkers_db;
CREATE DATABASE checkers_db;
use checkers_db;
SET CHARSET 'utf8';

DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS friend_invitation;
DROP TABLE IF EXISTS game_invitation;
DROP TABLE IF EXISTS game_invitation_status;
DROP TABLE IF EXISTS share_invitation;
DROP TABLE IF EXISTS player;

# ALTER TABLE user CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE user (
  id BIGINT PRIMARY KEY auto_increment,
  user_id VARCHAR(30) NOT NULL UNIQUE,
  username VARCHAR(20) NOT NULL UNIQUE,
  email VARCHAR(40),
  name VARCHAR(80),
  picture_url VARCHAR(200)
);

CREATE TABLE player_details (
  user_id BIGINT PRIMARY KEY,
  elo INT NOT NULL,
  level INT NOT NULL,
  level_points INT NOT NULL,
  coins INT NOT NULL
);

# TODO: implement TABLE_PER_CLASS inheritance
CREATE TABLE friend_invitation (
  id BIGINT PRIMARY KEY auto_increment,
  from_player_id BIGINT NOT NULL,
  to_player_id BIGINT NOT NULL,
  request_date DATETIME NOT NULL,
  UNIQUE KEY friend_invitation (from_player_id, to_player_id)
);

CREATE TABLE game_invitation (
  id BIGINT PRIMARY KEY auto_increment,
  from_player_id BIGINT NOT NULL,
  to_player_id BIGINT NOT NULL,
  status_id INT NOT NULL,
  is_private BOOLEAN NOT NULL,
  request_date DATETIME NOT NULL,
  UNIQUE KEY game_invitation (from_player_id, to_player_id)
);

CREATE TABLE game_invitation_status (
  id INT PRIMARY KEY NOT NULL,
  description VARCHAR(200) NOT NULL
);

CREATE TABLE share_invitation (
  id BIGINT PRIMARY KEY auto_increment,
  from_player_id BIGINT NOT NULL,
  to_player_id BIGINT NOT NULL,
  code VARCHAR(100) NOT NULL,
  request_date DATETIME NOT NULL,
  UNIQUE KEY game_invitation_players (from_player_id, to_player_id)
);

CREATE TABLE friendship(
  id BIGINT PRIMARY KEY auto_increment,
  player_id1 BIGINT NOT NULL,
  player_id2 BIGINT NOT NULL,
  UNIQUE KEY friendship_player (player_id1, player_id2)
);

# TODO: implement TABLE_PER_CLASS inheritance
CREATE TABLE notification (
  id BIGINT PRIMARY KEY auto_increment,
  is_new BOOLEAN NOT NULL DEFAULT TRUE,
   creation_date DATETIME
);

CREATE TABLE invite_notification (
  id BIGINT PRIMARY KEY auto_increment,
  is_new BOOLEAN NOT NULL DEFAULT TRUE,
  creation_date DATETIME,
  invitation_id BIGINT
);

CREATE TABLE message_notification (
   id BIGINT PRIMARY KEY auto_increment,
   is_new BOOLEAN NOT NULL DEFAULT TRUE,
   creation_date DATETIME,
   description VARCHAR(500)
);