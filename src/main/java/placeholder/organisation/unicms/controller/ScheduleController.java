package placeholder.organisation.unicms.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.DayOfWeek;
import placeholder.organisation.unicms.entity.Schedule;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.GenerateLessonsRequestDTO;
import placeholder.organisation.unicms.service.dto.request.ScheduleRequestDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@Controller
@AllArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/schedules")
public class ScheduleController {

    private final static String SUCCESS_GENERATE_LESSON_MESSAGE = "Lessons have been successfully generated";

    private final ScheduleService scheduleService;
    private final GroupService groupService;
    private final LecturerService lecturerService;
    private final SubjectService subjectService;
    private final DurationService durationService;
    private final RoomService roomService;

    @GetMapping("/schedule-setup")
    public String scheduleSetup(Model model,
                                @RequestParam(required = false) Long groupId) {
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("selectedGroupId", groupId);
        if (groupId != null) {
            List<Schedule> schedules = scheduleService.findByGroupId(groupId);
            EnumMap<DayOfWeek, List<Schedule>> schedulesByDay = new EnumMap<>(DayOfWeek.class);
            for (DayOfWeek day : DayOfWeek.values()) {
                schedulesByDay.put(day, new ArrayList<>());
            }
            for (Schedule s : schedules) {
                schedulesByDay.get(s.getDay()).add(s);
            }
            model.addAttribute("schedulesByDay", schedulesByDay);
        }
        return "schedule-setup";
    }

    @GetMapping("/create-schedule")
    public String getCreateScheduleForm(Model model,
                                        @RequestParam(required = false) Long groupId,
                                        @RequestParam(required = false) Long subjectId,
                                        @RequestParam(required = false) DayOfWeek day) {
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("days", DayOfWeek.values());
        model.addAttribute("durations", durationService.findAllDurations());
        model.addAttribute("presetDay", day);
        model.addAttribute("groupId", groupId);
        model.addAttribute("subjectId", subjectId);
        if (groupId != null) {
            model.addAttribute("subjects", subjectService.findSubjectsByGroupId(groupId));
        } else {
            model.addAttribute("subjects", List.of());
        }
        if (subjectId != null) {
            model.addAttribute("lecturers", lecturerService.findLecturersBySubject(subjectId));
        } else {
            model.addAttribute("lecturers", List.of());
        }
        return "create-schedule";
    }

    @GetMapping("/{id}/edit")
    public String getEditScheduleForm(@PathVariable Long id,
                                      @RequestParam(required = false) Long subjectId,
                                      Model model) {
        Schedule schedule = scheduleService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(Schedule.class, String.valueOf(id)));
        Long effectiveSubjectId = subjectId != null ? subjectId : schedule.getSubject().getId();
        model.addAttribute("schedule", schedule);
        model.addAttribute("durations", durationService.findAllDurations());
        model.addAttribute("subjects", subjectService.findSubjectsByGroupId(schedule.getGroup().getId()));
        model.addAttribute("lecturers", lecturerService.findLecturersBySubject(effectiveSubjectId));
        model.addAttribute("subjectId", effectiveSubjectId);
        return "edit-schedule";
    }

    @PostMapping
    public String createSchedule(@Valid @ModelAttribute ScheduleRequestDTO dto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Some fields are invalid");
            return "redirect:/schedules/create-schedule";
        }
        try {
            scheduleService.creatSchedule(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Schedule created successfully");
        } catch (EntityValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/schedules/schedule-setup?groupId=" + dto.getGroupId();
    }

    @PostMapping("/delete-schedule")
    public String deleteSchedule(@RequestParam Long scheduleId,
                                 RedirectAttributes redirectAttributes) {
        scheduleService.removeSchedule(scheduleId);
        redirectAttributes.addFlashAttribute("successMessage", "Schedule deleted successfully");
        return "redirect:/schedules/schedule-setup";
    }


    @GetMapping("/generate-lessons")
    public String getGenerateLessonsForm(@RequestParam Long scheduleId, Model model) {
        Schedule schedule = scheduleService.findById(scheduleId)
            .orElseThrow(() -> new EntityNotFoundException(Schedule.class, String.valueOf(scheduleId)));
        model.addAttribute("schedule", schedule);
        model.addAttribute("rooms", roomService.findAllRooms());
        return "generate-lessons-form";
    }

    @PostMapping("/generate-lessons")
    public String generateLessons(@Valid @ModelAttribute GenerateLessonsRequestDTO dto,
                                  RedirectAttributes redirectAttributes) {
        LocalDate monday = LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LocalDate to = monday.plusWeeks(dto.getWeeks()).minusDays(1);
        scheduleService.generateLessons(dto.getScheduleId(), dto.getClassRoomId(), monday, to);
        redirectAttributes.addFlashAttribute("successMessage", SUCCESS_GENERATE_LESSON_MESSAGE);
        return "redirect:/schedules/schedule-setup";
    }
}
