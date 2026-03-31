package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.repository.specifications.SubjectSpecification;
import placeholder.organisation.unicms.service.dto.request.SubjectRequestDTO;
import placeholder.organisation.unicms.service.dto.request.filter.SubjectFilter;
import placeholder.organisation.unicms.service.mapper.SubjectMapper;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectService(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;
        this.subjectMapper = subjectMapper;
    }

    public List<Subject> findAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        log.debug("Found {} subjects in DB", subjects.size());
        return subjects;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createSubject(Subject subject) {
        subjectRepository.save(subject);
        log.debug("Subject saved successfully: {}", subject.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createSubject(SubjectRequestDTO requestDTO) {
        Subject subject = subjectMapper.toEntity(requestDTO);
        subjectRepository.save(subject);
        log.debug("Subject saved successfully: {}", subject.getName());
    }

    public Optional<Subject> findStudySubjectByName(String subjectName) {
        return subjectRepository.findByName(subjectName);
    }

    public Optional<Subject> findSubject(long studySubjectId) {
        return subjectRepository.findById(studySubjectId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void removeStudySubject(long studySubjectId) {
        if (!subjectRepository.existsById(studySubjectId)) {
            throw new EntityNotFoundException(Subject.class, String.valueOf(studySubjectId));
        }
        subjectRepository.deleteById(studySubjectId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateStudySubject(long studySubjectId, SubjectRequestDTO subjectRequestDTO) {
        Subject subject = subjectRepository.findById(studySubjectId)
            .orElseThrow(() -> new EntityNotFoundException(Subject.class, String.valueOf(studySubjectId)));

        subjectMapper.updateEntityFromDto(subjectRequestDTO, subject);
        subjectRepository.save(subject);

        log.debug("Study subject updated successfully. ID: {}", studySubjectId);
    }

    public Page<Subject> findAll(Pageable pageable, SubjectFilter filter) {
        log.debug("Trying to get paginated Subjects: {}", pageable);
        return subjectRepository.findAll(SubjectSpecification.filter(filter), pageable);
    }
}