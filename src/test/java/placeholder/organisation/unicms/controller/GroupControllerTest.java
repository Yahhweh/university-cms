package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.GroupService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.StudentService;
import placeholder.organisation.unicms.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(GroupController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})
class GroupControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    GroupService groupService;
    @MockitoBean
    StudentService studentService;
    @MockitoBean
    UserService userService;
    @MockitoBean
    LecturerService lecturerService;
    @MockitoBean
    CourseService courseService;

    @Test
    void getGroups_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        List<Group> groups = List.of(getGroup());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Group> groupPage = new PageImpl<>(groups, pageable, groups.size());

        when(groupService.findAll(pageable))
            .thenReturn(groupPage);

        mockMvc.perform(get("/groups")
                .param("page", "0")
                .param("size", "9")
                .param("sort", "id,asc"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups"))
            .andExpect(model().attributeExists("groups"))
            .andExpect(model().attribute("groups", groups))
            .andExpect(model().attribute("page", groupPage))
            .andExpect(model().attribute("url", "groups"));

        verify(groupService).findAll(pageable);
    }

    @Test
    @WithMockUser(username = "lecturer@test.com", roles = "LECTURER")
    void getLecturerGroups_ShouldReturnViewName_whenEverythingIsCorrect() throws Exception {
        Lecturer lecturer = getLecturer();
        List<Group> groups = List.of(getGroup());

        when(lecturerService.findByEmail("lecturer@test.com")).thenReturn(lecturer);
        when(groupService.findGroupsRelatedToLecturer(lecturer.getId())).thenReturn(groups);

        mockMvc.perform(get("/" + lecturer.getId()+"/groups"))
            .andExpect(status().isOk())
            .andExpect(view().name("groups"))
            .andExpect(model().attribute("groups", groups))
            .andExpect(model().attribute("url", lecturer.getId() +"/groups"));

        verify(lecturerService).findByEmail("lecturer@test.com");
        verify(groupService).findGroupsRelatedToLecturer(lecturer.getId());
    }

    private Group getGroup() {
        Group testGroup = new Group();
        testGroup.setId(1L);
        testGroup.setName("Alpha Group");
        return testGroup;
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Doe");
        return lecturer;
    }
}