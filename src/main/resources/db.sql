DROP DATABASE IF EXISTS top10;
CREATE DATABASE top10;
use  top10;
SET CHARSET 'utf8';

DROP TABLE IF EXISTS question_difficulty;
DROP TABLE IF EXISTS question_category;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS friendship;
DROP TABLE IF EXISTS friend_invitation;
DROP TABLE IF EXISTS game_invitation;
DROP TABLE IF EXISTS game_invitation_status;
DROP TABLE IF EXISTS share_invitation;
DROP TABLE IF EXISTS player;

CREATE TABLE question_difficulty (
  id BIGINT PRIMARY KEY auto_increment,
  description VARCHAR(80) NOT NULL
);

CREATE TABLE question_category (
   id BIGINT PRIMARY KEY auto_increment,
   name VARCHAR(100) NOT NULL
);

CREATE TABLE question (
  id BIGINT PRIMARY KEY auto_increment,
  description VARCHAR(500) NOT NULL,
  question_difficulty_id BIGINT NOT NULL,
  question_category_id BIGINT NOT NULL,
  FOREIGN KEY (question_difficulty_id) REFERENCES question_difficulty(id),
  FOREIGN KEY (question_category_id) REFERENCES question_category(id)
);

CREATE TABLE answer (
  id BIGINT PRIMARY KEY auto_increment,
  question_id BIGINT NOT NULL,
  description VARCHAR(200),
  display_description VARCHAR(200),
  points INT,
  FOREIGN KEY (question_id) REFERENCES question(id)
);

ALTER TABLE answer CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE question CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE player (
  id BIGINT PRIMARY KEY auto_increment,
  player_id VARCHAR(200) NOT NULL UNIQUE,
  username VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE player_details (
  player_id BIGINT PRIMARY KEY,
  elo INT NOT NULL,
  level INT NOT NULL,
  level_points INT NOT NULL,
  coins INT NOT NULL
);

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

-- Populate data
insert into question_difficulty(description)
values('LOW'), ('MEDIUM'), ('HARD');

insert into question_category(name)values
('Αθλητικά'),
('Ιστορία'),
('Γενικές γνώσεις'),
('Επιστήμη-Τεχνολογία'),
('Μουσική-Κινηματογράφος'),
('Γεωγραφία');

insert into question(description, question_difficulty_id, question_category_id)values
  ('Οχήματα με ρόδες', 1, 3),
  ('Site κοινωνικής δικτύωσης', 3, 4),
  ('Γλώσσες προγραμματισμού ', 1, 4),

  ('Οι μεγαλύτεροι έλληνες ηθοποιοί των 80s', 2, 5),
#   ('Φράσεις απο παιδικά', 2),
   ('Γνωστά επιτραπέζια', 2, 3),

('Φράσεις από διαφημίσεις', 3, 5);
#   ('Παραδοσιακά ελληνικά φαγητά', 2),
#
#   ('Διάσημοι ζωγράφοι', 3),
#   ('Ηρωες ελληνικής επανάστασης', 3),
#   ('Ξένες λέξεις που χρησιμοποιούμε καθημερινά', 3)
;

insert into answer(question_id, description, points)
values
  (1, 'Άμαξα', 1),
  (1, 'Λεωφορείο‎', 1),
  (1, 'Μοτοσικλέτα‎', 1),
  (1, 'Τρακτέρ‎', 2),
  (1, 'Φορτηγό‎', 1),
  (1, 'Πατίνι', 2),
  (1, 'Ποδήλατο', 2),
  (1, 'Αυτοκίνητο|Αμάξι‎', 1),
  (1, 'Δέσπα', 2),

  (2, 'Facebook', 1),
  (2, 'Twitter', 1),
  (2, 'Linkedin', 3),
  (2, 'Google+', 2),
  (2, 'YouTube', 1),
  (2, 'Instagram', 1),
  (2, 'Pinterest', 4),
  (2, 'Snapchat', 2),
  (2, 'Tumblr', 5),
  (2, 'Vine', 5),

  (3, 'Java', 1),
  (3, 'C', 2),
  (3, 'C++', 1),
  (3, 'Javascript', 2),
  (3, 'C#', 2),
  (3, 'Ruby', 2),
  (3, 'Python', 2),
  (3, 'Swift', 3),
  (3, 'Kotlin', 3),
  (3, 'Scala', 3);

insert into answer(question_id, description, display_description, points)values
(5, 'Scrabble|σκράμπλ', 'Scrabble', 1),
(5, 'Monopoly|μονόπολη','Monopoly', 1),
(5, 'Dixit|ντίξιτ', 'Dixit', 1),
(5, 'Tichu|τίτσου','Tichu', 4);

insert into answer(question_id, description, display_description,  points)
values
  (4, 'Ψάλτης', 'Στάθης Ψάλτης', 1),
  (4, 'Γαρδέλης', 'Σταμάτης Γαρδέλης', 1),
  (4,  'Φίνου','Καίτη Φίνου', 1),
  (4,  'Ευαγγελόπουλος','Παύλος Ευαγγελόπουλος', 4),
  (4,  'Αλιμπέρτη','Σοφία Αλιμπέρτη', 4),
  (4,  'Φιλίνη','Ελένη Φιλίνη', 4),
  (4,  'Μιχαλόπουλος','Πάνος Μιχαλόπουλος', 4),
  (4,  'Παναγοπούλου','Βάσια Παναγοπούλου', 5),
  (4,  'Πίκουλα','Εφη Πίκουλα', 5),
  (4,  'Μόσιος','Μιχάλης Μόσιος', 5);

insert into answer(question_id, description, display_description,  points)values
  (6, 'Τυχαίο', 'Τυχαίο Δε νομίζω',  1),
  (6, 'Ομορφάντρα', 'Ομορφάντρα μου', 1),
  (6, 'μακαρονάς', 'Είμαι μακαρονάς τι να κάνουμε',  4),
  (6,  'κορμί|πατριώτη', 'Το χάσαμε το κορμί πατριώτη',  4),
  (6,  'Τρατζικ', 'Τρατζικ', 4),
  (6,  'καμπάνα', 'Σήμα καμπάνα',  4);

insert into player( player_id, username)values
(102611879418639905272,	'Katiforis Lefteris'),
(112815880341047863213,	'George Katiforis');
insert into player_details(player_id,  coins, elo, level, level_points )values
(1, 100,	1990, 40, 234),
(2, 1000,	1690, 45, 2);

SELECT  * from answer;
SELECT  * from question;
SELECT  * from question_category;