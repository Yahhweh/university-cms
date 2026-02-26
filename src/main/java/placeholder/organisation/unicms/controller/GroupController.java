package placeholder.organisation.unicms.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
                            @RequestParam(defaultValue = "id") String sortField,
                            @RequestParam(defaultValue = "asc") String sortDirection,
                            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo) {

        Page<Group> page = groupService.getFilteredAndSortedGroup(sortField, sortDirection, pageNo);

        List<Group> groups = page.getContent();

        String url = "groups";

        String nextDir = sortDirection.equals("asc") ? "desc" : (sortDirection.equals("desc") ? "none" : "asc");

        model.addAttribute("groups", groups);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("nextDir", nextDir);
        model.addAttribute("url", url);

        return "groups";
    }
}
