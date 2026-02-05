package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.*;
import placeholder.organisation.unicms.repository.*;
import placeholder.organisation.unicms.service.dto.LessonDTO;
import placeholder.organisation.unicms.service.dto.StudySubjectDTO;
import placeholder.organisation.unicms.service.mapper.LessonMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import static org.assertj.core.api.AssertionsForClassTypes.in;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
    @Mock
    LessonRepository lessonRepositoryMock;
    @Spy
    LessonMapper lessonMapper = Mappers.getMapper(LessonMapper.class);
    @InjectMocks
    LessonService lessonService;

    @Test
    void findLessonsInRange_shouldReturnLessons_whenDataExists() {
        LocalDate start = LocalDate.of(2025, 10, 1);
        LocalDate end = LocalDate.of(2025, 10, 31);
        long personId = 1L;
        PersonType type = PersonType.Lecturer;
        Lesson lesson = getLesson();

        when(lessonRepositoryMock.findInRange(start, end, personId, type))
                .thenReturn(List.of(lesson));

        List<Lesson> result = lessonService.findLessonsInRange(start, end, personId, type);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(lesson);
        verify(lessonRepositoryMock).findInRange(start, end, personId, type);
    }

    @Test
    void findByDate_shouldReturnEmptyList_whenNoLessonsFound() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        long personId = 1L;
        PersonType type = PersonType.Student;

        when(lessonRepositoryMock.findByDateAndRole(date, personId, type))
                .thenReturn(List.of());

        List<Lesson> result = lessonService.findByDate(date, personId, type);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
        verify(lessonRepositoryMock).findByDateAndRole(date, personId, type);
    }

    @Test
    void updateLesson_changesObject_whenCorrectDtoIsGiven() {
        Lesson initial = getLesson();
        LessonDTO lessonDTO = getLessonDto();
        long id = initial.getId();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(initial));

        lessonService.updateLesson(id, lessonDTO);

        verify(lessonMapper).updateEntityFromDto(lessonDTO, initial);
        verify(lessonRepositoryMock).save(initial);

        assertThat(initial.getStudySubject().getName()).isEqualTo(lessonDTO.getStudySubject().getName());
    }

    Lesson getLesson() {
        return new Lesson(1L, getDuration(), new StudySubject(1L, "Math"), getGroup(), getLecturer(), getClassRoom(), LocalDate.now());
    }

    LessonDTO getLessonDto() {
        return new LessonDTO(new StudySubjectDTO("Physics"));
    }

    Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("Petr");
        lecturer.setSureName("Petrov");
        lecturer.setSalary(40000);
        return lecturer;
    }

    Group getGroup() {
        return new Group(1L, "A-122");
    }

    Duration getDuration() {
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00));
    }

    ClassRoom getClassRoom() {
        return new ClassRoom(1L, "A-101", new ClassRoomType(1L, "Hall", 100L));
    }}