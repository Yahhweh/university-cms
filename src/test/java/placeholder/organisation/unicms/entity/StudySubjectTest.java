package placeholder.organisation.unicms.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.repository.StudySubjectRepository;
import placeholder.organisation.unicms.service.StudySubjectService;
import placeholder.organisation.unicms.service.dto.StudySubjectDTO;
import placeholder.organisation.unicms.service.mapper.StudySubjectMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudySubjectServiceTest {

    @Mock
    private StudySubjectRepository studySubjectRepository;

    @Spy
    private StudySubjectMapper studySubjectMapper = Mappers.getMapper(StudySubjectMapper.class);

    @InjectMocks
    private StudySubjectService studySubjectService;

    @Test
    void updateStudySubject_whenValidStudySubjectDTO_thenStudySubjectIsUpdated() {
        StudySubject initial = getStudySubject();
        StudySubjectDTO changes = getStudySubjectDto();
        long id = initial.getId();

        when(studySubjectRepository.findById(id)).thenReturn(Optional.of(initial));

        studySubjectService.updateStudySubject(id, changes);

        verify(studySubjectMapper).updateEntityFromDto(changes, initial);
        verify(studySubjectRepository).save(initial);

        assertThat(initial.getName()).isEqualTo("Math");
    }

    private StudySubject getStudySubject() {
        StudySubject subject = new StudySubject();
        subject.setId(1L);
        subject.setName("Physics");
        return subject;
    }

    private StudySubjectDTO getStudySubjectDto() {
        StudySubjectDTO dto = new StudySubjectDTO();
        dto.setName("Math");
        return dto;
    }
}