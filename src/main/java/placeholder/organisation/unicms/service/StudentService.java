package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.StudentJpa;
import placeholder.organisation.unicms.dao.StudentJpa;
import placeholder.organisation.unicms.dao.StudySubjectJpa;
import placeholder.organisation.unicms.entity.Student;
import placeholder.organisation.unicms.entity.StudySubject;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class StudentService {

    private final StudentJpa studentJpa;

    public StudentService(StudentJpa studentJpa) {
        this.studentJpa = studentJpa;
    }

    public List<Student> findAllStudents() {
        List<Student> students = studentJpa.findAll();
        log.debug("Found {} students in DB", students.size());
        return students;
    }

    public Optional<Student> findStudent(long studentId) {
        Optional<Student> student = studentJpa.findById(studentId);
        student.ifPresent(value -> log.debug("Found student: {}", value));
        return student;
    }

    @Transactional
    public void createStudent(Student student) {
        if (student == null) {
            log.error("Attempt to save a null student object");
            throw new IllegalArgumentException("Student cannot be null");
        }
        studentJpa.save(student);
        log.info("Student saved successfully. Name: {}, Surname: {}", student.getName(), student.getSureName());
    }

    @Transactional
    public void updateStudent(Student student) {
        if (student == null || student.getId() == null) {
            log.error("Attempt to update student with null object or ID");
            throw new IllegalArgumentException("Student and Student ID cannot be null");
        }
        studentJpa.save(student);
        log.info("Student updated successfully. ID: {}, Name: {}", student.getId(), student.getName());
    }

    @Transactional
    public void deleteStudent(long studentId) {
        Optional<Student> student = studentJpa.findById(studentId);
        if (student.isEmpty()) {
            log.warn("Student with ID {} not found for deletion", studentId);
            return;
        }
        studentJpa.delete(student.get());
        log.info("Student deleted. ID: {}", studentId);
    }

}