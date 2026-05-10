INSERT INTO study_subject (id, name) VALUES (1, 'Java');
INSERT INTO study_subject (id, name) VALUES (2, 'Math');

INSERT INTO course (id, name) VALUES (1, 'Programming');
INSERT INTO course (id, name) VALUES (2, 'Physics');

INSERT INTO course_study_subject (course_id, study_subject_id) VALUES (1, 1);
INSERT INTO course_study_subject (course_id, study_subject_id) VALUES (2, 2);

INSERT INTO address (id, city, street, country, phone_number, house_number)
VALUES (1, 'Vilnius', 'Sauletekio al.', 'Lithuania', '+37060000000', '11');

INSERT INTO "user" (id, password, name, sure_name, gender, address_id, date_of_birth, email, dtype)
VALUES (1, 'password', 'Admin', 'User', 'Male', 1, '1980-01-01', 'admin@university.com', 'User');

INSERT INTO "group" (id, name, course, mentor_id, info) VALUES (1, 'Group-A', 1, 1, 'Group info');
INSERT INTO "group" (id, name, course, mentor_id, info) VALUES (2, 'Group-B', 2, 1, 'Group info');