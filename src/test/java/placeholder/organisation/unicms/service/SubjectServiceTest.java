package placeholder.organisation.unicms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.service.dto.SubjectDTO;
import placeholder.organisation.unicms.service.mapper.StudySubjectMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Spy
    private StudySubjectMapper studySubjectMapper = Mappers.getMapper(StudySubjectMapper.class);

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void updateStudySubject_whenValidStudySubjectDTO_thenStudySubjectIsUpdated() {
        Subject initial = getStudySubject();
        SubjectDTO changes = getStudySubjectDto();
        long id = initial.getId();

        when(subjectRepository.findById(id)).thenReturn(Optional.of(initial));

        subjectService.updateStudySubject(id, changes);

        verify(studySubjectMapper).updateEntityFromDto(changes, initial);
        verify(subjectRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Math");
    }

    private Subject getStudySubject() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Physics");
        return subject;
    }

    private SubjectDTO getStudySubjectDto() {
        SubjectDTO dto = new SubjectDTO();
        dto.setName("Math");
        return dto;
    }
}