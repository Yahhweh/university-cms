INSERT INTO study_subject (id, name) VALUES (1, 'Java');
INSERT INTO study_subject (id, name) VALUES (2, 'Math');

INSERT INTO course (id, name) VALUES (1, 'Programming');
INSERT INTO course (id, name) VALUES (2, 'Physics');

INSERT INTO course_study_subject (course_id, study_subject_id) VALUES (1, 1);
INSERT INTO course_study_subject (course_id, study_subject_id) VALUES (2, 2);

INSERT INTO "group" (id, name, course) VALUES (1, 'Group-A', 1);
INSERT INTO "group" (id, name, course) VALUES (2, 'Group-B', 2);