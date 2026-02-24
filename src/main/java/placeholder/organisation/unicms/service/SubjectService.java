package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.RoomType;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.SubjectDTO;
import placeholder.organisation.unicms.service.mapper.StudySubjectMapper;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final StudySubjectMapper studySubjectMapper;
    private  final  FilterAndSorterOfEntities filterAndSorterOfEntities;

    public SubjectService(SubjectRepository subjectRepository, StudySubjectMapper studySubjectMapper,
                          FilterAndSorterOfEntities filterAndSorterOfEntities) {
        this.subjectRepository = subjectRepository;
        this.studySubjectMapper = studySubjectMapper;
        this.filterAndSorterOfEntities = filterAndSorterOfEntities;
    }

    public List<Subject> findAllSubjects() {
        List<Subject> subjects = subjectRepository.findAll();
        log.debug("Found {} subjects in DB", subjects.size());
        return subjects;
    }

    @Transactional
    public void createStudySubject(Subject subject) {
        subjectRepository.save(subject);
        log.debug("Subject saved successfully: {}", subject.getName());
    }

    public Optional<Subject> findStudySubjectByName(String subjectName) {
        return subjectRepository.findByName(subjectName);
    }

    public Optional<Subject> findSubject(long studySubjectId) {
        return subjectRepository.findById(studySubjectId);
    }

    @Transactional
    public void removeStudySubject(long studySubjectId) {
        if (!subjectRepository.existsById(studySubjectId)) {
            throw new EntityNotFoundException(Subject.class, String.valueOf(studySubjectId));
        }
        subjectRepository.deleteById(studySubjectId);
    }

    @Transactional
    public void updateStudySubject(long studySubjectId, SubjectDTO subjectDTO) {
        Subject subject = subjectRepository.findById(studySubjectId)
                .orElseThrow(() -> new EntityNotFoundException(Subject.class, String.valueOf(studySubjectId)));

        studySubjectMapper.updateEntityFromDto(subjectDTO, subject);
        subjectRepository.save(subject);

        log.debug("Study subject updated successfully. ID: {}", studySubjectId);
    }

    public Page<Subject> getFilteredAndSortedSubject(String sortField, String sortDir){
        return filterAndSorterOfEntities.getFilteredAndSortedEntities(sortField, sortDir, subjectRepository, Specification.where(null));
    }
}