package placeholder.organisation.unicms.service.util;

import placeholder.organisation.unicms.entity.Lesson;

import java.time.DayOfWeek;
import java.util.*;

public class ScheduleUtil {

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

}
