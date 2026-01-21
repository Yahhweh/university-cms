package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.StudentDao;
import placeholder.organisation.unicms.entity.Student;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class StudentService {

    private final StudentDao studentDao;

    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public List<Student> findAllStudents() {
        List<Student> students = studentDao.findAll();
        log.debug("Found {} students in DB", students.size());
        return students;
    }

    public Optional<Student> findStudent(long studentId) {
        Optional<Student> student = studentDao.findById(studentId);
        student.ifPresent(value -> log.debug("Found student: {}", value));
        return student;
    }

    @Transactional
    public void createStudent(Student student) {
        if (student == null) {
            log.error("Attempt to save a null student object");
            throw new IllegalArgumentException("Student cannot be null");
        }
        studentDao.save(student);
        log.info("Student saved successfully. Name: {}, Surname: {}", student.getName(), student.getSureName());
    }

    @Transactional
    public void updateStudent(Student student) {
        if (student == null || student.getId() == null) {
            log.error("Attempt to update student with null object or ID");
            throw new IllegalArgumentException("Student and Student ID cannot be null");
        }
        studentDao.save(student);
        log.info("Student updated successfully. ID: {}, Name: {}", student.getId(), student.getName());
    }

    @Transactional
    public void deleteStudent(long studentId) {
        Optional<Student> student = studentDao.findById(studentId);
        if (student.isEmpty()) {
            log.warn("Student with ID {} not found for deletion", studentId);
            return;
        }
        studentDao.delete(student.get());
        log.info("Student deleted. ID: {}", studentId);
    }

}