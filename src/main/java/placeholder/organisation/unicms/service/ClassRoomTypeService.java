package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.repository.ClassRoomTypeRepository;
import placeholder.organisation.unicms.entity.ClassRoomType;
import placeholder.organisation.unicms.service.createDTO.ClassRoomDTO;
import placeholder.organisation.unicms.service.createDTO.ClassRoomTypeDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.mapper.ClassRoomTypeMapper;

import java.util.List;
import java.util.Optional;


@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomTypeService {

    private final ClassRoomTypeRepository classRoomTypeRepository;
    private final ClassRoomTypeMapper classRoomTypeMapper;

    public ClassRoomTypeService(ClassRoomTypeRepository classRoomTypeRepository, ClassRoomTypeMapper classRoomTypeMapper) {
        this.classRoomTypeRepository = classRoomTypeRepository;
        this.classRoomTypeMapper = classRoomTypeMapper;
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

    @Transactional
    public void removeClassRoomType(long classRoomId) {
        if (!classRoomTypeRepository.existsById(classRoomId)) {
            throw new ServiceException("Classroom type not found with id: " + classRoomId);
        }
        classRoomTypeRepository.deleteById(classRoomId);
    }

    @Transactional
    public void updateClassRoomType(long classRoomTypeId, ClassRoomTypeDTO classRoomTypeDTO) {
        ClassRoomType classRoom = classRoomTypeRepository.findById(classRoomTypeId)
                .orElseThrow(() -> new ServiceException("ClassRoom type not found with id: " + classRoomTypeId));
        try {
            classRoomTypeMapper.updateEntityFromDto(classRoomTypeDTO, classRoom);
        } catch (Exception e) {
            log.error("Failed to map DTO to Entity for classroom id: {}", classRoomTypeId, e);
            throw new ServiceException("Error updating classroom", e);
        }
    }
}
