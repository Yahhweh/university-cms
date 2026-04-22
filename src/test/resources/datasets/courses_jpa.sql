INSERT INTO address (id, city, street, country, phone_number, house_number)
VALUES (1, 'Vilnius', 'Sauletekio al.', 'Lithuania', '+37060000000', '11');

INSERT INTO "user" (id, password, name, sure_name, gender, address_id,
                    date_of_birth, email, dtype)
VALUES (1, 'bobo', 'Darius', 'Zabuluonis', 'Male', 1, '1980-01-01',
        'darius.zabuluonis@lecturer.university.com', 'Lecturer');

INSERT INTO user_roles (user_id, role)
VALUES (1, 'LECTURER');

INSERT INTO lecturer (id, salary)
VALUES (1, 3000.00);

INSERT INTO study_subject (id, name)
VALUES (1, 'Java');
INSERT INTO study_subject (id, name)
VALUES (2, 'Math');

INSERT INTO lecturer_study_subject (lecturer_id, study_subject_id)
VALUES (1, 1);

INSERT INTO course (id, name)
VALUES (1, 'Programming');
INSERT INTO course (id, name)
VALUES (2, 'Physics');

INSERT INTO course_study_subject (course_id, study_subject_id)
VALUES (1, 1);
INSERT INTO course_study_subject (course_id, study_subject_id)
VALUES (2, 2);
