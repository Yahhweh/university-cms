package placeholder.organisation.unicms.controller;

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
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.dto.request.SubjectRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.SubjectFilter;

@Controller
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class SubjectController {

    private final static String removeSubjectMessage = "Subject has been successfully deleted";
    private final static String errorAddSubjectMessage = "Error in validation subject";
    private final static String successAddSubjectMessage = "Subject has been successfully added";

    private final  SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping(value = "/subjects")
    public String getRoomTypes(Model model,
                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                               @ModelAttribute SubjectFilter filter ) {
        Page<Subject> page = subjectService.findAll(pageable, filter);

        model.addAttribute("subjects", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "admin/subjects");
        return "subjects";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/delete-subject")
    public String deleteSubject(RedirectAttributes redirectAttributes, @RequestParam Long subjectId,
                                @PageableDefault(direction = Sort.Direction.ASC, sort = "id") Pageable pageable,
                                @RequestParam String name){

        subjectService.removeStudySubject(subjectId);
        redirectAttributes.addFlashAttribute("successMessage", removeSubjectMessage);
        redirectAttributes.addAttribute("page", pageable.getPageNumber());
        pageable.getSort().forEach(order ->
            redirectAttributes.addAttribute("sort",
                order.getProperty() + "," + order.getDirection().name().toLowerCase())
        );
        redirectAttributes.addAttribute("name", name);
        return "redirect:subjects";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/add-subject")
    public String showAddLessonForm(){
        return "add-subject";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add-subject")
    public String addLesson(@ModelAttribute SubjectRequestDTO subjectRequestDTO, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorMessage", errorAddSubjectMessage);
        }

        subjectService.createSubject(subjectRequestDTO);
        redirectAttributes.addFlashAttribute("successMessage", successAddSubjectMessage);
        return "redirect:add-subject";
    }
}
