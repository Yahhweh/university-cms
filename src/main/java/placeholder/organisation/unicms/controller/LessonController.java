package placeholder.organisation.unicms.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.filter.LessonFilter;
import placeholder.organisation.unicms.service.dto.request.LessonRequestDTO;

@Slf4j
@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/lessons")
@Validated
public class LessonController {

    private static final String CREATE_LESSON_MESSAGE = "Lesson has been successfully created";
    private static final String VALIDATION_ADD_LESSON_MESSAGE = "Some of your forms are not valid";
    private static final String REMOVE_LESSON_MESSAGE = "Lesson has been successfully deleted";

    private final LessonService lessonService;
    private final SubjectService subjectService;
    private final LecturerService lecturerService;
    private final RoomService roomService;
    private final DurationService durationService;
    private final GroupService groupService;

    public LessonController(LessonService lessonService, SubjectService subjectService, LecturerService lecturerService, RoomService roomService, DurationService durationService, GroupService groupService) {
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.lecturerService = lecturerService;
        this.roomService = roomService;
        this.durationService = durationService;
        this.groupService = groupService;
    }

    @GetMapping("/lesson-setup")
    public String lessonSetup(Model model, @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                              @ModelAttribute("filters") LessonFilter filter) {
        Page<Lesson> lessons = lessonService.findAll(pageable, filter);
        model.addAttribute("url", "lessons/lesson-setup");
        model.addAttribute("page", lessons);
        model.addAttribute("lessons", lessons.getContent());
        model.addAttribute("durations", durationService.findAllDurations());
        return "lesson-setup";
    }


    @GetMapping("/create-lesson")
    public String getCreateLessonForm(Model model, @RequestParam(required = false) Long subjectId) {
        if (subjectId != null) {
            model.addAttribute("lecturers", lecturerService.findLecturersBySubject(subjectId));
            model.addAttribute("subjectId", subjectId);
        } else {
            model.addAttribute("lecturers", lecturerService.findAllLecturers());
        }
        model.addAttribute("subjects", subjectService.findAllSubjects());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("rooms", roomService.findAllRooms());
        model.addAttribute("durations", durationService.findAllDurations());
        return "create-lesson";
    }

    @PostMapping ("/create-lesson")
    public String createLesson(RedirectAttributes redirectAttributes, @Valid @ModelAttribute LessonRequestDTO lessonRequestDTO,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", VALIDATION_ADD_LESSON_MESSAGE);
            return "redirect:/lessons/create-lesson";
        }
        lessonService.createLesson(lessonRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_LESSON_MESSAGE);
        return "redirect:/lessons/create-lesson";
    }

    @PostMapping("/delete-lesson")
    public String deleteLesson(RedirectAttributes redirectAttributes, @RequestParam Long lessonId,
                               @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                               @ModelAttribute("filters") LessonFilter filter) {
        lessonService.removeLesson(lessonId);
        PageProvider.providePages(REMOVE_LESSON_MESSAGE, pageable, redirectAttributes, filter);
        return "redirect:/lessons/lesson-setup";
    }
}
