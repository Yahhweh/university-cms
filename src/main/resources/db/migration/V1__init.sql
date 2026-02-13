DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'gender_type') THEN
            CREATE TYPE gender_type AS ENUM ('Male', 'Female', 'Other');
        END IF;
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'degree_type') THEN
            CREATE TYPE degree_type AS ENUM ('Bachelor', 'Master', 'Doctoral');
        END IF;
    END $$;


CREATE TABLE IF NOT EXISTS address (
                                       id SERIAL PRIMARY KEY,
                                       city VARCHAR(255) NOT NULL,
                                       street VARCHAR(255) NOT NULL,
                                       country VARCHAR(255) NOT NULL,
                                       phone_number VARCHAR(50) NOT NULL,
                                       house_number VARCHAR(20) NOT NULL,
                                       postal_code VARCHAR(20) NOT NULL DEFAULT '0000'
);

CREATE TABLE IF NOT EXISTS "group" (
                                       id SERIAL PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS duration (
                                        id SERIAL PRIMARY KEY,
                                        start TIME NOT NULL,
                                        "end" TIME NOT NULL
);

CREATE TABLE IF NOT EXISTS class_room_type (
                                               id SERIAL PRIMARY KEY,
                                               name VARCHAR(255) NOT NULL,
                                               capacity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS study_subject (
                                             id SERIAL PRIMARY KEY,
                                             name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS person (
                                      id SERIAL PRIMARY KEY,
                                      password VARCHAR(255) NOT NULL,
                                      name VARCHAR(255) NOT NULL,
                                      sure_name VARCHAR(255) NOT NULL,
                                      gender gender_type NOT NULL,
                                      address_id INTEGER NOT NULL,
                                      date_of_birth DATE NOT NULL,
                                      email VARCHAR(255) NOT NULL UNIQUE,
                                      CONSTRAINT fk_person_address FOREIGN KEY (address_id) REFERENCES address(id)
);

CREATE TABLE IF NOT EXISTS class_room (
                                          id SERIAL PRIMARY KEY,
                                          room VARCHAR(50) NOT NULL,
                                          class_room_type_id INTEGER NOT NULL,
                                          CONSTRAINT fk_classroom_type FOREIGN KEY (class_room_type_id) REFERENCES class_room_type(id)
);

CREATE TABLE IF NOT EXISTS student (
                                       id INTEGER PRIMARY KEY,
                                       group_id INTEGER,
                                       degree_type_attr degree_type,
                                       CONSTRAINT fk_student_person FOREIGN KEY (id) REFERENCES person(id),
                                       CONSTRAINT fk_student_group FOREIGN KEY (group_id) REFERENCES "group"(id)
);

CREATE TABLE IF NOT EXISTS lecturer (
                                        id INTEGER PRIMARY KEY,
                                        salary INTEGER NOT NULL DEFAULT 0,
                                        CONSTRAINT fk_lecturer_person FOREIGN KEY (id) REFERENCES person(id)
);

CREATE TABLE IF NOT EXISTS lesson (
                                      id SERIAL PRIMARY KEY,
                                      duration_id INTEGER NOT NULL,
                                      study_subject_id INTEGER NOT NULL,
                                      group_id INTEGER NOT NULL,
                                      lecturer_id INTEGER NOT NULL,
                                      class_room_id INTEGER NOT NULL,
                                      date DATE NOT NULL DEFAULT '1900-01-01',
                                      CONSTRAINT fk_lesson_duration FOREIGN KEY (duration_id) REFERENCES duration(id),
                                      CONSTRAINT fk_lesson_subject FOREIGN KEY (study_subject_id) REFERENCES study_subject(id),
                                      CONSTRAINT fk_lesson_group FOREIGN KEY (group_id) REFERENCES "group"(id),
                                      CONSTRAINT fk_lesson_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturer(id),
                                      CONSTRAINT fk_lesson_classroom FOREIGN KEY (class_room_id) REFERENCES class_room(id)
);

CREATE TABLE IF NOT EXISTS lesson_duration (
                                               duration_id INTEGER NOT NULL,
                                               lesson_id INTEGER NOT NULL,
                                               PRIMARY KEY (duration_id, lesson_id),
                                               CONSTRAINT fk_ld_duration FOREIGN KEY (duration_id) REFERENCES duration(id),
                                               CONSTRAINT fk_ld_lesson FOREIGN KEY (lesson_id) REFERENCES lesson(id)
);

CREATE TABLE IF NOT EXISTS lecturer_study_subject (
                                                      lecturer_id INTEGER NOT NULL,
                                                      study_subject_id INTEGER NOT NULL,
                                                      PRIMARY KEY (lecturer_id, study_subject_id),
                                                      CONSTRAINT fk_lss_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturer(id),
                                                      CONSTRAINT fk_lss_subject FOREIGN KEY (study_subject_id) REFERENCES study_subject(id)
);