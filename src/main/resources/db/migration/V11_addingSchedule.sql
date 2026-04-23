CREATE TYPE day_of_week_type AS ENUM (
    'MONDAY',
    'TUESDAY',
    'WEDNESDAY',
    'THURSDAY',
    'FRIDAY'
    );

CREATE TABLE schedule
(
    id          BIGSERIAL PRIMARY KEY,
    day         day_of_week_type NOT NULL,
    group_id    BIGINT           NOT NULL,
    lecturer_id BIGINT           NOT NULL,
    subject_id  BIGINT           NOT NULL,
    CONSTRAINT fk_schedule_group
        FOREIGN KEY (group_id) REFERENCES "group" (id),
    CONSTRAINT fk_schedule_lecturer
        FOREIGN KEY (lecturer_id) REFERENCES lecturer (id),
    CONSTRAINT fk_schedule_subject
        FOREIGN KEY (subject_id) REFERENCES study_subject (id)
);
