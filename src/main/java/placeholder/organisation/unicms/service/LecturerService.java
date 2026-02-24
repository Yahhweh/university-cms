package placeholder.organisation.unicms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import placeholder.organisation.unicms.entity.Group;
import placeholder.organisation.unicms.repository.LecturerRepository;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.service.dto.LecturerDTO;
import placeholder.organisation.unicms.service.mapper.LecturerMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerMapper lecturerMapper;
    private final FilterAndSorterOfEntities filterAndSorterOfEntities;

    public LecturerService(LecturerRepository lecturerRepository, SubjectRepository subjectRepository,
                           LecturerMapper lecturerMapper, FilterAndSorterOfEntities filterAndSorterOfEntities) {
        this.lecturerRepository = lecturerRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerMapper = lecturerMapper;
        this.filterAndSorterOfEntities = filterAndSorterOfEntities;
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
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new EntityNotFoundException(Subject.class, String.valueOf(subjectId)));
        Lecturer lecturer = lecturerRepository.findById(lecturerId).orElseThrow(
                () -> new EntityNotFoundException(Lecturer.class, String.valueOf(lecturerId))
        );
        if (lecturer.getSubjects().add(subject)) {
            log.info("Lecturer assigned to keep this subject. lecturerId: {}, subjectId: {}", lecturerId, subjectId);
        }
    }

    public void removeSubjectFromLecturer(long subjectId, long lecturerId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(Subject.class, String.valueOf(subjectId)));

        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException(Lecturer.class, String.valueOf(lecturerId)));

        boolean isRemoved = lecturer.getSubjects().remove(subject);
        if (!isRemoved) {
            throw new ServiceException("Subject " + subjectId + " is not related to lecturer " + lecturerId);
        }
        log.info("Subject {} removed from lecturer {}", subjectId, lecturerId);
    }

    @Transactional
    public void removeLecturer(long lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) {
            throw new EntityNotFoundException(Lecturer.class, String.valueOf(lecturerId));
        }
        lecturerRepository.deleteById(lecturerId);
    }

    @Transactional
    public void updateLecturer(long lecturerId, LecturerDTO lecturerDTO) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
                .orElseThrow(() -> new EntityNotFoundException(Lecturer.class, String.valueOf(lecturerId)));
        lecturerMapper.updateEntityFromDto(lecturerDTO, lecturer);
        resolveRelations(lecturerDTO, lecturer);
        lecturerRepository.save(lecturer);
        log.debug("Lecturer updated successfully. ID: {}", lecturerId);
    }

    public Page<Lecturer> getFilteredAndSortedLecturers(String sortField, String sortDir, int pageNo){
        return filterAndSorterOfEntities.getFilteredAndSortedEntities(sortField, sortDir, lecturerRepository, Specification.where(null), pageNo);
    }

    private void resolveRelations(LecturerDTO dto, Lecturer lecturer) {
        if (dto.getStudySubjectIds() != null && !dto.getStudySubjectIds().isEmpty()) {
            List<Subject> subjects = subjectRepository.findAllById(dto.getStudySubjectIds());

            if (subjects.size() != dto.getStudySubjectIds().size()) {
                throw new EntityNotFoundException(Lecturer.class, lecturer.getName());
            }

            lecturer.setSubjects(new HashSet<>(subjects));
        }
    }


}
