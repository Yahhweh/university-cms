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
import placeholder.organisation.unicms.service.dto.request.filter.LessonFilterRequestDTO;
import placeholder.organisation.unicms.service.dto.request.LessonRequestDTO;

@Slf4j
@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/lessons")
@Validated
public class LessonController {

    private final LessonService lessonService;
    private final SubjectService subjectService;
    private final LecturerService lecturerService;
    private final RoomService roomService;
    private final DurationService durationService;
    private final GroupService groupService;

    private final static String successAddLessonMessage = "Lesson has been successfully created";
    private final static String validationAddLessonMessage = "Some of your forms are not valid";
    private final static String successRemoveLessonMessage = "Lesson has been successfully deleted";


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
                              @ModelAttribute("filters") LessonFilterRequestDTO requestDTO) {
        Page<Lesson> lessons = lessonService.findAll(pageable, requestDTO);
        model.addAttribute("url", "lessons/lesson-setup");
        model.addAttribute("page", lessons);
        model.addAttribute("lessons", lessons.getContent());
        model.addAttribute("durations", durationService.findAllDurations());
        return "lesson-setup";
    }


    @GetMapping("/add-lesson")
    public String getAddLessonForm(Model model, @RequestParam(required = false) Long subjectId) {
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
        return "add-lesson";
    }

    @PostMapping(value = "/add-lesson")
    public String addLesson(RedirectAttributes redirectAttributes, @Valid @ModelAttribute LessonRequestDTO lessonRequestDTO,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", validationAddLessonMessage);
            return "redirect:/lessons/add-lesson";
        }
        lessonService.createLesson(lessonRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", successAddLessonMessage);
        return "redirect:/lessons/add-lesson";
    }

    @PostMapping(value = "/delete-lesson")
    public String deleteLesson(RedirectAttributes redirectAttributes, @RequestParam Long lessonId,
                               @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                               @ModelAttribute("filters") LessonFilterRequestDTO requestDTO) {
        lessonService.removeLesson(lessonId);
        addRedirectAttributes(pageable, requestDTO, redirectAttributes);
        return "redirect:/lessons/lesson-setup";
    }


    private void addRedirectAttributes(Pageable pageable, LessonFilterRequestDTO requestDTO, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("successMessage", successRemoveLessonMessage);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("durationId", requestDTO.getDurationId() != null? requestDTO.getDurationId() : "");
        redirectAttributes.addAttribute("subject", requestDTO.getSubject() != null ? requestDTO.getSubject().toLowerCase() : "");
        redirectAttributes.addAttribute("group", requestDTO.getGroup() != null ? requestDTO.getGroup().toLowerCase() : "");
        redirectAttributes.addAttribute("lecturer.name", requestDTO.getLecturer().getName() != null ? requestDTO.getLecturer().getName() : "");
        redirectAttributes.addAttribute("lecturer.sureName", requestDTO.getLecturer().getSureName() != null ? requestDTO.getLecturer().getSureName() : "");
        redirectAttributes.addAttribute("room", requestDTO.getRoom() != null ? requestDTO.getRoom().toLowerCase(): "");
    }
}
