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
    private LessonRepository lessonRepository;
    private ClassRoomRepository classRoomRepository;
    private StudentRepository studentRepository;

    public void validateLesson(Lesson newLesson) {
        if (isLecturerConflict(newLesson)) {
            throw new EntityValidationException("Contradiction in lectures time", Lesson.class, String.valueOf(newLesson.getId()));
        }
        if (isRoomConflict(newLesson)) {
            throw new EntityValidationException("classroom is not available for this time", Lesson.class, String.valueOf(newLesson.getId()));
        }
        if (!isGroupSizeSmallerThanClassRoomTypeCapacity(newLesson)) {
            throw new EntityValidationException("Group size is greater than room capacity", Lesson.class, String.valueOf(newLesson.getId()));
        }
    }

    public boolean isLecturerConflict(Lesson newLesson) {
        return lessonRepository
                .findConflictionLessonsForLecturer(newLesson.getLecturer().getId(),
                        newLesson.getDate(),
                        newLesson.getDuration().getStart(),
                        newLesson.getDuration().getEnd());
    }

    private boolean isGroupSizeSmallerThanClassRoomTypeCapacity(Lesson newLesson) {
        ClassRoomType classRoomType = newLesson.getClassRoom().getClassRoomType();
        List<Student> group = studentRepository.findStudentsByGroup(newLesson.getGroup());

        return group.size() <= classRoomType.getCapacity();
    }

    private boolean isRoomConflict(Lesson newLesson) {
        return lessonRepository.findConflicts(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd(),
                newLesson.getClassRoom().getId());

    }
}