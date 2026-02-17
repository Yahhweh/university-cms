INSERT INTO address (id, city, street, country, phone_number, house_number)
VALUES (1, 'Vilnius', 'Sauletekio al.', 'Lithuania', '+37060000000', '11');

INSERT INTO person (id, password, name, sure_name, gender, address_id, date_of_birth, email)
VALUES (1,'bobo', 'John', 'Doe', 'Male', 1, '2005-01-01', 'doe@gmail.com');

INSERT INTO person (id, password, name, sure_name, gender, address_id, date_of_birth, email)
VALUES (2,'bobo', 'Darius', 'Zabuluonis', 'Male', 1, '1980-01-01', 'darius@gmail.com');

INSERT INTO person (id, password, name, sure_name, gender, address_id, date_of_birth, email)
VALUES (3,'bobo', 'Bilbo', 'Begins', 'Male', 1, '2005-01-01', 'begins@gmail.com');


INSERT INTO lecturer (id, salary) VALUES (2, 3000.00);

INSERT INTO "group" (id, name) VALUES (1,'ITfu-25/1');

INSERT INTO "group" (id, name) VALUES (2,'ITfu-25/2');

INSERT INTO student (id, group_id, degree) VALUES (1,1,  'Bachelor');

INSERT INTO student (id, group_id, degree) VALUES (3,2,  'Bachelor');

INSERT INTO duration (id,start, "end") VALUES (1,'08:30:00', '10:05:00');
INSERT INTO duration (id,start, "end") VALUES (2,'10:30:00', '12:05:00');

INSERT INTO study_subject (id, name) VALUES (1, 'Java Programming');
INSERT INTO study_subject (id, name) VALUES (2, 'Math');
INSERT INTO study_subject (id, name) VALUES (3, 'Information Technologies');

INSERT INTO class_room_type (id, name, capacity) VALUES (1,'Lecture Hall', 100);
INSERT INTO class_room (id, room, class_room_type_id) VALUES (1,'SRL-I 412', 1);
INSERT INTO class_room (id, room, class_room_type_id) VALUES (2,'SRL-I 413', 1);
INSERT INTO lesson (
    id,
    duration_id,
    study_subject_id,
    group_id,
    lecturer_id,
    class_room_id,
    date
) VALUES (1,1, 1, 1, 2, 1, '2026-01-17');

INSERT INTO lesson (
    id,
    duration_id,
    study_subject_id,
    group_id,
    lecturer_id,
    class_room_id,
    date
) VALUES (2,2, 2, 2, 2, 2, '2026-01-17');

INSERT INTO lesson (
    id,
    duration_id,
    study_subject_id,
    group_id,
    lecturer_id,
    class_room_id,
    date
) VALUES (3,1, 3, 1, 2, 1, '2026-01-18');
