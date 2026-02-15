package placeholder.organisation.unicms.service.validation;

import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.EntityValidationException;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.repository.StudentRepository;

import java.time.LocalTime;
import java.util.List;
@Component
public class LessonValidator {
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public LessonValidator(LessonRepository lessonRepository, StudentRepository studentRepository, GroupRepository groupRepository) {
        this.lessonRepository = lessonRepository;
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    public void validateLesson(Lesson lesson) {
        Long lessonId = lesson.getId();

        if (!lesson.getDuration().getStart().isBefore(lesson.getDuration().getEnd())) {
            throw new EntityValidationException("Start time must be before end time", Lesson.class, String.valueOf(lessonId));
        }

        if (isLecturerConflict(lesson, lessonId)) {
            throw new EntityValidationException("Lecturer has another lesson at this time", Lesson.class, String.valueOf(lessonId));
        }
        if (isRoomConflict(lesson, lessonId)) {
            throw new EntityValidationException("Classroom is occupied", Lesson.class, String.valueOf(lessonId));
        }
        if (isGroupConflict(lesson, lessonId)) {
            throw new EntityValidationException("Group has another lesson at this time", Lesson.class, String.valueOf(lessonId));
        }

        if (!isLecturerAuthorized(lesson)) {
            throw new EntityValidationException("Lecturer is not authorized to teach this subject", Lesson.class, String.valueOf(lessonId));
        }
        if (!isCapacitySufficient(lesson)) {
            throw new EntityValidationException("Group size exceeds room capacity", Lesson.class, String.valueOf(lessonId));
        }
    }

    private boolean isLecturerConflict(Lesson lesson, Long excludeId) {
        return lessonRepository.findConflictionLessonsForLecturer(
                lesson.getLecturer().getId(),
                lesson.getDate(),
                lesson.getDuration().getStart(),
                lesson.getDuration().getEnd(),
                excludeId);
    }

    private boolean isRoomConflict(Lesson lesson, Long excludeId) {
        return lessonRepository.findConflicts(
                lesson.getDate(),
                lesson.getDuration().getStart(),
                lesson.getDuration().getEnd(),
                lesson.getClassRoom().getId(),
                excludeId);
    }

    private boolean isGroupConflict(Lesson lesson, Long excludeId) {
        return lessonRepository.existsGroupConflict(
                lesson.getGroup().getId(),
                lesson.getDate(),
                lesson.getDuration().getStart(),
                lesson.getDuration().getEnd(),
                excludeId);
    }

    private boolean isLecturerAuthorized(Lesson lesson) {
        return lesson.getLecturer().getStudySubjects().contains(lesson.getStudySubject());
    }

    private boolean isCapacitySufficient(Lesson lesson) {
        long capacity = lesson.getClassRoom().getClassRoomType().getCapacity();
        return studentRepository.findStudentsByGroup(groupRepository.findById(lesson.getGroup().getId()).get()).size() <= capacity;
    }
}