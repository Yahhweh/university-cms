CREATE TABLE course (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE course_study_subject (
    course_id        INTEGER REFERENCES course(id),
    study_subject_id INTEGER REFERENCES study_subject(id),
    PRIMARY KEY (course_id, study_subject_id)
);

ALTER TABLE "group"
    ADD COLUMN course INTEGER REFERENCES course(id);
