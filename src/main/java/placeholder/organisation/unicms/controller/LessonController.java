package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Lesson;
import placeholder.organisation.unicms.service.LessonService;

import java.util.List;

@Controller
public class LessonController {

    LessonService lessonService;
    FieldExtractor fieldExtractor;

    public LessonController(LessonService lessonService, FieldExtractor fieldExtractor) {
        this.lessonService = lessonService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/lessons", method = RequestMethod.GET)
    public String getLessons(Model model){
        List<String> fields = fieldExtractor.getFieldNames(Lesson.class);
        List<Lesson> lessons = lessonService.findAllLessons();

        model.addAttribute("fields", fields);
        model.addAttribute("objects", lessons);
        return "table";
    }
}
