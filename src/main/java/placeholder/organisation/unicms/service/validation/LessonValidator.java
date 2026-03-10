package placeholder.organisation.unicms.service.validation;

import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.EntityValidationException;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.repository.StudentRepository;

import java.time.DayOfWeek;

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

    public void validateLesson(Lesson lesson, long lessonId) {

        if(!isLectureOnWorkingDays(lesson)){
            throw new EntityValidationException("Lecture on weekends.", Lesson.class, String.valueOf(lessonId));
        }
        if (hasLecturerTimeConflict(lesson)) {
            throw new EntityValidationException("Lecturer has another lesson at this time", Lesson.class, String.valueOf(lessonId));
        }
        if (hasRoomTimeConflict(lesson)) {
            throw new EntityValidationException("Classroom is occupied", Lesson.class, String.valueOf(lessonId));
        }
        if (hasGroupTimeConflict(lesson)) {
            throw new EntityValidationException("Group has another lesson at this time", Lesson.class, String.valueOf(lessonId));
        }
        if (!isLecturerAuthorized(lesson)) {
            throw new EntityValidationException("Lecturer is not authorized to teach this subject", Lesson.class, String.valueOf(lessonId));
        }
        if (!isCapacitySufficient(lesson)) {
            throw new EntityValidationException("Group size exceeds room capacity", Lesson.class, String.valueOf(lessonId));
        }
    }

    private boolean hasLecturerTimeConflict(Lesson lesson) {
        return lessonRepository.findConflictionLessonsForLecturer(
                lesson.getLecturer().getId(),
                lesson.getDate(),
                lesson.getDuration().getStart(),
                lesson.getDuration().getEnd(),
                lesson.getId());
    }

    private boolean hasRoomTimeConflict(Lesson lesson) {
        return lessonRepository.findRoomConflictsInTime(
                lesson.getDate(),
                lesson.getDuration().getStart(),
                lesson.getDuration().getEnd(),
                lesson.getRoom().getId(),
                lesson.getId());
    }

    private boolean hasGroupTimeConflict(Lesson lesson) {
        return lessonRepository.findGroupConflictInTime(
                lesson.getGroup().getId(),
                lesson.getDate(),
                lesson.getDuration().getStart(),
                lesson.getDuration().getEnd(),
                lesson.getId());
    }

    private boolean isLecturerAuthorized(Lesson lesson) {
        return lesson.getLecturer().getSubjects().contains(lesson.getSubject());
    }

    private boolean isCapacitySufficient(Lesson lesson) {
        long capacity = lesson.getRoom().getRoomType().getCapacity();
        return studentRepository.findStudentsByGroup(groupRepository.findById(lesson.getGroup().getId()).get()).size() <= capacity;
    }

    private boolean isLectureOnWorkingDays(Lesson lesson){
        DayOfWeek day =  lesson.getDate().getDayOfWeek();
        return DayOfWeek.SATURDAY != day && DayOfWeek.SUNDAY != day;
    }
}