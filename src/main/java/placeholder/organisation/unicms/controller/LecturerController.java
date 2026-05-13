package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.util.ScheduleUtil;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/lecturers")
@PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
@AllArgsConstructor
@Validated
public class LecturerController {

    private final LecturerService lecturerService;
    private final ScheduleUtil scheduleUtil;
    private final LessonService lessonService;

    @GetMapping()
    public String getLecturers(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Lecturer> page = lecturerService.findAll(pageable);

        model.addAttribute("lecturers", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "lecturers");

        return "lecturers";
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication authentication
    ) {
        Lecturer lecturer = lecturerService.findByEmail(authentication.getName());

        model.addAttribute("lecturer", lecturer);
        return "lecturer-profile";
    }

    @PreAuthorize("hasRole('LECTURER')")
    @GetMapping("my-schedule")
    public String getLecturerSchedule(@AuthenticationPrincipal UserDetails userDetails,
                                      @RequestParam(required = false) LocalDate startDate,
                                      @RequestParam(required = false) LocalDate endDate,
                                      Model model) {

        Lecturer lecturer = lecturerService.findByEmail(userDetails.getUsername());
        LocalDate today = LocalDate.now();

        List<Lesson> lessons = lessonService.findLessonsInRange(startDate, endDate, lecturer);
        model = scheduleUtil.addAttributes(model, today, startDate, endDate, lessons);

        return "lecturer-schedule";
    }
}
