package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.Lecturer;
import placeholder.organisation.unicms.entity.Role;
import placeholder.organisation.unicms.entity.Subject;
import placeholder.organisation.unicms.repository.LecturerRepository;
import placeholder.organisation.unicms.repository.SubjectRepository;
import placeholder.organisation.unicms.service.dto.request.LecturerRequestDTO;
import placeholder.organisation.unicms.service.dto.response.LecturerSubjectsDTO;
import placeholder.organisation.unicms.service.dto.response.StudentGroupDTO;
import placeholder.organisation.unicms.service.mapper.LecturerMapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional(readOnly = true)
public class LecturerService {
    private final LecturerRepository lecturerRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerMapper lecturerMapper;
    private final PasswordEncoder passwordEncoder;

    public LecturerService(LecturerRepository lecturerRepository, SubjectRepository subjectRepository,
                           LecturerMapper lecturerMapper, PasswordEncoder passwordEncoder) {
        this.lecturerRepository = lecturerRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerMapper = lecturerMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Lecturer> findAllLecturers() {
        List<Lecturer> lecturers = lecturerRepository.findAll();
        log.debug("Found size {} lecturers", lecturers.size());
        return lecturers;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createLecturer(Lecturer lecturer) {
        lecturerRepository.save(lecturer);
        log.debug("Lecturer saved successfully. Name: {}}", lecturer.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void createLecturer(LecturerRequestDTO lecturerRequestDTO) {
        Lecturer lecturer = lecturerMapper.toEntity(lecturerRequestDTO);
        lecturer.setPassword(passwordEncoder.encode(lecturerRequestDTO.getPassword()));
        lecturer.getRoles().add(Role.LECTURER);
        lecturerRepository.save(lecturer);
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

    public Optional<LecturerSubjectsDTO> findLecturerDto(Long lecturerId) {
        return lecturerRepository.findById(lecturerId)
            .map(l -> new LecturerSubjectsDTO(
                l.getId(),
                l.getName() + " " + l.getSureName(),
                l.getSubjects().stream().map(Subject::getId).collect(Collectors.toSet())
            ));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @Transactional
    public void updateLecturerSubjects(List<Long> subjectIds, long lecturerId) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
            .orElseThrow(() -> new EntityNotFoundException(
                Lecturer.class, String.valueOf(lecturerId)));

        Set<Subject> newSubjects = findSubjectsByIds(subjectIds);

        lecturer.getSubjects().removeIf(s -> !newSubjects.contains(s));
        newSubjects.stream()
            .filter(s -> !lecturer.getSubjects().contains(s))
            .forEach(lecturer.getSubjects()::add);
    }

    @Transactional
    public void removeLecturer(long lecturerId) {
        if (!lecturerRepository.existsById(lecturerId)) {
            throw new EntityNotFoundException(Lecturer.class, String.valueOf(lecturerId));
        }
        lecturerRepository.deleteById(lecturerId);
    }

    @Transactional
    public void updateLecturer(long lecturerId, LecturerRequestDTO lecturerDTO) {
        Lecturer lecturer = lecturerRepository.findById(lecturerId)
            .orElseThrow(() -> new EntityNotFoundException(Lecturer.class, String.valueOf(lecturerId)));
        lecturerMapper.updateEntityFromDto(lecturerDTO, lecturer);
        resolveRelations(lecturerDTO, lecturer);
        lecturerRepository.save(lecturer);
        log.debug("Lecturer updated successfully. ID: {}", lecturerId);
    }

    public Page<Lecturer> findAll(Pageable pageable) {
        log.debug("Trying to get paginated Lecturers: {}", pageable);
        return lecturerRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Lecturer findByEmail(String email) {
        Lecturer lecturer = lecturerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(Lecturer.class, email));
        log.debug("Found lecturer with email {}", email);
        return lecturer;
    }

    public List<Lecturer> findLecturersBySubject(Long subjectId) {
        log.debug("Trying to get Lecturers by subject id: {}", subjectId);
        return lecturerRepository.findDistinctBySubjectsId(subjectId);
    }

    private void resolveRelations(LecturerRequestDTO dto, Lecturer lecturer) {
        if (dto.getStudySubjectIds() != null && !dto.getStudySubjectIds().isEmpty()) {
            List<Subject> subjects = subjectRepository.findAllById(dto.getStudySubjectIds());

            if (subjects.size() != dto.getStudySubjectIds().size()) {
                throw new EntityNotFoundException(Lecturer.class, lecturer.getName());
            }

            lecturer.setSubjects(new HashSet<>(subjects));
        }
    }

    private Set<Subject> findSubjectsByIds(List<Long> subjectIds) {
        Set<Subject> subjects = new HashSet<>();
        for (Long subjectId : subjectIds) {
            Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new EntityNotFoundException(
                    Subject.class, String.valueOf(subjectId)));
            subjects.add(subject);
        }
        return subjects;
    }
}
