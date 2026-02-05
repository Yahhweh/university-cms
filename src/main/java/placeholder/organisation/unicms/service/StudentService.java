package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.repository.StudentRepository;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.service.dto.StudentDTO;
import placeholder.organisation.unicms.service.mapper.StudentMapper;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public List<Student> findAllStudents() {
        List<Student> students = studentRepository.findAll();
        log.debug("Found {} students in DB", students.size());
        return students;
    }

    public Optional<Student> findStudent(long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        student.ifPresent(value -> log.debug("Found student: {}", value));
        return student;
    }

    @Transactional
    public void createStudent(Student student) {
        if (student == null) {
            log.error("Attempt to save a null student object");
            throw new IllegalArgumentException("Student cannot be null");
        }
        studentRepository.save(student);
        log.info("Student saved successfully. Name: {}, Surname: {}", student.getName(), student.getSureName());
    }

    @Transactional
    public void updateStudent(Student student) {
        if (student == null || student.getId() == null) {
            log.error("Attempt to update student with null object or ID");
            throw new IllegalArgumentException("Student and Student ID cannot be null");
        }
        studentRepository.save(student);
        log.info("Student updated successfully. ID: {}, Name: {}", student.getId(), student.getName());
    }

    @Transactional
    public void deleteStudent(long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isEmpty()) {
            log.warn("Student with ID {} not found for deletion", studentId);
            return;
        }
        studentRepository.delete(student.get());
        log.info("Student deleted. ID: {}", studentId);
    }

    @Transactional
    public void removeStudent(long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ServiceException("Student not found with id: " + studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(long studentId, StudentDTO studentDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ServiceException("Student not found with id: " + studentId));
        try {
            studentMapper.updateEntityFromDto(studentDTO, student);
            studentRepository.save(student);
        } catch (Exception e) {
            log.error("Failed to map DTO to Entity for student id: {}", studentId, e);
            throw new ServiceException("Error updating student ", e);
        }
    }
}