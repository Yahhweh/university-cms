package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.*;
import placeholder.organisation.unicms.entity.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LessonService {

    LessonDao lessonDao;
    DurationDao durationDao;
    ClassRoomDao classRoomDao;
    GroupDao groupDao;
    StudySubjectDao studySubjectDao;
    LecturerDao lecturerDao;
    StudentDao studentDao;

    public LessonService(LessonDao lessonDao, StudentDao studentDao, DurationDao durationDao, ClassRoomDao classRoomDao, GroupDao groupDao, StudySubjectDao studySubjectDao, LecturerDao lecturerDao) {
        this.lessonDao = lessonDao;
        this.durationDao = durationDao;
        this.classRoomDao = classRoomDao;
        this.groupDao = groupDao;
        this.studySubjectDao = studySubjectDao;
        this.lecturerDao = lecturerDao;
        this.studentDao = studentDao;
    }

    public List<Lesson> findAllLessons() {
        try {
            List<Lesson> lessons = lessonDao.findAll();
            log.debug("found {} lessons", lessons.size());
            return lessons;
        } catch (RuntimeException e) {
            log.error("Database error while fetching lessons", e);
            throw new ServiceException("Error retrieving lessons", e);
        }
    }

    public Optional<Lesson> findLesson(long id) {
        try {
            Optional<Lesson> lesson = lessonDao.findById(id);
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
    public void addLesson(Lesson lesson) {
        if (lesson == null) {
            log.error("Attempt to save a null lesson object");
            throw new IllegalArgumentException("Lesson cannot be null");
        }
        lessonDao.save(lesson);
        log.info("Lesson saved successfully. Name: {}", lesson.getStudySubject());
    }

    @Transactional
    public void changeDuration(Long lessonId, Long durationId) {
        Lesson lesson = findLesson(lessonId);
        Duration dbDuration = durationDao.findById(durationId)
                .orElseThrow(() -> new IllegalArgumentException("Duration not found: " + durationId));

        if (!dbDuration.equals(lesson.getDuration())) {
            lesson.setDuration(dbDuration);
            log.info("Updated Lesson {} -> Duration {}", lessonId, durationId);
        }
    }

    @Transactional
    public void changeClassroom(Long lessonId, Long classroomId) {
        Lesson lesson = findLesson(lessonId);
        ClassRoom dbClassroom = classRoomDao.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found: " + classroomId));

        if (!dbClassroom.equals(lesson.getClassRoom())) {
            lesson.setClassRoom(dbClassroom);
            log.info("Updated Lesson {} -> Classroom {}", lessonId, classroomId);
        }
    }

    @Transactional
    public void changeGroup(Long lessonId, Long groupId) {
        Lesson lesson = findLesson(lessonId);
        Group dbGroup = groupDao.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupId));

        if (!dbGroup.equals(lesson.getGroup())) {
            lesson.setGroup(dbGroup);
            log.info("Updated Lesson {} -> Group {}", lessonId, groupId);
        }
    }

    @Transactional
    public void changeStudySubject(Long lessonId, Long subjectId) {
        Lesson lesson = findLesson(lessonId);
        StudySubject dbSubject = studySubjectDao.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + subjectId));

        if (!dbSubject.equals(lesson.getStudySubject())) {
            lesson.setStudySubject(dbSubject);
            log.info("Updated Lesson {} -> Subject {}", lessonId, subjectId);
        }
    }

    @Transactional
    public void changeLecturer(Long lessonId, Long lecturerId) {
        Lesson lesson = findLesson(lessonId);
        Lecturer dbLecturer = lecturerDao.findById(lecturerId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer not found: " + lecturerId));

        if (!dbLecturer.equals(lesson.getLecturer())) {
            lesson.setLecturer(dbLecturer);
            log.info("Updated Lesson {} -> Lecturer {}", lessonId, lecturerId);
        }
    }

    public Lesson findLesson(Long id) {
        return lessonDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found: " + id));
    }

    public List<Lesson> findLessonsInRange(LocalDate startDate, LocalDate endDate, long personId, PersonType type) {
        List<Lesson> lessons = lessonDao.findInRange(startDate, endDate, personId, type);
        log.debug("Using range filter {} lessons found", lessons.size());
        return lessons;
    }

    public List<Lesson> findByDate(LocalDate date, long personId, PersonType type) {
        List<Lesson> lessons = lessonDao.findByDateAndRole(date, personId, type);
        log.debug("Using find by date filter {} lessons found", lessons.size());
        return lessons;
    }

}