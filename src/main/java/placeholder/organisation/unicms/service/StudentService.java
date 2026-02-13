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
        log.debug("Found {} students", students.size());
        return students;
    }

    public Optional<Student> findStudent(long studentId) {
        return studentRepository.findById(studentId);
    }

    @Transactional
    public void createStudent(Student student) {
        studentRepository.save(student);
        log.info("Student saved: {} {}", student.getName(), student.getSureName());
    }

    @Transactional
    public void removeStudent(long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new EntityNotFoundException(Student.class, String.valueOf(studentId));
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(long studentId, StudentDTO studentDTO) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(Student.class, String.valueOf(studentId)));

        studentMapper.updateEntityFromDto(studentDTO, student);
        studentRepository.save(student);

        log.debug("Student updated successfully. ID: {}", studentId);
    }
}