package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.LessonService;

import java.util.List;

@Controller
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @RequestMapping(value = "/lessons", method = RequestMethod.GET)
    public String getLessons(Model model,
                             @RequestParam(defaultValue = "id") String sortField,
                             @RequestParam(defaultValue = "asc") String sortDirection,
                             @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Page<Lesson> page = lessonService.getFilteredAndSortedLesson(sortField, sortDirection, pageNo);

        List<Lesson> lessons = page.getContent();

        String url = "lessons";

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("lessons", lessons);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextDir", nextDir);
        model.addAttribute("url", url);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        return "lessons";
    }
}
