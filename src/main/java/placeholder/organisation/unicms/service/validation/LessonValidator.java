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
    private GroupRepository groupRepository;
    private ClassRoomRepository classRoomRepository;
    private StudentRepository studentRepository;

    public void validateLesson(Lesson newLesson) {
        if (!isLecturerDoesntHasLecturesInTheSameTime(newLesson)) {
            throw new EntityValidationException("Contradiction in lectures time", "Lesson", String.valueOf(newLesson.getId()));
        }
        if (!isRoomIsFree(newLesson)) {
            throw new EntityValidationException("classroom is not available for this time", "Lesson", String.valueOf(newLesson.getId()));
        }
        if (!isGroupSizeSmallerThenClassRoomTypeCapacity(newLesson)) {
            throw new EntityValidationException("Group size is greater than room capacity", "Lesson", String.valueOf(newLesson.getId()));
        }
    }

    public boolean isLecturerDoesntHasLecturesInTheSameTime(Lesson newLesson) {
        List<Lesson> existingLessons = lessonRepository
                .findConflictionLessonsForLecturer(newLesson.getLecturer().getId(),
                        newLesson.getDate(),
                        newLesson.getDuration().getStart(),
                        newLesson.getDuration().getEnd());

        return existingLessons.isEmpty();
    }

    private boolean isGroupSizeSmallerThenClassRoomTypeCapacity(Lesson newLesson) {
        ClassRoomType classRoomType = newLesson.getClassRoom().getClassRoomType();
        List<Student> group = studentRepository.findStudentsByGroup(newLesson.getGroup());

        return group.size() <= classRoomType.getCapacity();
    }

    private boolean isRoomIsFree(Lesson newLesson) {
        List<ClassRoom> classRooms = classRoomRepository.isClassRoomFree(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd());

        return classRooms.isEmpty();
    }
}