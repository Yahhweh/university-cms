package placeholder.organisation.unicms.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.DurationService;
import placeholder.organisation.unicms.service.LessonService;
import placeholder.organisation.unicms.service.dto.request.filter.LessonFilterRequestDTO;

@Controller
@PreAuthorize("hasRole('STAFF')")
@RequestMapping("/staff")
@AllArgsConstructor
public class StaffController {

    private final LessonService lessonService;
    private final DurationService durationService;

    @GetMapping()
    public String getStaffButtons(Model model){
        return "staff";
    }


    
}