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
import placeholder.organisation.unicms.service.mapper.LessonMapper;
import placeholder.organisation.unicms.service.validation.LessonValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {
    @Mock
    LessonRepository lessonRepositoryMock;
    @Spy
    LessonMapper lessonMapper = Mappers.getMapper(LessonMapper.class);
    @Mock
    LessonValidator lessonValidator;
    @Mock
    StudentRepository studentRepository;
    @Mock
    LecturerRepository lecturerRepository;
    @Mock
    SubjectRepository subjectRepository;
    @InjectMocks
    LessonService lessonService;

    @Test
    void findLessonsInRange_shouldReturnLessons_whenStudentExists() {
        LocalDate start = LocalDate.of(2025, 10, 1);
        LocalDate end = LocalDate.of(2025, 10, 31);
        long personId = 1L;
        Lesson lesson = getLesson();

        when(studentRepository.existsById(personId)).thenReturn(true);
        when(lessonRepositoryMock.findInRangeForStudent(start, end, personId))
                .thenReturn(List.of(lesson));

        List<Lesson> result = lessonService.findLessonsInRange(start, end, personId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(lesson);
        verify(studentRepository).existsById(personId);
        verify(lessonRepositoryMock).findInRangeForStudent(start, end, personId);
        verify(lecturerRepository, never()).existsById(personId);
    }

    @Test
    void findLessonsInRange_shouldReturnLessons_whenLecturerExists() {
        LocalDate start = LocalDate.of(2025, 10, 1);
        LocalDate end = LocalDate.of(2025, 10, 31);
        long personId = 2L;
        Lesson lesson = getLesson();

        when(studentRepository.existsById(personId)).thenReturn(false);
        when(lecturerRepository.existsById(personId)).thenReturn(true);
        when(lessonRepositoryMock.findInRangeForLecturer(start, end, personId))
                .thenReturn(List.of(lesson));

        List<Lesson> result = lessonService.findLessonsInRange(start, end, personId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(lesson);
        verify(studentRepository).existsById(personId);
        verify(lecturerRepository).existsById(personId);
        verify(lessonRepositoryMock).findInRangeForLecturer(start, end, personId);
    }

    @Test
    void findLessonsInRange_shouldThrowException_whenPersonNotFound() {
        LocalDate start = LocalDate.of(2025, 10, 1);
        LocalDate end = LocalDate.of(2025, 10, 31);
        long personId = 999L;

        when(studentRepository.existsById(personId)).thenReturn(false);
        when(lecturerRepository.existsById(personId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> lessonService.findLessonsInRange(start, end, personId));

        verify(studentRepository).existsById(personId);
        verify(lecturerRepository).existsById(personId);
    }

    @Test
    void findByDate_shouldReturnLessons_whenStudentExists() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        long personId = 1L;
        Lesson lesson = getLesson();

        when(studentRepository.existsById(personId)).thenReturn(true);
        when(lessonRepositoryMock.findByDateForStudent(date, personId))
                .thenReturn(List.of(lesson));

        List<Lesson> result = lessonService.findByDate(date, personId);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        verify(studentRepository).existsById(personId);
        verify(lessonRepositoryMock).findByDateForStudent(date, personId);
    }

    @Test
    void findByDate_shouldReturnEmptyList_whenLecturerHasNoLessons() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        long personId = 2L;

        when(studentRepository.existsById(personId)).thenReturn(false);
        when(lecturerRepository.existsById(personId)).thenReturn(true);
        when(lessonRepositoryMock.findByDateAndLecturerId(date, personId))
                .thenReturn(List.of());

        List<Lesson> result = lessonService.findByDate(date, personId);

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();
        verify(studentRepository).existsById(personId);
        verify(lecturerRepository).existsById(personId);
        verify(lessonRepositoryMock).findByDateAndLecturerId(date, personId);
    }

    @Test
    void findByDate_shouldThrowException_whenPersonNotFound() {
        LocalDate date = LocalDate.of(2025, 12, 25);
        long personId = 999L;

        when(studentRepository.existsById(personId)).thenReturn(false);
        when(lecturerRepository.existsById(personId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> lessonService.findByDate(date, personId));

        verify(studentRepository).existsById(personId);
        verify(lecturerRepository).existsById(personId);
    }

    @Test
    void updateLesson_changesObject_whenCorrectDtoIsGiven() {
        Lesson initial = getLesson();
        LessonDTO lessonDTO = getLessonDto();
        long id = initial.getId();
        long studySubjectId = lessonDTO.getStudySubjectId();

        when(lessonRepositoryMock.findById(id)).thenReturn(Optional.of(initial));
        when(subjectRepository.getReferenceById(studySubjectId)).thenReturn(new Subject("Physics"));

        lessonService.updateLesson(id, lessonDTO);

        verify(lessonMapper).updateEntityFromDto(lessonDTO, initial);
        verify(lessonRepositoryMock).save(initial);
        verify(lessonValidator).validateLesson(initial, id);

        assertThat(initial.getSubject().getName()).isEqualTo("Physics");
    }

    @Test
    void createLesson_shouldThrowEntityValidationException_whenWrongLessonGiven() {
        Lesson lesson = getLesson();
        lesson.getRoom().getRoomType().setCapacity(0L);

        doThrow(EntityValidationException.class).when(lessonValidator).validateLesson(lesson, -1L);

        assertThrows(EntityValidationException.class, () -> lessonService.createLesson(lesson));
    }

    @Test
    void updateLesson_shouldThrowEntityValidationException_whenWrongLessonGiven() {
        Lesson lesson = getLesson();
        lesson.getRoom().getRoomType().setCapacity(0L);
        LessonDTO lessonDto = getLessonDto();

        when(lessonRepositoryMock.findById(lesson.getId())).thenReturn(Optional.of(lesson));

        doThrow(EntityValidationException.class)
                .when(lessonValidator)
                .validateLesson(any(Lesson.class), anyLong());

        assertThrows(EntityValidationException.class, () ->
                lessonService.updateLesson(lesson.getId(), lessonDto)
        );

        verify(lessonValidator, times(1)).validateLesson(any(Lesson.class), anyLong());
    }
    @Test
    void createLesson_shouldSave_whenCorrectLessonGiven() {
        Lesson lesson = getLesson();

        assertDoesNotThrow(() -> lessonService.createLesson(lesson));

        verify(lessonValidator).validateLesson(lesson, -
                1L);
        verify(lessonRepositoryMock).save(lesson);
    }

    @Test
    void removeLesson_shouldRemoveLesson_WhenLessonExists() {
        Lesson lesson = getLesson();

        when(lessonRepositoryMock.existsById(lesson.getId())).thenReturn(true);

        lessonService.removeLesson(lesson.getId());

        verify(lessonRepositoryMock).deleteById(lesson.getId());
    }

    @Test
    void removeLesson_shouldThrowEntityNotFound_WhenLessonDoesNotExist() {
        long id = 22L;

        when(lessonRepositoryMock.existsById(id)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lessonService.removeLesson(id));
        verify(lessonRepositoryMock).existsById(id);
    }

    Lesson getLesson() {
        return new Lesson(1L, getDuration(), new Subject(1L, "Math"), getGroup(), getLecturer(), getClassRoom(), LocalDate.now());
    }

    LessonDTO getLessonDto() {
        return LessonDTO.builder()
                .studySubjectId(10L)
                .build();
    }

    Lecturer getLecturer() {
        Lecturer lecturer = new Lecturer();
        lecturer.setId(1L);
        lecturer.setName("John");
        lecturer.setSureName("Pork");
        lecturer.setSalary(40000);
        return lecturer;
    }

    Group getGroup() {
        return new Group(1L, "A-122");
    }

    Duration getDuration() {
        return new Duration(1L, LocalTime.of(8, 30), LocalTime.of(10, 00));
    }

    Room getClassRoom() {
        return new Room(1L, "A-101", new RoomType(1L, "Hall", 100L));
    }
}