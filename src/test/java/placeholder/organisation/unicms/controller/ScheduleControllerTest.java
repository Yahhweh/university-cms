package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
@WithMockUser(username = "user", roles = "ADMIN")
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScheduleService scheduleService;
    @MockitoBean
    private GroupService groupService;
    @MockitoBean
    private LecturerService lecturerService;
    @MockitoBean
    private SubjectService subjectService;
    @MockitoBean
    private DurationService durationService;
    @MockitoBean
    private RoomService roomService;


    @Test
    void scheduleSetup_ShouldReturnScheduleSetupView_whenGroupIdIsNull() throws Exception {
        when(groupService.findAllGroups()).thenReturn(List.of());

        mockMvc.perform(get("/schedules/schedule-setup"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedule-setup"))
            .andExpect(model().attribute("groups", List.of()))
            .andExpect(model().attributeDoesNotExist("schedulesByDay"));
    }

    @Test
    void scheduleSetup_ShouldReturnScheduleSetupViewWithSchedules_whenGroupIdProvided() throws Exception {
        when(groupService.findAllGroups()).thenReturn(List.of());
        when(scheduleService.findByGroupId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/schedules/schedule-setup").param("groupId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedule-setup"))
            .andExpect(model().attribute("selectedGroupId", 1L))
            .andExpect(model().attributeExists("schedulesByDay"));
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void scheduleSetup_ShouldReturnScheduleSetupView_whenRoleIsStaff() throws Exception {
        when(groupService.findAllGroups()).thenReturn(List.of());

        mockMvc.perform(get("/schedules/schedule-setup"))
            .andExpect(status().isOk())
            .andExpect(view().name("schedule-setup"));
    }

    @Test
    @WithMockUser(roles = "LECTURER")
    void scheduleSetup_ShouldReturnForbidden_whenRoleIsLecturer() throws Exception {
        mockMvc.perform(get("/schedules/schedule-setup"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void scheduleSetup_ShouldReturnForbidden_whenRoleIsStudent() throws Exception {
        mockMvc.perform(get("/schedules/schedule-setup"))
            .andExpect(status().isForbidden());
    }

    @Test
    void getCreateScheduleForm_ShouldReturnCreateScheduleView_whenNoParams() throws Exception {
        when(groupService.findAllGroups()).thenReturn(List.of());
        when(durationService.findAllDurations()).thenReturn(List.of());

        mockMvc.perform(get("/schedules/create-schedule"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-schedule"))
            .andExpect(model().attribute("groups", List.of()))
            .andExpect(model().attribute("durations", List.of()))
            .andExpect(model().attribute("subjects", List.of()))
            .andExpect(model().attribute("lecturers", List.of()));
    }

    @Test
    void getCreateScheduleForm_ShouldReturnCreateScheduleView_whenGroupIdProvided() throws Exception {
        List<Subject> subjects = List.of(new Subject(1L, "Math"));
        when(groupService.findAllGroups()).thenReturn(List.of());
        when(durationService.findAllDurations()).thenReturn(List.of());
        when(subjectService.findSubjectsByGroupId(1L)).thenReturn(subjects);

        mockMvc.perform(get("/schedules/create-schedule").param("groupId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-schedule"))
            .andExpect(model().attribute("groupId", 1L))
            .andExpect(model().attribute("subjects", subjects));
    }

    @Test
    void getCreateScheduleForm_ShouldReturnCreateScheduleView_whenGroupAndSubjectIdProvided() throws Exception {
        List<Lecturer> lecturers = List.of(getLecturer());
        when(groupService.findAllGroups()).thenReturn(List.of());
        when(durationService.findAllDurations()).thenReturn(List.of());
        when(subjectService.findSubjectsByGroupId(1L)).thenReturn(List.of());
        when(lecturerService.findLecturersBySubject(2L)).thenReturn(lecturers);

        mockMvc.perform(get("/schedules/create-schedule")
                .param("groupId", "1")
                .param("subjectId", "2"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-schedule"))
            .andExpect(model().attribute("subjectId", 2L))
            .andExpect(model().attribute("lecturers", lecturers));
    }

    @Test
    void getEditScheduleForm_ShouldReturnEditScheduleView_whenScheduleExists() throws Exception {
        Schedule schedule = getSchedule();
        when(scheduleService.findById(1L)).thenReturn(Optional.of(schedule));
        when(durationService.findAllDurations()).thenReturn(List.of());
        when(subjectService.findSubjectsByGroupId(schedule.getGroup().getId())).thenReturn(List.of());
        when(lecturerService.findLecturersBySubject(schedule.getSubject().getId())).thenReturn(List.of());

        mockMvc.perform(get("/schedules/1/edit"))
            .andExpect(status().isOk())
            .andExpect(view().name("edit-schedule"))
            .andExpect(model().attribute("schedule", schedule))
            .andExpect(model().attribute("subjectId", schedule.getSubject().getId()));
    }

    @Test
    void createSchedule_ShouldRedirectToScheduleSetup_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/schedules")
                .param("day", "MONDAY")
                .param("groupId", "1")
                .param("lecturerId", "1")
                .param("subjectId", "1")
                .param("durationId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/schedules/schedule-setup*"));

        verify(scheduleService).creatSchedule(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createSchedule_ShouldRedirectWithError_whenDataIsInvalid() throws Exception {
        mockMvc.perform(post("/schedules")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/schedules/create-schedule"))
            .andExpect(flash().attribute("errorMessage", "Some fields are invalid"));
    }

    @Test
    void deleteSchedule_ShouldRedirectToScheduleSetup_whenScheduleDeleted() throws Exception {
        mockMvc.perform(post("/schedules/delete-schedule")
                .param("scheduleId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/schedules/schedule-setup"))
            .andExpect(flash().attribute("successMessage", "Schedule deleted successfully"));

        verify(scheduleService).removeSchedule(1L);
    }

    @Test
    void getGenerateLessonsForm_ShouldReturnGenerateLessonsView_whenScheduleExists() throws Exception {
        Schedule schedule = getSchedule();
        when(scheduleService.findById(1L)).thenReturn(Optional.of(schedule));
        when(roomService.findAllRooms()).thenReturn(List.of());

        mockMvc.perform(get("/schedules/generate-lessons").param("scheduleId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("generate-lessons-form"))
            .andExpect(model().attribute("schedule", schedule))
            .andExpect(model().attribute("rooms", List.of()));
    }

    @Test
    void generateLessons_ShouldRedirectToScheduleSetup_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/schedules/generate-lessons")
                .param("scheduleId", "1")
                .param("classRoomId", "1")
                .param("weeks", "2")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/schedules/schedule-setup"))
            .andExpect(flash().attribute("successMessage", "Lessons have been successfully generated"));

        verify(scheduleService).generateLessons(
            org.mockito.ArgumentMatchers.eq(1L),
            org.mockito.ArgumentMatchers.eq(1L),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any()
        );
    }

    private Schedule getSchedule() {
        Subject subject = new Subject(1L, "Math");
        Group group = new Group();
        group.setId(1L);
        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setDay(DayOfWeek.MONDAY);
        schedule.setSubject(subject);
        schedule.setGroup(group);
        return schedule;
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Doe");
        return lecturer;
    }
}
