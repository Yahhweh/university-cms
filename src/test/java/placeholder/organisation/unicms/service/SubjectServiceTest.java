package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.service.dto.request.SubjectRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.SubjectFilter;
import placeholder.organisation.unicms.service.mapper.SubjectMapper;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Spy
    private SubjectMapper subjectMapper = Mappers.getMapper(SubjectMapper.class);

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void updateStudySubject_whenValidStudySubjectDTO_thenStudySubjectIsUpdated() {
        Subject initial = getStudySubject();
        SubjectRequestDTO changes = getStudySubjectDto();
        long id = initial.getId();

        when(subjectRepository.findById(id)).thenReturn(Optional.of(initial));

        subjectService.updateStudySubject(id, changes);

        verify(subjectMapper).updateEntityFromDto(changes, initial);
        verify(subjectRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Math");
    }

    @Test
    void createSubject_shouldSaveEntity_whenDtoGiven() {
        SubjectRequestDTO dto = getStudySubjectDto();

        subjectService.createSubject(dto);

        verify(subjectMapper).toEntity(dto);
        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    void removeStudySubject_shouldDelete_whenSubjectExists() {
        when(subjectRepository.existsById(1L)).thenReturn(true);

        subjectService.removeStudySubject(1L);

        verify(subjectRepository).deleteById(1L);
    }

    @Test
    void removeStudySubject_shouldThrowEntityNotFoundException_whenSubjectNotFound() {
        when(subjectRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> subjectService.removeStudySubject(99L));

        verify(subjectRepository, never()).deleteById(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void findAll_shouldReturnPage_whenNameFilterGiven() {
        List<Subject> subjects = List.of(getStudySubject());
        Pageable pageable = PageRequest.of(0, 9);
        Page<Subject> expectedPage = new PageImpl<>(subjects, pageable, subjects.size());

        when(subjectRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(expectedPage);

        Page<Subject> result = subjectService.findAll(pageable, new SubjectFilter("Physics"));

        assertThat(result).isEqualTo(expectedPage);
        verify(subjectRepository).findAll(any(Specification.class), eq(pageable));
    }

    private Subject getStudySubject() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Physics");
        return subject;
    }

    private SubjectRequestDTO getStudySubjectDto() {
        SubjectRequestDTO dto = new SubjectRequestDTO();
        dto.setName("Math");
        return dto;
    }
}