package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.StudySubjectRepository;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class StudySubjectService {

    private final StudySubjectRepository studySubjectRepository;

    public StudySubjectService(StudySubjectRepository studySubjectRepository) {
        this.studySubjectRepository = studySubjectRepository;
    }

    public List<StudySubject> findAllSubjects() {

        List<StudySubject> studySubjects = studySubjectRepository.findAll();
        log.debug("Found {} subject ", studySubjects.size());
        return studySubjects;
    }

    @Transactional
    public void createStudySubject(StudySubject subject) {
        studySubjectRepository.save(subject);
        log.debug("Subject saved successfully. Name: {}}", subject.getName());
    }

    public Optional<StudySubject> findStudySubjectByName(String subjectName) {
        Optional<StudySubject> subject = studySubjectRepository.findByName(subjectName);
        subject.ifPresent(value -> log.debug("Found subject {}", value));
        return subject;
    }

    public Optional<StudySubject> findSubject(long studySubjectId) {
        Optional<StudySubject> subject = studySubjectRepository.findById(studySubjectId);
        subject.ifPresent(value -> log.debug("Found subject {}", value));
        return subject;
    }

    @Transactional
    public void removeStudySubject(long studySubjectId) {
        if (!studySubjectRepository.existsById(studySubjectId)) {
            throw new ServiceException("Study subject not found with id: " + studySubjectId);
        }
        studySubjectRepository.deleteById(studySubjectId);
    }

}
