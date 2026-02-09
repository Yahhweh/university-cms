package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.excpetion.EntityNotFoundException;
import placeholder.organisation.unicms.repository.*;
import placeholder.organisation.unicms.service.dto.LessonDTO;
import placeholder.organisation.unicms.service.mapper.LessonMapper;
import placeholder.organisation.unicms.service.validation.LessonValidation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Service
@Log4j2
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final LessonValidation lessonValidation;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper, LessonValidation lessonValidation) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
        this.lessonValidation = lessonValidation;
    }

    public List<Lesson> findAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        log.debug("Found {} lessons", lessons.size());
        return lessons;
    }

    public Optional<Lesson> findLesson(long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        lesson.ifPresent(l -> log.debug("Found lesson: {} with id: {}", l.getStudySubject(), id));
        return lesson;
    }

    @Transactional
    public void createLesson(Lesson lesson) {
        lessonValidation.validateLesson(lesson);
        lessonRepository.save(lesson);
        log.info("Lesson saved successfully: {}", lesson.getStudySubject());
    }

    public List<Lesson> findLessonsInRange(LocalDate startDate, LocalDate endDate, long personId, PersonType type) {
        return lessonRepository.findInRange(startDate, endDate, personId, type);
    }

    public List<Lesson> findByDate(LocalDate date, long personId, PersonType type) {
        return lessonRepository.findByDateAndRole(date, personId, type);
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
        lessonRepository.save(lesson);

        log.debug("Lesson updated successfully. ID: {}", lessonId);
    }
}