package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.*;
import placeholder.organisation.unicms.entity.*;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LessonService {

    LessonJpa lessonJpa;
    DurationJpa durationJpa;
    ClassRoomJpa classRoomJpa;
    GroupJpa groupJpa;
    StudySubjectJpa studySubjectJpa;
    LecturerJpa lecturerJpa;

    public LessonService(LessonJpa lessonJpa, DurationJpa durationJpa, ClassRoomJpa classRoomJpa, GroupJpa groupJpa, StudySubjectJpa studySubjectJpa, LecturerJpa lecturerJpa) {
        this.lessonJpa = lessonJpa;
        this.durationJpa = durationJpa;
        this.classRoomJpa = classRoomJpa;
        this.groupJpa = groupJpa;
        this.studySubjectJpa = studySubjectJpa;
        this.lecturerJpa = lecturerJpa;
    }

    List<Lesson> findAllLessons() {
        try {
            List<Lesson> lessons = lessonJpa.findAll();
            log.debug("found {} lessons", lessons.size());
            return lessons;
        } catch (RuntimeException e) {
            log.error("Database error while fetching lessons", e);
            throw new ServiceException("Error retrieving lessons", e);
        }
    }

    Optional<Lesson> findLesson(long id) {
        try {
            Optional<Lesson> lesson = lessonJpa.findById(id);
            if (lesson.isPresent()) {
                log.debug("Found {} lesson with id {}", lesson.get().getStudySubject(), lesson.get().getId());
                return lesson;
            } else {
                log.debug("Lesson with id {} not found", id);
                return Optional.empty();
            }
        } catch (RuntimeException e) {
            log.error("Database error while searching for lesson id: {}", id, e);
            throw new ServiceException("Error finding lesson", e);
        }
    }

    @Transactional
    void addLesson(Lesson lesson) {
        if (lesson == null) {
            log.error("Attempt to save a null lesson object");
            throw new IllegalArgumentException("Lesson cannot be null");
        }
        lessonJpa.save(lesson);
        log.info("Lesson saved successfully. Name: {}", lesson.getStudySubject());
    }

    @Transactional
    public void changeDuration(Long lessonId, Long durationId) {
        Lesson lesson = findLesson(lessonId);
        Duration dbDuration = durationJpa.findById(durationId)
                .orElseThrow(() -> new IllegalArgumentException("Duration not found: " + durationId));

        if (!dbDuration.equals(lesson.getDuration())) {
            lesson.setDuration(dbDuration);
            log.info("Updated Lesson {} -> Duration {}", lessonId, durationId);
        }
    }

    @Transactional
    public void changeClassroom(Long lessonId, Long classroomId) {
        Lesson lesson = findLesson(lessonId);
        ClassRoom dbClassroom = classRoomJpa.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found: " + classroomId));

        if (!dbClassroom.equals(lesson.getClassRoom())) {
            lesson.setClassRoom(dbClassroom);
            log.info("Updated Lesson {} -> Classroom {}", lessonId, classroomId);
        }
    }

    @Transactional
    public void changeGroup(Long lessonId, Long groupId) {
        Lesson lesson = findLesson(lessonId);
        Group dbGroup = groupJpa.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupId));

        if (!dbGroup.equals(lesson.getGroup())) {
            lesson.setGroup(dbGroup);
            log.info("Updated Lesson {} -> Group {}", lessonId, groupId);
        }
    }

    @Transactional
    public void changeStudySubject(Long lessonId, Long subjectId) {
        Lesson lesson = findLesson(lessonId);
        StudySubject dbSubject = studySubjectJpa.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + subjectId));

        if (!dbSubject.equals(lesson.getStudySubject())) {
            lesson.setStudySubject(dbSubject);
            log.info("Updated Lesson {} -> Subject {}", lessonId, subjectId);
        }
    }

    @Transactional
    public void changeLecturer(Long lessonId, Long lecturerId) {
        Lesson lesson = findLesson(lessonId);
        Lecturer dbLecturer = lecturerJpa.findById(lecturerId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer not found: " + lecturerId));

        if (!dbLecturer.equals(lesson.getLecturer())) {
            lesson.setLecturer(dbLecturer);
            log.info("Updated Lesson {} -> Lecturer {}", lessonId, lecturerId);
        }
    }

    private Lesson findLesson(Long id) {
        return lessonJpa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found: " + id));
    }
}