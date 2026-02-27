package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import placeholder.organisation.unicms.entity.Lecturer;
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
                             @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Lesson> page = lessonService.findAll(pageable);

        model.addAttribute("lessons", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "lessons");

        return "lessons";
    }
}
