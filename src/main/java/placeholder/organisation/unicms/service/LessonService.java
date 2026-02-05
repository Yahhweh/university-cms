package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.*;
import placeholder.organisation.unicms.service.dto.LessonDTO;
import placeholder.organisation.unicms.service.mapper.LessonMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public LessonService(LessonRepository lessonRepository, LessonMapper lessonMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonMapper = lessonMapper;
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

    @Transactional
    public void updateLesson(long lessonId, LessonDTO lessonDTO) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new ServiceException("Lesson not found with id: " + lessonId));
        try {
            lessonMapper.updateEntityFromDto(lessonDTO, lesson);
            lessonRepository.save(lesson);
        } catch (Exception e) {
            log.error("Failed to map DTO to Entity for lesson id: {}", lessonId, e);
            throw new ServiceException("Error updating lesson ", e);
        }
    }
}