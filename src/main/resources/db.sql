DROP DATABASE IF EXISTS top10;
CREATE DATABASE top10;
use  top10;
SET CHARSET 'utf8';

# DROP DATABASE IF EXISTS top;
# CREATE DATABASE top;
# use  top;
# # SET CHARSET 'utf8';

DROP TABLE IF EXISTS question_difficulty;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS active_game_answer;
DROP TABLE IF EXISTS active_game;
DROP TABLE IF EXISTS  active_game_question;
DROP TABLE IF EXISTS active_game_player;

DROP TABLE IF EXISTS player;





CREATE TABLE question_difficulty (
  id BIGINT PRIMARY KEY auto_increment,
  description VARCHAR(80)
);

CREATE TABLE question (
  id BIGINT PRIMARY KEY auto_increment,
  description VARCHAR(500),
  question_difficulty_id BIGINT,
  FOREIGN KEY (question_difficulty_id) REFERENCES question_difficulty(id)
);

CREATE TABLE answer (
  id BIGINT PRIMARY KEY auto_increment,
  question_id BIGINT,
  description VARCHAR(200),
  display_description VARCHAR(200),
#   identical BOOLEAN DEFAULT  FALSE,
  points INT,
  FOREIGN KEY (question_id) REFERENCES question(id)
);
ALTER TABLE answer CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
ALTER TABLE question CONVERT TO CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE TABLE player (
  id BIGINT PRIMARY KEY auto_increment,
  player_id VARCHAR(200),
  username VARCHAR(200)
);


CREATE TABLE active_game (
  id BIGINT PRIMARY KEY auto_increment,
  active_player INT,
  date_started datetime
);


CREATE TABLE active_game_question (
  id BIGINT PRIMARY KEY auto_increment,
    active_game_id BIGINT,
  question_id BIGINT,
   FOREIGN KEY (active_game_id) REFERENCES active_game(id),
  FOREIGN KEY (question_id) REFERENCES question(id)
);


CREATE TABLE active_game_player (
  id BIGINT PRIMARY KEY auto_increment,
    active_game_id BIGINT,
  player_id BIGINT,
   FOREIGN KEY (active_game_id) REFERENCES active_game(id),
  FOREIGN KEY (player_id) REFERENCES player(id)
);

CREATE TABLE active_game_answer (
    id BIGINT PRIMARY KEY auto_increment,
   answer_id BIGINT UNIQUE,
   player_id BIGINT,
   question_id BIGINT,
   is_correct BOOLEAN,
   active_game_id BIGINT,
   FOREIGN KEY (player_id) REFERENCES player(id),
   FOREIGN KEY (answer_id) REFERENCES answer(id),
   FOREIGN KEY (question_id) REFERENCES question(id),
   FOREIGN KEY (active_game_id) REFERENCES active_game(id)
);





insert into question_difficulty(description)
values('LOW'), ('MEDIUM'), ('HARD');

insert into question(description, question_difficulty_id)values
  ('Οχήματα με ρόδες', 1),
  ('Site κοινωνικής δικτύωσης', 1),
  ('Γλώσσες προγραμματισμού ', 1),

  ('Οι μεγαλύτεροι έλληνες ηθοποιοί των 80s', 2),
#   ('Φράσεις απο παιδικά', 2),
   ('Γνωστά επιτραπέζια', 2),

('Φράσεις από διαφημίσεις', 3);
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
  (3, 'Scala', 3),

(5, 'Scrabble|σκράμπλ', 1),
(5, 'Monopoly|μονόπολη', 1),
(5, 'Dixit|ντίξιτ', 1),
(5, 'Tichu|τίτσου', 4);


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


#   (5, 'answer 1', 1),
#   (5, 'answer 2', 2),
#   (5, 'answer 3', 1),
#   (5, 'answer 4', 2),
#   (5, 'answer 5', 2),
#
#   (6, 'answer 1', 1),
#   (6, 'answer 2', 2),
#   (6, 'answer 3', 1),
#   (6, 'answer 4', 2),
#   (6, 'answer 5', 2),
#
#   (7, 'answer 1', 1),
#   (7, 'answer 2', 2),
#   (7, 'answer 3', 1),
#   (7, 'answer 4', 2),
#   (7, 'answer 5', 2),
#
#   (8, 'answer 1', 1),
#   (8, 'answer 2', 2),
#   (8, 'answer 3', 1),
#   (8, 'answer 4', 2),
#   (8, 'answer 5', 2),
#
#   (9, 'answer 1', 1),
#   (9, 'answer 2', 2),
#   (9, 'answer 3', 1),
#   (9, 'answer 4', 2),
#   (9, 'answer 5', 2),
#
#   (10, 'answer 1', 1),
#   (10, 'answer 2', 2),
#   (10, 'answer 3', 1),
#   (10, 'answer 4', 2),
#   (10, 'answer 5', 2)
;


SELECT  * from answer;
SELECT  * from question;
#SELECT  * from active_game;
SELECT  * from active_game_answer;
#SELECT  * from active_game_question;
#SELECT  * from active_game_player;