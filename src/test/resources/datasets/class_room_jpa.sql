INSERT INTO address (id, city, street, country, phone_number, house_number, postal_code)
VALUES (1, '1', '1', '1', '+1', '1', '1');

INSERT INTO "group" (id, name) VALUES (1, 'A-1');

INSERT INTO study_subject (id, name) VALUES (1, 'Java');

INSERT INTO person (id, password, name, sure_name, gender, address_id, date_of_birth, email)
VALUES (1, '123', 'John', 'Doe', 'Male', 1, '1985-05-15', 'j.doe@university.edu');

INSERT INTO lecturer (id, salary) VALUES (1, 50000);

INSERT INTO class_room_type (id, name, capacity) VALUES (1, 'Lecture Hall', 100);

INSERT INTO class_room (id, room, class_room_type_id) VALUES (1, 'A-101', 1);

INSERT INTO duration (id, start, "end") VALUES (1, '08:30:00', '10:00:00');

INSERT INTO lesson (id, duration_id, study_subject_id, group_id, lecturer_id, class_room_id, date)
VALUES (1, 1, 1, 1, 1, 1, '2025-10-29');