package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final DurationRepository durationRepository;
    private final ClassRoomRepository classRoomRepository;
    private final GroupRepository groupRepository;
    private final StudySubjectRepository studySubjectRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;

    public LessonService(LessonRepository lessonRepository, StudentRepository studentRepository, DurationRepository durationRepository, ClassRoomRepository classRoomRepository, GroupRepository groupRepository, StudySubjectRepository studySubjectRepository, LecturerRepository lecturerRepository) {
        this.lessonRepository = lessonRepository;
        this.durationRepository = durationRepository;
        this.classRoomRepository = classRoomRepository;
        this.groupRepository = groupRepository;
        this.studySubjectRepository = studySubjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
    }

    public List<Lesson> findAllLessons() {
        try {
            List<Lesson> lessons = lessonRepository.findAll();
            log.debug("found {} lessons", lessons.size());
            return lessons;
        } catch (RuntimeException e) {
            log.error("Database error while fetching lessons", e);
            throw new ServiceException("Error retrieving lessons", e);
        }
    }

    public Optional<Lesson> findLesson(long id) {
        try {
            Optional<Lesson> lesson = lessonRepository.findById(id);
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
    public void createLesson(Lesson lesson) {
        if (lesson == null) {
            log.error("Attempt to save a null lesson object");
            throw new IllegalArgumentException("Lesson cannot be null");
        }
        lessonRepository.save(lesson);
        log.info("Lesson saved successfully. Name: {}", lesson.getStudySubject());
    }

    @Transactional
    public void changeDuration(Long lessonId, Long durationId) {
        Optional<Lesson> lesson = findLesson(lessonId);
        Duration dbDuration = durationRepository.findById(durationId)
                .orElseThrow(() -> new IllegalArgumentException("Duration not found: " + durationId));

        if (!dbDuration.equals(lesson.get().getDuration())) {
            lesson.get().setDuration(dbDuration);
            log.info("Updated Lesson {} -> Duration {}", lessonId, durationId);
        }
    }

    @Transactional
    public void changeClassroom(Long lessonId, Long classroomId) {
        Optional<Lesson> lesson = findLesson(lessonId);
        ClassRoom dbClassroom = classRoomRepository.findById(classroomId)
                .orElseThrow(() -> new IllegalArgumentException("Classroom not found: " + classroomId));

        if (!dbClassroom.equals(lesson.get().getClassRoom())) {
            lesson.get().setClassRoom(dbClassroom);
            log.info("Updated Lesson {} -> Classroom {}", lessonId, classroomId);
        }
    }

    @Transactional
    public void changeGroup(Long lessonId, Long groupId) {
        Optional<Lesson> lesson = findLesson(lessonId);
        Group dbGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupId));

        if (!dbGroup.equals(lesson.get().getGroup())) {
            lesson.get().setGroup(dbGroup);
            log.info("Updated Lesson {} -> Group {}", lessonId, groupId);
        }
    }

    @Transactional
    public void changeStudySubject(Long lessonId, Long subjectId) {
        Optional<Lesson> lesson = findLesson(lessonId);
        StudySubject dbSubject = studySubjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found: " + subjectId));

        if (!dbSubject.equals(lesson.get().getStudySubject())) {
            lesson.get().setStudySubject(dbSubject);
            log.info("Updated Lesson {} -> Subject {}", lessonId, subjectId);
        }
    }

    @Transactional
    public void changeLecturer(Long lessonId, Long lecturerId) {
        Optional<Lesson> lesson = findLesson(lessonId);
        Lecturer dbLecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new IllegalArgumentException("Lecturer not found: " + lecturerId));

        if (!dbLecturer.equals(lesson.get().getLecturer())) {
            lesson.get().setLecturer(dbLecturer);
            log.info("Updated Lesson {} -> Lecturer {}", lessonId, lecturerId);
        }
    }


    public List<Lesson> findLessonsInRange(LocalDate startDate, LocalDate endDate, long personId, PersonType type) {
        List<Lesson> lessons = lessonRepository.findInRange(startDate, endDate, personId, type);
        log.debug("Using range filter {} lessons found", lessons.size());
        return lessons;
    }

    public List<Lesson> findByDate(LocalDate date, long personId, PersonType type) {
        List<Lesson> lessons = lessonRepository.findByDateAndRole(date, personId, type);
        log.debug("Using find by date filter {} lessons found", lessons.size());
        return lessons;
    }

    @Transactional
    public void removeLesson(long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new ServiceException("Lesson not found with id: " + lessonId);
        }
        lessonRepository.deleteById(lessonId);
    }

}