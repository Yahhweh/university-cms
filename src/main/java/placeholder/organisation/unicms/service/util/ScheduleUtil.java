package placeholder.organisation.unicms.service.util;

import placeholder.organisation.unicms.entity.DayOfWeek;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Schedule;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

public class ScheduleUtil {

    public static EnumMap<DayOfWeek, List<Schedule>> groupSchedulesByDay(List<Schedule> schedules) {
        EnumMap<DayOfWeek, List<Schedule>> schedulesByDay = new EnumMap<>(DayOfWeek.class);
        for (DayOfWeek day : DayOfWeek.values()) {
            schedulesByDay.put(day, new ArrayList<>());
        }
        for (Schedule s : schedules) {
            schedulesByDay.get(s.getDay()).add(s);
        }
        return schedulesByDay;
    }

    public static Map<java.time.DayOfWeek, List<Lesson>> groupLessonsByDay(List<Lesson> lessons) {
        return lessons.stream()
            .sorted(Comparator.comparing(Lesson::getDate)
                .thenComparing(l -> l.getDuration().getId()))
            .collect(Collectors.groupingBy(
                l -> l.getDate().getDayOfWeek(),
                LinkedHashMap::new,
                Collectors.toList()
            ));
    }

    public static LocalDate[] resolveDateRange(String period) {
        LocalDate today = LocalDate.now();
        if ("month".equals(period)) {
            return new LocalDate[]{
                today.with(TemporalAdjusters.firstDayOfMonth()),
                today.with(TemporalAdjusters.lastDayOfMonth())
            };
        }
        return new LocalDate[]{
            today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)),
            today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.FRIDAY))
        };
    }
}
