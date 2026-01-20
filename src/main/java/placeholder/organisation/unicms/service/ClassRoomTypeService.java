package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.dao.ClassRoomTypeJpa;
import placeholder.organisation.unicms.entity.ClassRoomType;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomTypeService {

    private final ClassRoomTypeJpa classRoomTypeJpa;

    public ClassRoomTypeService(ClassRoomTypeJpa classRoomTypeJpa) {
        this.classRoomTypeJpa = classRoomTypeJpa;
    }

    public List<ClassRoomType> findAllRoomTypes() {
        List<ClassRoomType> classRoomTypes = classRoomTypeJpa.findAll();
        log.debug("Found {} classroom types ", classRoomTypes.size());
        return classRoomTypes;
    }

    @Transactional
    public void createClassroomType(ClassRoomType classRoomTypeName) {
        classRoomTypeJpa.save(classRoomTypeName);
        log.debug("Classroom type saved successfully. Name: {}}", classRoomTypeName.getName());
    }

    public Optional<ClassRoomType> findClassRoomTypeByName(String classRoomTypeName) {
        Optional<ClassRoomType> classRoomType = classRoomTypeJpa.findByName(classRoomTypeName);
        classRoomType.ifPresent(value -> log.debug("Found classroom type {}", value));
        return classRoomType;
    }

    public Optional<ClassRoomType> findClassRoomType(long classRoomTypeId) {
        Optional<ClassRoomType> classRoomType = classRoomTypeJpa.findById(classRoomTypeId);
        classRoomType.ifPresent(value -> log.debug("Found classroom type {}", value));
        return classRoomType;
    }

}
