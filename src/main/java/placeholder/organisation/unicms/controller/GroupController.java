package placeholder.organisation.unicms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.GroupService;

import java.util.List;

@Controller
public class GroupController {

    GroupService groupService;
    FieldExtractor fieldExtractor;

    public GroupController(GroupService groupService, FieldExtractor fieldExtractor) {
        this.groupService = groupService;
        this.fieldExtractor = fieldExtractor;
    }

    @RequestMapping(path = "/groups", method = RequestMethod.GET)
    public String getGroups(Model model){
        List<String> fields = fieldExtractor.getFieldNames(Group.class);
        List<Group> groups = groupService.findAllGroups();

        model.addAttribute("fields", fields);
        model.addAttribute("objects", groups);

        return "table";
    }
}
