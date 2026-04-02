package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.LecturerService;

@Controller
@RequestMapping("/lecturers")
@PreAuthorize("hasAnyRole('ADMIN', 'LECTURER')")
public class LecturerController {

    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping()
    public String getLecturers(Model model,
                               @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Lecturer> page = lecturerService.findAll(pageable);

        model.addAttribute("lecturers", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "lecturers");

        return "lecturers";
    }

    @GetMapping("/profile")
    public String getProfile(
        Model model,
        Authentication authentication
    ){
        Lecturer lecturer = lecturerService.findByEmail(authentication.getName());

        model.addAttribute("lecturer", lecturer);
        return "lecturer-profile";
    }
}
