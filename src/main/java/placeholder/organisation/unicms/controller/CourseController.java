package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.entity.Course;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.request.CourseRequestDTO;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final SubjectService subjectService;

    private static final String successAddMessage = "Course has been successfully created";
    private static final String errorAddMessage = "Error in course validation";
    private static final String successDeleteMessage = "Course has been successfully deleted";

    @GetMapping("/courses")
    public String getCourses(Model model,
                             @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(required = false) String name) {
        Page<Course> page = courseService.findAll(pageable, name);
        model.addAttribute("courses", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "admin/courses");
        model.addAttribute("name", name);
        return "courses";
    }

    @GetMapping("/add-course")
    public String getAddCourseForm(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "add-course";
    }

    @PostMapping("/add-course")
    public String addCourse(@ModelAttribute CourseRequestDTO courseRequestDTO,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", errorAddMessage);
            return "redirect:add-course";
        }
        courseService.createCourse(courseRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", successAddMessage);
        return "redirect:add-course";
    }

    @PostMapping("/delete-course")
    public String deleteCourse(@RequestParam Long courseId,
                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               @RequestParam(required = false) String name,
                               RedirectAttributes redirectAttributes) {
        courseService.removeCourse(courseId);
        redirectAttributes.addFlashAttribute("successMessage", successDeleteMessage);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("name", name != null ? name : "");
        return "redirect:courses";
    }
}
