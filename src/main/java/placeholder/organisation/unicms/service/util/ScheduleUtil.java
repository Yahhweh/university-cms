package placeholder.organisation.unicms.service.util;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.entity.User;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Component
@AllArgsConstructor
public class ScheduleUtil {
    private final LessonService lessonService;
    private final StudentService studentService;

    public static EnumMap<DayOfWeek, List<Lesson>> groupLessonsByDay(List<Lesson> lessons) {
        EnumMap<DayOfWeek, List<Lesson>> lessonsByDay = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            lessonsByDay.put(day, new ArrayList<>());
        }
        for (Lesson s : lessons) {
            lessonsByDay.get(s.getDate().getDayOfWeek()).add(s);
        }
        return lessonsByDay;
    }

    public List<Lesson> findLessonsInRange(LocalDate startDate, LocalDate endDate, LocalDate today, User user){

        if (startDate == null) {
            startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        }

        List<Lesson> lessons = (endDate == null)
            ? lessonService.findByDate(startDate, user.getId())
            : lessonService.findLessonsInRange(startDate, endDate, user.getId());

        return lessons;
    }

    public Model addAtributes(Model model, LocalDate today, LocalDate startDate, LocalDate endDate, List<Lesson> lessons){
        LocalDate weekBegin = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
        LocalDate monthBegin = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd = today.with(TemporalAdjusters.lastDayOfMonth());

        model.addAttribute("lessonsByDay",groupLessonsByDay(lessons));
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("weekBegin", weekBegin);
        model.addAttribute("weekEnd", weekEnd);
        model.addAttribute("monthBegin", monthBegin);
        model.addAttribute("monthEnd", monthEnd);

        return model;
    }

}
