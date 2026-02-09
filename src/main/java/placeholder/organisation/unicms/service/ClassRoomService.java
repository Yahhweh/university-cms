package placeholder.organisation.unicms.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import placeholder.organisation.unicms.excpetion.EntityNotFoundException;
import placeholder.organisation.unicms.excpetion.EntityValidationException;
import placeholder.organisation.unicms.repository.ClassRoomRepository;
import placeholder.organisation.unicms.entity.ClassRoom;
import placeholder.organisation.unicms.service.dto.ClassRoomDTO;
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
        List<ClassRoom> classRooms = classRoomRepository.findAll();
        log.debug("Found {} classrooms", classRooms.size());
        return classRooms;
    }

    @Transactional
    public void createClassRoom(ClassRoom classRoom) {
        classRoomValidation.validateClassRoom(classRoom);

        classRoomRepository.save(classRoom);
        log.debug("Classroom saved successfully: {}", classRoom.getRoom());
    }

    public Optional<ClassRoom> findClassRoomByName(String classRoomName) {
        return classRoomRepository.findByRoom(classRoomName);
    }

    public Optional<ClassRoom> findClassRoom(long classRoomId) {
        return classRoomRepository.findById(classRoomId);
    }

    @Transactional
    public void removeClassRoom(long classRoomId) {
        if (!classRoomRepository.existsById(classRoomId)) {
            throw new EntityNotFoundException(ClassRoom.class, String.valueOf(classRoomId));
        }
        classRoomRepository.deleteById(classRoomId);
    }

    @Transactional
    public void updateClassRoom(long classRoomId, ClassRoomDTO classRoomDTO) {
        ClassRoom classRoom = classRoomRepository.findById(classRoomId)
                .orElseThrow(() -> new EntityNotFoundException(ClassRoom.class, String.valueOf(classRoomId)));

        classRoomMapper.updateEntityFromDto(classRoomDTO, classRoom);
        classRoomRepository.save(classRoom);
        log.debug("Classroom updated successfully. ID: {}", classRoomId);
    }
}