package placeholder.organisation.unicms.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.StudentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("my-schedule")
public class ScheduleController {

    private final LessonService lessonService;
    private final StudentService studentService;
    private final LecturerService lecturerService;

    public ScheduleController(LessonService lessonService, StudentService studentService, LecturerService lecturerService) {
        this.lessonService = lessonService;
        this.studentService = studentService;
        this.lecturerService = lecturerService;
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public String getStudentSchedule(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Student student = studentService.findByEmail(userDetails.getUsername());

        if (weekStart == null) {
            weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        }
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Lesson> lessons = lessonService.findLessonsInRange(weekStart, weekEnd, student.getId());

        model.addAttribute("lessonsByDay", groupByDay(weekStart, lessons));
        model.addAttribute("weekStart", weekStart);
        model.addAttribute("weekEnd", weekEnd);
        model.addAttribute("prevWeek", weekStart.minusWeeks(1));
        model.addAttribute("nextWeek", weekStart.plusWeeks(1));
        return "student-schedule";
    }

    @GetMapping("/lecturer")
    @PreAuthorize("hasRole('LECTURER')")
    public String getLecturerSchedule(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Lecturer lecturer = lecturerService.findByEmail(userDetails.getUsername());

        if (weekStart == null) {
            weekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        }
        LocalDate weekEnd = weekStart.plusDays(6);

        List<Lesson> lessons = lessonService.findLessonsInRange(weekStart, weekEnd, lecturer.getId());

        model.addAttribute("lessonsByDay", groupByDay(weekStart, lessons));
        model.addAttribute("weekStart", weekStart);
        model.addAttribute("weekEnd", weekEnd);
        model.addAttribute("prevWeek", weekStart.minusWeeks(1));
        model.addAttribute("nextWeek", weekStart.plusWeeks(1));
        return "lecturer-schedule";
    }

    private Map<LocalDate, List<Lesson>> groupByDay(LocalDate weekStart, List<Lesson> lessons) {
        Map<LocalDate, List<Lesson>> map = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            map.put(weekStart.plusDays(i), new ArrayList<>());
        }
        for (Lesson lesson : lessons) {
            List<Lesson> dayLessons = map.get(lesson.getDate());
            if (dayLessons != null) {
                dayLessons.add(lesson);
            }
        }
        return map;
    }
}
