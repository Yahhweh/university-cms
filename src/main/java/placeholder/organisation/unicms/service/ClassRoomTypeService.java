package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.ClassRoomTypeRepository;
import placeholder.organisation.unicms.entity.ClassRoomType;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomTypeService {

    private final ClassRoomTypeRepository classRoomTypeRepository;

    public ClassRoomTypeService(ClassRoomTypeRepository classRoomTypeRepository) {
        this.classRoomTypeRepository = classRoomTypeRepository;
    }

    public List<ClassRoomType> findAllRoomTypes() {
        List<ClassRoomType> classRoomTypes = classRoomTypeRepository.findAll();
        log.debug("Found {} classroom types ", classRoomTypes.size());
        return classRoomTypes;
    }

    @Transactional
    public void createClassroomType(ClassRoomType classRoomTypeName) {
        classRoomTypeRepository.save(classRoomTypeName);
        log.debug("Classroom type saved successfully. Name: {}}", classRoomTypeName.getName());
    }

    public Optional<ClassRoomType> findClassRoomTypeByName(String classRoomTypeName) {
        Optional<ClassRoomType> classRoomType = classRoomTypeRepository.findByName(classRoomTypeName);
        classRoomType.ifPresent(value -> log.debug("Found classroom type {}", value));
        return classRoomType;
    }

    public Optional<ClassRoomType> findClassRoomType(long classRoomTypeId) {
        Optional<ClassRoomType> classRoomType = classRoomTypeRepository.findById(classRoomTypeId);
        classRoomType.ifPresent(value -> log.debug("Found classroom type {}", value));
        return classRoomType;
    }

    public void removeClassRoomType(long classRoomTypeId){
        try {
            Optional<ClassRoomType> classRoomType = classRoomTypeRepository.findById(classRoomTypeId);
            classRoomType.ifPresent(classRoomTypeRepository::delete);
        }catch (RuntimeException e){
            log.error("Failed to delete classroom type with id: {}", classRoomTypeId);
            throw new ServiceException("Error deleting classroom type");
        }
    }



}
