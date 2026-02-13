package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.repository.StudySubjectRepository;
import placeholder.organisation.unicms.entity.StudySubject;
import placeholder.organisation.unicms.service.dto.StudySubjectDTO;
import placeholder.organisation.unicms.service.mapper.StudySubjectMapper;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class StudySubjectService {

    private final StudySubjectRepository studySubjectRepository;
    private final StudySubjectMapper studySubjectMapper;

    public StudySubjectService(StudySubjectRepository studySubjectRepository, StudySubjectMapper studySubjectMapper) {
        this.studySubjectRepository = studySubjectRepository;
        this.studySubjectMapper = studySubjectMapper;
    }

    public List<StudySubject> findAllSubjects() {
        List<StudySubject> studySubjects = studySubjectRepository.findAll();
        log.debug("Found {} subjects in DB", studySubjects.size());
        return studySubjects;
    }

    @Transactional
    public void createStudySubject(StudySubject subject) {
        studySubjectRepository.save(subject);
        log.debug("Subject saved successfully: {}", subject.getName());
    }

    public Optional<StudySubject> findStudySubjectByName(String subjectName) {
        return studySubjectRepository.findByName(subjectName);
    }

    public Optional<StudySubject> findSubject(long studySubjectId) {
        return studySubjectRepository.findById(studySubjectId);
    }

    @Transactional
    public void removeStudySubject(long studySubjectId) {
        if (!studySubjectRepository.existsById(studySubjectId)) {
            throw new EntityNotFoundException(StudySubject.class, String.valueOf(studySubjectId));
        }
        studySubjectRepository.deleteById(studySubjectId);
    }

    @Transactional
    public void updateStudySubject(long studySubjectId, StudySubjectDTO studySubjectDTO) {
        StudySubject studySubject = studySubjectRepository.findById(studySubjectId)
                .orElseThrow(() -> new EntityNotFoundException(StudySubject.class, String.valueOf(studySubjectId)));

        studySubjectMapper.updateEntityFromDto(studySubjectDTO, studySubject);
        studySubjectRepository.save(studySubject);

        log.debug("Study subject updated successfully. ID: {}", studySubjectId);
    }
}