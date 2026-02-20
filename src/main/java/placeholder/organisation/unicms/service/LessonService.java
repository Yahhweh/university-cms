package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.*;
import placeholder.organisation.unicms.service.dto.LessonDTO;
import placeholder.organisation.unicms.service.mapper.LessonMapper;
import placeholder.organisation.unicms.service.validation.LessonValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final LessonValidator lessonValidator;
    private final StudentRepository studentRepository;
    private final LecturerRepository lecturerRepository;
    private final DurationRepository durationRepository;
    private final SubjectRepository subjectRepository;
    private final GroupRepository groupRepository;
    private final RoomRepository roomRepository;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper,
                         LessonValidator lessonValidator, StudentRepository studentRepository,
                         LecturerRepository lecturerRepository, DurationRepository durationRepository,
                         SubjectRepository subjectRepository, GroupRepository groupRepository,
                         RoomRepository roomRepository) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.lessonValidator = lessonValidator;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
        this.durationRepository = durationRepository;
        this.subjectRepository = subjectRepository;
        this.groupRepository = groupRepository;
        this.roomRepository = roomRepository;
    }

    public List<Lesson> findAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        log.debug("Found {} lessons", lessons.size());
        return lessons;
    }

    public Optional<Lesson> findLesson(long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        lesson.ifPresent(l -> log.debug("Found lesson: {} with id: {}", l.getSubject(), id));
        return lesson;
    }

    @Transactional
    public void createLesson(Lesson lesson) {
        lessonValidator.validateLesson(lesson, -1L);
        lessonRepository.save(lesson);
        log.info("Lesson saved successfully: {}", lesson.getSubject());
    }

    public List<Lesson> findLessonsInRange(LocalDate startDate, LocalDate endDate, long personId) {
        if (studentRepository.existsById(personId)) {
            return lessonRepository.findInRangeForStudent(startDate, endDate, personId);
        } else if (lecturerRepository.existsById(personId)) {
            return lessonRepository.findInRangeForLecturer(startDate, endDate, personId);
        }
        throw new IllegalArgumentException("Person with id " + personId + " is neither student nor lecturer");
    }

    public List<Lesson> findByDate(LocalDate date, long personId) {
        if (studentRepository.existsById(personId)) {
            return lessonRepository.findByDateForStudent(date, personId);
        } else if (lecturerRepository.existsById(personId)) {
            return lessonRepository.findByDateAndLecturerId(date, personId);
        }
        throw new IllegalArgumentException("Person with id " + personId + " is neither student nor lecturer");
    }

    @Transactional
    public void removeLesson(long lessonId) {
        if (!lessonRepository.existsById(lessonId)) {
            throw new EntityNotFoundException(Lesson.class, String.valueOf(lessonId));
        }
        lessonRepository.deleteById(lessonId);
    }

    @Transactional
    public void updateLesson(long lessonId, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException(Lesson.class, String.valueOf(lessonId)));

        lessonMapper.updateEntityFromDto(lessonDTO, lesson);

        resolveRelations(lessonDTO, lesson);

        lessonValidator.validateLesson(lesson, lessonId);

        lessonRepository.save(lesson);

        log.debug("Lesson updated successfully. ID: {}", lessonId);
    }

    private void resolveRelations(LessonDTO dto, Lesson lesson) {
        if (dto.getDurationId() != null) {
            lesson.setDuration(durationRepository.findById(dto.getDurationId())
                    .orElse(lesson.getDuration()));
        }
        if (dto.getStudySubjectId() != null) {
            lesson.setSubject(subjectRepository.getReferenceById(dto.getStudySubjectId()));
        }
        if (dto.getGroupId() != null) {
            lesson.setGroup(groupRepository.getReferenceById(dto.getGroupId()));
        }

        if (dto.getLecturerId() != null) {
            lesson.setLecturer(lecturerRepository.getReferenceById(dto.getLecturerId()));
        }

        if (dto.getClassRoomId() != null) {
            lesson.setRoom(roomRepository.getReferenceById(dto.getClassRoomId()));
        }
    }
}