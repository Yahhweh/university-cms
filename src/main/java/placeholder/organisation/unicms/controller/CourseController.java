package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.request.CourseRequestDTO;

import java.util.List;
import java.util.Objects;

@Controller
@PreAuthorize("hasAnyRole('ADMIN')")
@AllArgsConstructor
public class CourseController {

    private static final String CREATE_MESSAGE = "Course has been successfully created";
    private static final String ERROR_CREATE_MESSAGE = "Error in course validation";
    private static final String DELETE_MESSAGE = "Course has been successfully deleted";

    private final CourseService courseService;
    private final SubjectService subjectService;
    private final LecturerService lecturerService;

    @GetMapping("/admin/create-course")
    public String getCreateCourseForm(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "create-course";
    }

    @PostMapping("/admin/create-course")
    public String createCourse(@ModelAttribute CourseRequestDTO courseRequestDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", ERROR_CREATE_MESSAGE);
            return "redirect:create-course";
        }
        courseService.createCourse(courseRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", CREATE_MESSAGE);
        return "redirect:create-course";
    }

    @PostMapping("/admin/delete-course")
    public String deleteCourse(@RequestParam Long courseId,
                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               @RequestParam(required = false) String name,
                               RedirectAttributes redirectAttributes) {
        courseService.removeCourse(courseId);
        RedirectAttributesHelper.addPageAndFilterAttributes(DELETE_MESSAGE, pageable, redirectAttributes);
        redirectAttributes.addAttribute("name", name != null ? name : "");
        return "redirect:/courses";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/courses")
    public String getCourses(Model model,
                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(required = false) String name) {
        Page<Course> page = courseService.findAll(pageable, name);
        model.addAttribute("courses", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "courses");
        model.addAttribute("name", name);
        return "courses";
    }

    @PreAuthorize("hasAnyRole('LECTURER', 'MENTOR')")
    @GetMapping("/my-courses")
    public String getLecturerCourses(Model model, @RequestParam Long lecturerId, @AuthenticationPrincipal UserDetails userDetails) {
        List<Course> courses = courseService.findCoursesRelatedToLecturer(lecturerId);
        ifLecturerCanCheckCourses(userDetails.getUsername(), lecturerId);

        model.addAttribute("courses", courses);
        return "lecturer-courses";
    }


    private void ifLecturerCanCheckCourses(String email, Long lecturerId) {
        if (Objects.equals(lecturerService.findByEmail(email).getId(), lecturerId)) {
            return;
        }
        throw new AccessDeniedException("You are not allowed to visit this page");
    }
}
