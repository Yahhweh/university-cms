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
import placeholder.organisation.unicms.entity.Duration;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.service.GroupService;

import java.util.List;

@Controller
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @RequestMapping(path = "/groups", method = RequestMethod.GET)
    public String getGroups(Model model,
                            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<Group> page = groupService.findAll(pageable);

        model.addAttribute("groups", page.getContent());
        model.addAttribute("page", page);
        model.addAttribute("url", "groups");

        return "groups";
    }
}
