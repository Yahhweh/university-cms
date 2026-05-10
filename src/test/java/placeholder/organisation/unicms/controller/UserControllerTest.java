package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.repository.GroupRepository;
import placeholder.organisation.unicms.repository.LessonRepository;
import placeholder.organisation.unicms.service.CourseService;
import placeholder.organisation.unicms.service.LecturerService;
import placeholder.organisation.unicms.service.SubjectService;
import placeholder.organisation.unicms.service.UserService;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;
    @MockitoBean
    LecturerService lecturerService;
    @MockitoBean
    SubjectService subjectService;
    @MockitoBean
    CourseService courseService;
    @MockitoBean
    GroupRepository groupRepository;
    @MockitoBean
    LessonRepository lessonRepository;

    @Test
    void getSchedule_shouldReturnScheduleSetupView_whenNoGroupIdProvided() throws Exception {
        List<Group> groups = List.of(getGroup());
        when(groupRepository.findAll()).thenReturn(groups);

        mockMvc.perform(get("/users/schedule-setup"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedule-setup"))
            .andExpect(model().attribute("groups", groups))
            .andExpect(model().attribute("url", "/users/schedule-setup"))
            .andExpect(model().attributeDoesNotExist("lessonsByDay"));
    }

    @Test
    void getSchedule_shouldReturnScheduleSetupViewWithLessons_whenGroupIdProvided() throws Exception {
        long groupId = 1L;
        List<Group> groups = List.of(getGroup());
        when(groupRepository.findAll()).thenReturn(groups);
        when(lessonRepository.findByGroupId(groupId)).thenReturn(List.of());

        mockMvc.perform(get("/users/schedule-setup").param("groupId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedule-setup"))
            .andExpect(model().attribute("groups", groups))
            .andExpect(model().attributeExists("lessonsByDay"));

        verify(lessonRepository).findByGroupId(groupId);
    }

    private Group getGroup() {
        Group group = new Group();
        group.setId(1L);
        group.setName("A-122");
        return group;
    }
}