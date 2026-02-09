package placeholder.organisation.unicms.service.validation;

import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.excpetion.EntityValidationException;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.repository.StudentRepository;

import java.time.LocalTime;
import java.util.List;

@Component
public class LessonValidation {
    LessonRepository lessonRepository;
    GroupRepository groupRepository;
    ClassRoomRepository classRoomRepository;
    StudentRepository studentRepository;

    public void validateLesson(Lesson newLesson){
        if(!isLecturerDoesntHasLecturesInTheSameTime(newLesson)) throw new EntityValidationException("Contradiction in lectures time", "Lesson", String.valueOf(newLesson.getId()));
        if(!isRoomIsFree(newLesson)) throw new EntityValidationException("classroom is not available for this time", "Lesson", String.valueOf(newLesson.getId()));
        if(!isGroupSizeSmallerThenClassRoomTypeCapacity(newLesson)) throw new EntityValidationException("Group size is greater than room capacity", "Lesson", String.valueOf(newLesson.getId()));
    }

    private boolean isLecturerDoesntHasLecturesInTheSameTime(Lesson newLesson) {
        List<Lesson> existingLessons = lessonRepository
                .findAllLessonsRelatedToLecturer(newLesson.getLecturer().getId());

        List<Lesson> lessonsOnSameDate = existingLessons.stream()
                .filter(lesson -> lesson.getDate().equals(newLesson.getDate()))
                .toList();

        for (Lesson existingLesson : lessonsOnSameDate) {
            if (isTimeOverlap(newLesson, existingLesson)) {
                return false;
            }
        }

        return true;
    }

    private boolean isGroupSizeSmallerThenClassRoomTypeCapacity(Lesson newLesson){
        ClassRoomType classRoomType = newLesson.getClassRoom().getClassRoomType();
        List<Student> group =studentRepository.findStudentsByGroup(newLesson.getGroup());

        if(group.size() <= classRoomType.getCapacity()) return true;
        else return false;
    }

    private boolean isRoomIsFree(Lesson newLesson){
        ClassRoom classRoom = newLesson.getClassRoom();
        List<ClassRoom> classRooms = classRoomRepository.findFreeClassRooms(
                newLesson.getDate(),
                newLesson.getDuration().getStart(),
                newLesson.getDuration().getEnd());

        return classRooms.contains(classRoom);
    }

    private boolean isTimeOverlap(Lesson lesson1, Lesson lesson2) {
        LocalTime start1 = lesson1.getDuration().getStart();
        LocalTime end1 = lesson1.getDuration().getEnd();
        LocalTime start2 = lesson2.getDuration().getStart();
        LocalTime end2 = lesson2.getDuration().getEnd();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}