package placeholder.organisation.unicms.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.service.*;
import placeholder.organisation.unicms.service.dto.request.filter.LessonFilter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LessonController.class)
@WithMockUser(username = "user", roles = {"ADMIN"})
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonService lessonService;
    @MockitoBean
    private SubjectService subjectService;
    @MockitoBean
    private LecturerService lecturerService;
    @MockitoBean
    private RoomService roomService;
    @MockitoBean
    private DurationService durationService;
    @MockitoBean
    private GroupService groupService;

    @Test
    void lessonSetup_shouldReturnLessonsView_withPagedLessons() throws Exception {
        List<Lesson> lessons = List.of(getLesson());
        Pageable pageable = PageRequest.of(0, 9, Sort.by("id").ascending());
        Page<Lesson> lessonPage = new PageImpl<>(lessons, pageable, lessons.size());

        when(lessonService.findAll(any(Pageable.class), any(LessonFilter.class)))
            .thenReturn(lessonPage);

        mockMvc.perform(get("/lessons/lesson-setup")
                .param("sort", "id,asc")
                .param("size", "9")
                .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(view().name("lesson-setup"))
            .andExpect(model().attribute("lessons", lessons))
            .andExpect(model().attribute("page", lessonPage))
            .andExpect(model().attribute("url", "lessons/lesson-setup"));
    }

    @Test
    void getAddLessonForm_shouldReturnAddLessonView_withAllData() throws Exception {
        when(lecturerService.findAllLecturers()).thenReturn(List.of());
        when(subjectService.findAllSubjects()).thenReturn(List.of());
        when(groupService.findAllGroups()).thenReturn(List.of());
        when(roomService.findAllRooms()).thenReturn(List.of());
        when(durationService.findAllDurations()).thenReturn(List.of());

        mockMvc.perform(get("/lessons/create-lesson"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-lesson"))
            .andExpect(model().attributeExists("subjects", "groups", "rooms", "durations", "lecturers"));
    }

    @Test
    void getAddLessonForm_shouldFilterLecturersBySubject_whenSubjectIdProvided() throws Exception {
        Lecturer lecturer = getLecturer();
        when(lecturerService.findLecturersBySubject(1L)).thenReturn(List.of(lecturer));

        mockMvc.perform(get("/lessons/create-lesson").param("subjectId", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("create-lesson"))
            .andExpect(model().attribute("lecturers", List.of(lecturer)))
            .andExpect(model().attribute("subjectId", 1L));

        verify(lecturerService).findLecturersBySubject(1L);
    }

    @Test
    void createLesson_shouldRedirectWithSuccess_whenDtoIsValid() throws Exception {
        mockMvc.perform(post("/lessons/create-lesson")
                .param("durationId", "1")
                .param("studySubjectId", "1")
                .param("groupId", "1")
                .param("lecturerId", "1")
                .param("classRoomId", "1")
                .param("date", "2025-10-06")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/lessons/create-lesson"))
            .andExpect(flash().attribute("successMessage", "Lesson has been successfully created"));

        verify(lessonService).createLesson(any(placeholder.organisation.unicms.service.dto.request.LessonRequestDTO.class));
    }

    @Test
    void deleteLesson_shouldRedirectToLessonSetup_whenLessonDeleted() throws Exception {
        mockMvc.perform(post("/lessons/delete-lesson")
                .param("lessonId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("/lessons/lesson-setup*"));

        verify(lessonService).removeLesson(1L);
    }

    private Lesson getLesson() {
        return new Lesson(1L, getDuration(), new Subject(1L, "Math"), getGroup(), getLecturer(), getClassRoom(), LocalDate.now());
    }

    private Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Pork");
        lecturer.setSalary(40000);
        return lecturer;
    }

    private Group getGroup() {
        return new Group(1L, "A-122", getCourse(), null, "info");
    }

    private Course getCourse(){
        return new Course(1L, "SE", List.of(new Subject()));
    }

    private Duration getDuration() {
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 0));
    }

    private Room getClassRoom() {
        return new Room(1L, "A-101", new RoomType(1L, "Hall", 100L));
    }
}