package placeholder.organisation.unicms.service.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Lesson;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ScheduleUtilTest {

    @Test
    void groupLessonsByDay_shouldGroupLessonsByDayOfWeek_whenLessonsProvided() {
        Lesson mondayLesson = getLesson(LocalDate.of(2026, 5, 11));
        Lesson tuesdayLesson = getLesson(LocalDate.of(2026, 5, 12));

        EnumMap<DayOfWeek, List<Lesson>> result = ScheduleUtil.groupLessonsByDay(List.of(mondayLesson, tuesdayLesson));

        assertThat(result.get(DayOfWeek.MONDAY).size()).isEqualTo(1);
        assertThat(result.get(DayOfWeek.TUESDAY).size()).isEqualTo(1);
        assertThat(result.get(DayOfWeek.WEDNESDAY).size()).isEqualTo(0);
    }

    @Test
    void groupLessonsByDay_shouldReturnAllDaysWithEmptyLists_whenNoLessons() {
        EnumMap<DayOfWeek, List<Lesson>> result = ScheduleUtil.groupLessonsByDay(List.of());

        assertThat(result.size()).isEqualTo(DayOfWeek.values().length);
        for (DayOfWeek day : DayOfWeek.values()) {
            assertThat(result.get(day).size()).isEqualTo(0);
        }
    }

    private Lesson getLesson(LocalDate date) {
        Lesson lesson = new Lesson();
        lesson.setDate(date);
        return lesson;
    }
}