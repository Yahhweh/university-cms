package placeholder.organisation.unicms.service.validation;

import org.springframework.stereotype.Component;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.service.ServiceException;

import java.time.LocalTime;
import java.util.List;

@Component
public class LessonValidation {
    LessonRepository lessonRepository;
    GroupRepository groupRepository;
    ClassRoomRepository classRoomRepository;

    public void validateLesson(Lesson newLesson){
        if(!isLecturerDoesntHasLecturesInTheSameTime(newLesson)) throw new ServiceException("Contradiction in lectures time");
        if(!isRoomIsFree(newLesson)) throw new ServiceException("classroom is not available for this time");
        if(!isGroupSizeSmallerThenClassRoomTypeCapacity(newLesson)) throw new ServiceException("Group size is greater than room capacity");
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
        List<Student> group =groupRepository.findStudentsByGroupId(newLesson.getGroup().getId());

        if(group.size() <= classRoomType.getCapacity()) return true;
        else return false;
    }

    private boolean isRoomIsFree(Lesson newLesson){
        ClassRoom classRoom = newLesson.getClassRoom();
        List<ClassRoom> classRooms = lessonRepository.findFreeClassRooms(
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