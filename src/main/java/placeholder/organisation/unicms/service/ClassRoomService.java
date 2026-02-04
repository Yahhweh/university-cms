package placeholder.organisation.unicms.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.service.createDTO.ClassRoomDTO;
import placeholder.organisation.unicms.service.createDTO.ClassRoomTypeDTO;
import placeholder.organisation.unicms.service.mapper.ClassRoomMapper;
import placeholder.organisation.unicms.service.validation.ClassRoomValidation;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional(readOnly = true)
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomValidation classRoomValidation;
    private final ClassRoomMapper classRoomMapper;

    public ClassRoomService(ClassRoomRepository classRoomRepository, ClassRoomValidation classRoomValidation, ClassRoomMapper classRoomMapper) {
        this.classRoomRepository = classRoomRepository;
        this.classRoomValidation = classRoomValidation;
        this.classRoomMapper = classRoomMapper;
    }

    public List<ClassRoom> findAllRooms() {
        try {
            List<ClassRoom> classRooms = classRoomRepository.findAll();
            log.debug("Found {} classrooms ", classRooms.size());
            return classRooms;
        } catch (RuntimeException e) {
            log.error("Database error while fetching classrooms", e);
            throw new ServiceException("Error retrieving rooms", e);
        }
    }

    @Transactional
    public void createClassRoom(ClassRoom classRoom) {
        if(!classRoomValidation.isClassRoomInCorrectCorpus(classRoom)) throw  new ServiceException("Error in validation of classroom");
        classRoomRepository.save(classRoom);
        log.debug("Classroom saved successfully. Name: {}}", classRoom.getRoom());
    }

    public Optional<ClassRoom> findClassRoomByName(String classRoomName) {
        try {
            Optional<ClassRoom> classRoom = classRoomRepository.findByRoom(classRoomName);
            classRoom.ifPresent(value -> log.debug("Found classroom by name {}", value));
            return classRoom;
        } catch (RuntimeException e) {
            log.error("Database error while searching for classroom name: {}", classRoomName, e);
            throw new ServiceException("Error finding lesson", e);
        }
    }

    public Optional<ClassRoom> findClassRoom(long classRoomId) {
        try {
            Optional<ClassRoom> classRoom = classRoomRepository.findById(classRoomId);
            classRoom.ifPresent(value -> log.debug("Found classroom {}", value));
            return classRoom;
        } catch (RuntimeException e) {
            log.error("Database error while searching for classroom  id: {}", classRoomId, e);
            throw new ServiceException("Error finding classroom", e);
        }
    }

    @Transactional
    public void removeClassRoom(long classRoomId) {
        if (!classRoomRepository.existsById(classRoomId)) {
            throw new ServiceException("Classroom not found with id: " + classRoomId);
        }
        classRoomRepository.deleteById(classRoomId);
    }

    @Transactional
    public void updateClassRoom(long classRoomId, ClassRoomDTO classRoomDTO) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new ServiceException("ClassRoom not found with id: " + classRoomId));
        try {
            classRoomMapper.updateEntityFromDto(classRoomDTO, classRoom);
        } catch (Exception e) {
            log.error("Failed to map DTO to Entity for classroom id: {}", classRoomId, e);
            throw new ServiceException("Error updating classroom", e);
        }
    }
}