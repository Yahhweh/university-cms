package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.request.CourseRequestDTO;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@AllArgsConstructor
public class CourseController {

    private static final String CREATE_MESSAGE = "Course has been successfully created";
    private static final String ERROR_CREATE_MESSAGE = "Error in course validation";
    private static final String DELETE_MESSAGE = "Course has been successfully deleted";

    private final CourseService courseService;
    private final SubjectService subjectService;

    @GetMapping("/create-course")
    public String getCreateCourseForm(Model model) {
        model.addAttribute("subjects", subjectService.findAllSubjects());
        return "create-course";
    }

    @PostMapping("/create-course")
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

    @PostMapping("/delete-course")
    public String deleteCourse(@RequestParam Long courseId,
                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               @RequestParam(required = false) String name,
                               RedirectAttributes redirectAttributes) {
        courseService.removeCourse(courseId);
        redirectAttributes.addFlashAttribute("successMessage", DELETE_MESSAGE);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("name", name != null ? name : "");
        return "redirect:courses";
    }
}
