package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.StudySubjectJpa;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class StudySubjectService {

    private final StudySubjectJpa studySubjectJpa;

    public StudySubjectService(StudySubjectJpa studySubjectJpa) {
        this.studySubjectJpa = studySubjectJpa;
    }

    public List<StudySubject> findAllSubjects() {

        List<StudySubject> studySubjects = studySubjectJpa.findAll();
        log.debug("Found {} subject ", studySubjects.size());
        return studySubjects;
    }

    @Transactional
    public void createStudySubject(StudySubject subject) {
        studySubjectJpa.save(subject);
        log.debug("Subject saved successfully. Name: {}}", subject.getName());
    }

    public Optional<StudySubject> findStudySubjectByName(String subjectName) {
        Optional<StudySubject> subject = studySubjectJpa.findByName(subjectName);
        subject.ifPresent(value -> log.debug("Found subject {}", value));
        return subject;
    }

    public Optional<StudySubject> findSubject(long studySubjectId){
        Optional<StudySubject> subject = studySubjectJpa.findById(studySubjectId);
        subject.ifPresent(value -> log.debug("Found subject {}", value));
        return subject;
    }

}
