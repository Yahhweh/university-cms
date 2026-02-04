package placeholder.organisation.unicms.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.RepositoryException;
import placeholder.organisation.unicms.repository.LecturerRepository;
import placeholder.organisation.unicms.repository.StudySubjectRepository;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final StudySubjectRepository studySubjectRepository;

    public LecturerService(LecturerRepository lecturerRepository, StudySubjectRepository studySubjectRepository) {
        this.lecturerRepository = lecturerRepository;
        this.studySubjectRepository = studySubjectRepository;
    }

    public List<Lecturer> findAllLecturers() {
        List<Lecturer> lecturers = lecturerRepository.findAll();
        log.debug("Found size {} lecturers", lecturers.size());
        return lecturers;
    }

    @Transactional
    public void createLecturer(Lecturer lecturer) {
        lecturerRepository.save(lecturer);
        log.debug("Lecturer saved successfully. Name: {}}", lecturer.getName());
    }

    public Optional<Lecturer> findLecturerByNameAndSureName(String name, String sureName) {
        Optional<Lecturer> lecturer = lecturerRepository.findByNameAndSureName(name, sureName);
        lecturer.ifPresent(value -> log.debug("Found lecturer {}", value));
        return lecturer;
    }

    public Optional<Lecturer> findLecturer(long lecturerId) {
        Optional<Lecturer> lecturer = lecturerRepository.findById(lecturerId);
        lecturer.ifPresent(value -> log.debug("Found lecturer {}", value));
        return lecturer;
    }

    public void assignSubjectToLecturer(long subjectId, long lecturerId) {
        try {
            Optional<StudySubject> subject = studySubjectRepository.findById(subjectId);
            Optional<Lecturer> lecturer = lecturerRepository.findById(lecturerId);
            if (subject.isPresent() && lecturer.isPresent()) {
                if (lecturer.get().getStudySubjects().add(subject.get())) {
                    log.info("Lecturer assigned to keep this subject. lecturerId: {}, subjectId: {}", lecturerId, subjectId);
                }
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to add subject to lecturer, lecturer id: " + lecturerId
                    + " subject id: " + subjectId, e);
        }
    }

    public void removeSubjectToLecturer(long subjectId, long lecturerId) {
        try {
            Optional<StudySubject> subject = studySubjectRepository.findById(subjectId);
            Optional<Lecturer> lecturer = lecturerRepository.findById(lecturerId);
            if (subject.isPresent() && lecturer.isPresent()) {
                if (lecturer.get().getStudySubjects().remove(subject.get())) {
                    log.info("Lecturer is not to keeping this subject. lecturerId: {}, subjectId: {}", lecturerId, subjectId);
                }
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to remove subject from lecturer, lecturer id: " + lecturerId
                    + " subject id: " + subjectId, e);
        }
    }

    @Transactional
    public void removeLecturer(long lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) {
            throw new ServiceException("Lecturer not found with id: " + lecturerId);
        }
        lecturerRepository.deleteById(lecturerId);
    }

}
