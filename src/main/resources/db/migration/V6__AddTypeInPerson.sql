CREATE TYPE person_type AS ENUM ('Student', 'Lecturer');

ALTER TABLE person
ADD type person_type DEFAULT 'Student';