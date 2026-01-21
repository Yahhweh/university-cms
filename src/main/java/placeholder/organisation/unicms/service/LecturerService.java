package placeholder.organisation.unicms.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.dao.DaoException;
import placeholder.organisation.unicms.dao.LecturerDao;
import placeholder.organisation.unicms.dao.StudySubjectDao;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LecturerService {
    LecturerDao lecturerDao;
    StudySubjectDao studySubjectDao;

    public LecturerService(LecturerDao lecturerDao, StudySubjectDao studySubjectDao) {
        this.lecturerDao = lecturerDao;
        this.studySubjectDao = studySubjectDao;
    }

    public List<Lecturer> findAllLecturers(){
        List<Lecturer> lecturers = lecturerDao.findAll();
        log.debug("Found size {} lecturers", lecturers.size());
        return lecturers;
    }

    @Transactional
    public void addLecturer(Lecturer lecturer) {
        lecturerDao.save(lecturer);
        log.debug("Lecturer saved successfully. Name: {}}", lecturer.getName());
    }

    public Optional<Lecturer> findLecturerByNameAndSureName(String name, String sureName) {
        Optional<Lecturer> lecturer = lecturerDao.findByNameAndSureName(name, sureName);
        lecturer.ifPresent(value -> log.debug("Found lecturer {}", value));
        return lecturer;
    }

    public Optional<Lecturer> findLecturer(long lecturerId){
        Optional<Lecturer> lecturer = lecturerDao.findById(lecturerId);
        lecturer.ifPresent(value -> log.debug("Found lecturer {}", value));
        return lecturer;
    }

    public void assignSubjectToLecturer(long subjectId, long lecturerId){
        try {
            Optional<StudySubject> subject = studySubjectDao.findById(subjectId);
            Optional<Lecturer> lecturer = lecturerDao.findById(lecturerId);
            if(subject.isPresent() && lecturer.isPresent()){
                if(lecturer.get().getStudySubjects().add(subject.get())){
                    log.info("Lecturer assigned to keep this subject. lecturerId: {}, subjectId: {}", lecturerId, subjectId);
                }
            }
        }catch (DaoException e) {
            throw new ServiceException("Failed to add subject to lecturer, lecturer id: " + lecturerId
                    + " subject id: " + subjectId, e);
        }
    }

    public void removeSubjectToLecturer(long subjectId, long lecturerId){
        try {
            Optional<StudySubject> subject = studySubjectDao.findById(subjectId);
            Optional<Lecturer> lecturer = lecturerDao.findById(lecturerId);
            if(subject.isPresent() && lecturer.isPresent()){
                if(lecturer.get().getStudySubjects().remove(subject.get())){
                    log.info("Lecturer is not to keeping this subject. lecturerId: {}, subjectId: {}", lecturerId, subjectId);
                }
            }
        }catch (DaoException e) {
            throw new ServiceException("Failed to remove subject from lecturer, lecturer id: " + lecturerId
                    + " subject id: " + subjectId, e);
        }
    }

}
